package org.consumersunion.stories.server.business_logic;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.StoryLink;
import org.consumersunion.stories.server.index.Indexer;
import org.consumersunion.stories.server.index.collection.CollectionDocument;
import org.consumersunion.stories.server.index.story.UpdatedStoryCollectionIndexer;
import org.consumersunion.stories.server.persistence.AuthPersister;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

@Component
public class CollectionIndexerServiceImpl implements CollectionIndexerService {
    private final Provider<UpdatedStoryCollectionIndexer> updatedStoryCollectionIndexerProvider;
    private final OrganizationService organizationService;
    private final AuthPersister authPersister;
    private final Indexer<CollectionDocument> collectionIndexer;

    @Inject
    CollectionIndexerServiceImpl(
            Provider<UpdatedStoryCollectionIndexer> updatedStoryCollectionIndexerProvider,
            OrganizationService organizationService,
            AuthPersister authPersister,
            Indexer<CollectionDocument> collectionIndexer) {
        this.updatedStoryCollectionIndexerProvider = updatedStoryCollectionIndexerProvider;
        this.organizationService = organizationService;
        this.authPersister = authPersister;
        this.collectionIndexer = collectionIndexer;
    }

    @Override
    public void index(Collection collection) {
        Organization org = organizationService.get(collection.getOwner());
        Set<String> admins = Sets.newLinkedHashSet();
        admins.add(org.getName());
        Set<Integer> auths = authPersister.getNonStoryAuths(collection.getId(), ROLE_READER);

        collectionIndexer.index(new CollectionDocument(collection, null, auths, auths, auths, admins));

        for (StoryLink storyLink : collection.getStories()) {
            UpdatedStoryCollectionIndexer updatedStoryCollectionIndexer = updatedStoryCollectionIndexerProvider.get();
            updatedStoryCollectionIndexer.index(storyLink.getStory());
        }
    }
}
