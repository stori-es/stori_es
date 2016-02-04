package org.consumersunion.stories.dashboard.client.application.widget.addto;

import java.util.Set;

import org.consumersunion.stories.common.client.api.TaskService;
import org.consumersunion.stories.common.client.event.InteractiveMessageEvent;
import org.consumersunion.stories.common.client.i18n.CommonI18nMessages;
import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.ui.stories.StoriesListContainer;
import org.consumersunion.stories.common.client.util.CachingService;
import org.consumersunion.stories.common.client.util.CurrentUser;
import org.consumersunion.stories.common.client.widget.messages.MessageFactory;
import org.consumersunion.stories.common.shared.dto.tasks.AddStoriesToCollectionTask;
import org.consumersunion.stories.common.shared.dto.tasks.TasksFactory;
import org.consumersunion.stories.common.shared.model.StorySelectField;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;
import org.consumersunion.stories.dashboard.client.application.ui.addto.AddToMenuHandler;
import org.consumersunion.stories.dashboard.client.event.StorySelectedEvent;

import com.google.common.collect.Sets;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;

/**
 * Widget used to add stories from a CellList to one or more collections
 */
class AddStoriesToCollectionsPresenter
        extends AddToPresenter<AddToCollectionsUiHandlers, AddStoriesToCollectionsPresenter.MyView>
        implements AddToCollectionsUiHandlers {
    interface MyView extends AddToView<AddToCollectionsUiHandlers> {
        void updateCollectionLink(int numberOfCollections, int numberOfSelectedStories);

        void onItemRemoved();
    }

    private final RpcCollectionServiceAsync collectionService;
    private final TasksFactory tasksFactory;
    private final CurrentUser currentUser;
    private final ResourceDelegate<TaskService> taskServiceDelegate;
    private final MessageFactory messageFactory;
    private final CachingService cachingService;
    private final Set<Integer> collectionIds;
    private final Set<CollectionSummary> collectionSummaries;

    @Inject
    AddStoriesToCollectionsPresenter(
            EventBus eventBus,
            MyView view,
            RpcCollectionServiceAsync collectionService,
            TasksFactory tasksFactory,
            CurrentUser currentUser,
            ResourceDelegate<TaskService> taskServiceDelegate,
            StorySummarySelectionHelper storySummarySelectionHelper,
            CommonI18nMessages messages,
            MessageFactory messageFactory,
            CachingService cachingService,
            @Assisted AddToMenuHandler addToMenuHandler,
            @Assisted StoriesListContainer storiesListContainer) {
        super(eventBus, view, storySummarySelectionHelper, addToMenuHandler, storiesListContainer, messages);

        this.collectionService = collectionService;
        this.tasksFactory = tasksFactory;
        this.currentUser = currentUser;
        this.taskServiceDelegate = taskServiceDelegate;
        this.messageFactory = messageFactory;
        this.cachingService = cachingService;
        collectionIds = Sets.newLinkedHashSet();
        collectionSummaries = Sets.newLinkedHashSet();
    }

    @Override
    public void onGoClicked(Set<Integer> storyIds) {
        collectionService.linkStoriesToCollections(collectionIds, storyIds,
                this.<ActionResponse>createCallback(messages.storiesAddedSuccessfully()));
    }

    @Override
    public void onGoClicked(StorySearchParameters storySearchParameters) {
        int profileId = currentUser.getCurrentProfileId();

        AddStoriesToCollectionTask task = tasksFactory.createAddStoriesTask(profileId, collectionIds,
                storySearchParameters.getSearchToken(), storySearchParameters.getCollectionId(),
                storySearchParameters.getQuestionnaireId());

        final Set<CollectionSummary> collectionSummaries = Sets.newLinkedHashSet(this.collectionSummaries);
        taskServiceDelegate.withCallback(this.<AddStoriesToCollectionTask>createNoLoaderCallback(null,
                new AsyncCallback<AddStoriesToCollectionTask>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(AddStoriesToCollectionTask result) {
                        InteractiveMessageEvent.fire(AddStoriesToCollectionsPresenter.this,
                                messageFactory.createAddToCollections(result.getId(), collectionSummaries));
                    }
                })).createTask(task);
    }

    @Override
    public void onStorySelected(StorySelectedEvent event) {
        super.onStorySelected(event);

        updateCollectionLink();
    }

    @Override
    public void onStorySelectFieldChanged(StorySelectField currentSelectField) {
        super.onStorySelectFieldChanged(currentSelectField);

        updateCollectionLink();
    }

    @Override
    public boolean apply(CollectionData input) {
        return !collectionIds.contains(input.getId());
    }

    @Override
    public void onListItemAdded(CollectionSummary collectionSummary) {
        collectionIds.add(collectionSummary.getId());
        collectionSummaries.add(collectionSummary);
        updateCollectionLink();
    }

    @Override
    public void onCancelClicked() {
        addToMenuHandler.hideCurrent();
    }

    @Override
    public void onListItemRemoved(CollectionSummary collection) {
        collectionIds.remove(collection.getId());
        collectionSummaries.remove(collection);
        getView().onItemRemoved();
    }

    @Override
    public void onListItemClicked(CollectionSummary collection) {
        // Do nothing
    }

    @Override
    public void reset() {
        super.reset();

        collectionIds.clear();
        collectionSummaries.clear();
    }

    private void updateCollectionLink() {
        getView().updateCollectionLink(collectionIds.size(), getNumberOfSelectedStories());
    }
}
