package org.consumersunion.stories.server.business_logic;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.dto.RetrievePagedCollectionsParams;
import org.consumersunion.stories.common.shared.dto.tasks.AddStoriesToCollectionTask;
import org.consumersunion.stories.common.shared.dto.tasks.TaskStatus;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.StoryLink;
import org.consumersunion.stories.common.shared.model.Subscription;
import org.consumersunion.stories.common.shared.model.Theme;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.common.shared.service.datatransferobject.DocumentsContainer;
import org.consumersunion.stories.common.shared.service.datatransferobject.PagedData;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.server.business_logic.interceptor.RequiresLoggedUser;
import org.consumersunion.stories.server.exception.NotAuthorizedException;
import org.consumersunion.stories.server.exception.NotLoggedInException;
import org.consumersunion.stories.server.exception.PermalinkAlreadyExistsException;
import org.consumersunion.stories.server.notification_channel.StoriesAddedEvent;
import org.consumersunion.stories.server.persistence.CollectionPersister;
import org.consumersunion.stories.server.persistence.StoryPersister;
import org.consumersunion.stories.server.persistence.SubscriptionPersister;
import org.consumersunion.stories.server.persistence.TaskPersister;
import org.consumersunion.stories.server.solr.IndexerFactory;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_EXPLICIT;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_ADMIN;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;
import static org.consumersunion.stories.common.shared.model.StorySortField.CREATED_NEW;
import static org.consumersunion.stories.server.util.StringUtil.generateSlug;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class CollectionServiceImpl implements CollectionService {
    private final AuthorizationService authService;
    private final UserService userService;
    private final ThemeService themeService;
    private final DocumentService documentService;
    private final OrganizationService organizationService;
    private final StoryService storyService;
    private final IndexerService indexerService;
    private final TaskPersister taskPersister;
    private final CollectionPersister collectionPersister;
    private final SubscriptionPersister subscriptionPersister;
    private final StoryPersister storyPersister;
    private final DefaultBlocksSetter defaultBlocksSetter;
    private final CollectionIndexerService collectionIndexerService;
    private final IndexerFactory indexerFactory;
    private final EventBus eventBus;

    @Inject
    CollectionServiceImpl(
            AuthorizationService authService,
            UserService userService,
            ThemeService themeService,
            DocumentService documentService,
            OrganizationService organizationService,
            StoryService storyService,
            IndexerService indexerService,
            TaskPersister taskPersister,
            CollectionPersister collectionPersister,
            SubscriptionPersister subscriptionPersister,
            StoryPersister storyPersister,
            DefaultBlocksSetter defaultBlocksSetter,
            CollectionIndexerService collectionIndexerService,
            IndexerFactory indexerFactory,
            EventBus eventBus) {
        this.authService = authService;
        this.userService = userService;
        this.themeService = themeService;
        this.documentService = documentService;
        this.organizationService = organizationService;
        this.storyService = storyService;
        this.indexerService = indexerService;
        this.taskPersister = taskPersister;
        this.collectionPersister = collectionPersister;
        this.subscriptionPersister = subscriptionPersister;
        this.storyPersister = storyPersister;
        this.defaultBlocksSetter = defaultBlocksSetter;
        this.collectionIndexerService = collectionIndexerService;
        this.indexerFactory = indexerFactory;
        this.eventBus = eventBus;
    }

    @Override
    public Collection getCollection(int id) {
        return getCollectionForRole(id, ROLE_READER).getCollection();
    }

    @Override
    public CollectionData getCollectionData(int id) {
        return getCollectionForRole(id, ROLE_READER);
    }

    @Override
    public CollectionData getCollectionForRole(int id, int minRole) {
        CollectionData collectionData = collectionPersister.getCollectionData(id);
        if (authService.isUserAuthorized(minRole, collectionData.getCollection())) {
            addSubscriptionsData(collectionData);
            addDocuments(collectionData);

            return collectionData;
        } else {
            throw new NotAuthorizedException();
        }
    }

    @Override
    @RequiresLoggedUser
    public Collection createCollection(Collection collection) throws PermalinkAlreadyExistsException {
        validateCreateCollection(collection);

        Document bodyDocument = collection.getBodyDocument();
        String availablePermalink = collectionPersister.getAvailablePermalink(generateSlug(collection.getTitle()));
        collection.setPermalink(availablePermalink);

        collection.setOwner(userService.getContextOrganizationId());
        setTheme(collection);
        defaultBlocksSetter.setFor(collection);

        bodyDocument.setPrimaryAuthor(userService.getActiveProfileId());

        Iterator<StoryLink> iterator = collection.getStories().iterator();
        while (iterator.hasNext()) {
            StoryLink storyLink = iterator.next();
            if (!authService.canWrite(storyLink.getStory())) {
                iterator.remove();
            }
        }

        Collection collectionSaved = collectionPersister.createCollection(collection);

        collectionIndexerService.index(collectionSaved);

        return collectionSaved;
    }

    @Override
    public Collection getCollectionByPermalink(String permalink) {
        return collectionPersister.retrieveByPermalink(permalink);
    }

    @Override
    public void deleteCollection(int collectionId) {
        Collection dbCollection = collectionPersister.get(collectionId);

        if (authService.isUserAuthorized(ROLE_ADMIN, dbCollection)) {
            dbCollection.setDeleted(true);

            Collection updatedCollection = collectionPersister.updateCollection(dbCollection);

            indexerService.process(indexerFactory.createUpdatedCollection(updatedCollection));
        } else {
            throw new NotAuthorizedException();
        }
    }

    @Override
    public void setTheme(Collection collection) {
        Integer organizationId = userService.getContextOrganizationId();

        Organization organization = organizationService.get(organizationId);

        // TODO : Default theme issue : https://consumersunion.atlassian.net/browse/TASK-342
        if (organization != null) {
            if (organization.getDefaultTheme() == null) {
                List<Theme> themes = themeService.getThemesForOrganization(organizationId);
                if (themes != null && !themes.isEmpty()) {
                    collection.setTheme(themes.get(0).getId());
                }
            } else {
                collection.setTheme(organization.getDefaultTheme());
            }
        }
    }

    @Override
    public List<CollectionData> searchCollections(RetrievePagedCollectionsParams params) {
        List<CollectionData> collections = collectionPersister.retrievePagedCollections(params);

        addSubscriptionsData(collections);

        return collections;
    }

    @Override
    public List<CollectionData> searchCollectionsByStory(RetrievePagedCollectionsParams params) {
        return collectionPersister.retrievePagedCollectionsByStory(params);
    }

    @Override
    public List<CollectionData> searchCollectionsByUserWithoutStoryAssociated(RetrievePagedCollectionsParams params) {
        return collectionPersister.retrievePagedCollectionsByUserWithoutStoryAssociated(params);
    }

    // TODO : Add correct logic here instead of just bridging to persister
    @Override
    public Collection updateCollection(Collection collection) {
        Collection updatedCollection = collectionPersister.updateCollection(collection);

        indexerService.process(indexerFactory.createUpdatedCollection(updatedCollection));

        return updatedCollection;
    }

    @Override
    public Collection linkStoriesToCollection(
            Collection collection,
            java.util.Collection<Integer> storyIds) {
        List<Story> stories = newArrayList();
        for (Integer storyId : storyIds) {
            Story story = storyPersister.get(storyId);
            if (authService.canWrite(story)) {
                stories.add(story);

                addOrRemoveStoryLink(collection, storyId);
            }
        }

        if (stories.isEmpty() && !storyIds.isEmpty()) {
            throw new NotAuthorizedException();
        }

        updateCollection(collection);

        eventBus.post(new StoriesAddedEvent(collection, stories));

        for (Integer storyId : storyIds) {
            indexerService.process(indexerFactory.createUpdatedStory(storyId));
        }

        return collection;
    }

    private void addOrRemoveStoryLink(Collection collection, Integer storyId) {
        if (!collection.isAssociatedToStory(storyId)) {
            final StoryLink storyLink = new StoryLink(storyId, false);
            collection.addStory(storyLink);
        } else if (collection.getDeleted()) {
            collection.getStories().remove(new StoryLink(storyId, false));
        }
    }

    @Override
    public void linkStoryToCollections(Set<Integer> collectionIds, Story savedStory) {
        if (collectionIds != null && !collectionIds.isEmpty()) {
            List<Collection> collections = collectionPersister.get(Lists.newArrayList(collectionIds));
            for (Collection collection : collections) {
                linkStoriesToCollection(collection, Collections.singletonList(savedStory));
            }
        }
    }

    @Override
    public Collection linkStorySummariesToCollection(Collection collection, List<StorySummary> storySummaries) {
        List<Story> stories = Lists.transform(storySummaries, new Function<StorySummary, Story>() {
            @Override
            public Story apply(StorySummary input) {
                return input.getStory();
            }
        });

        return linkStoriesToCollection(collection, stories);
    }

    @Override
    public void linkStoriesToCollection(AddStoriesToCollectionTask task) {
        StorySearchParameters storySearchParameters = new StorySearchParameters(0, 0, task.getCollectionId(),
                task.getQuestionnaireId(), task.getSearchToken(), null, null, ACCESS_MODE_EXPLICIT, CREATED_NEW);

        int profileId = task.getProfileId();
        int storiesCount = storyService.getStoriesCount(storySearchParameters, profileId);
        task.setTotal(storiesCount);
        taskPersister.setTotal(task);
        taskPersister.changeStatus(task, TaskStatus.RUNNING);

        int pageSize = 10;
        storySearchParameters.setLength(pageSize);
        for (int i = storySearchParameters.getStart(); i < storiesCount; i += pageSize) {
            storySearchParameters.setStart(i);
            PagedData<StorySummary> stories = storyService.getStories(storySearchParameters, profileId);

            task.setCount(storySearchParameters.getStart());
            taskPersister.updateCount(task);

            for (Integer collectionId : task.getCollectionIds()) {
                Collection collection = getCollection(collectionId);
                // Check if task was canceled
                taskPersister.changeStatus(task, TaskStatus.RUNNING);
                linkStorySummariesToCollection(collection, stories.getData());
            }
        }

        task.setCount(task.getTotal());
        taskPersister.updateCount(task);
        taskPersister.changeStatus(task, TaskStatus.DONE);
    }

    private Collection linkStoriesToCollection(Collection collection, List<Story> stories) {
        for (Story story : stories) {
            if (!authService.canWrite(story)) {
                throw new NotAuthorizedException();
            }

            int storyId = story.getId();
            addOrRemoveStoryLink(collection, storyId);
        }

        updateCollection(collection);

        for (Story story : stories) {
            indexerService.process(indexerFactory.createUpdatedStory(story.getId()));
        }

        return collection;
    }

    private void addSubscriptionsData(List<CollectionData> collections) {
        List<Integer> collectionIds = Lists.transform(collections, new Function<CollectionData, Integer>() {
            @Override
            public Integer apply(CollectionData input) {
                return input.getId();
            }
        });

        int profileId = userService.getActiveProfileId();
        Map<Integer, java.util.Collection<Subscription>> subscriptions =
                subscriptionPersister.getActiveSubscriptions(collectionIds, profileId);

        for (CollectionData collection : collections) {
            java.util.Collection<Subscription> subscriptionsForCollection = subscriptions.get(collection.getId());

            collection.setSubscriptionsForActiveProfile(subscriptionsForCollection == null
                    ? Lists.<Subscription>newArrayList()
                    : newArrayList(subscriptionsForCollection));
        }
    }

    private void addSubscriptionsData(CollectionData collection) {
        User loggedInUser = userService.getLoggedInUser();
        if (loggedInUser != null) {
            int profileId = userService.getEffectiveSubject(loggedInUser);
            Map<Integer, java.util.Collection<Subscription>> subscriptions =
                    subscriptionPersister.getActiveSubscriptions(Lists.newArrayList(collection.getId()), profileId);

            java.util.Collection<Subscription> subscriptionsForCollection = subscriptions.get(collection.getId());

            collection.setSubscriptionsForActiveProfile(subscriptionsForCollection == null
                    ? Lists.<Subscription>newArrayList()
                    : newArrayList(subscriptionsForCollection));
        }
    }

    private void addDocuments(CollectionData collectionData) {
        List<Document> documents = documentService.getDocuments(collectionData.getCollection());
        collectionData.setDocuments(new DocumentsContainer(documents));
    }

    private void validateCreateCollection(Collection collection) {
        // TODO: i18n
        if (collection.getTitle() == null) {
            throw new IllegalArgumentException("Collection must define title.");
        }

        if (collection.getBodyDocument() == null) {
            throw new IllegalArgumentException("Collection must define body Document.");
        }
    }
}
