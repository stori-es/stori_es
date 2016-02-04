package org.consumersunion.stories.server.business_logic;

import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.StoryLink;
import org.consumersunion.stories.server.persistence.AuthPersister;
import org.consumersunion.stories.server.solr.IndexerFactory;
import org.consumersunion.stories.server.solr.collection.NewCollectionIndexer;
import org.consumersunion.stories.server.solr.story.UpdatedStoryCollectionIndexer;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

@Component
public class CollectionIndexerServiceImpl implements CollectionIndexerService {
    private final OrganizationService organizationService;
    private final AuthPersister authPersister;
    private final IndexerService indexerService;
    private final IndexerFactory indexerFactory;

    @Inject
    CollectionIndexerServiceImpl(
            OrganizationService organizationService,
            AuthPersister authPersister,
            IndexerService indexerService,
            IndexerFactory indexerFactory) {
        this.organizationService = organizationService;
        this.authPersister = authPersister;
        this.indexerService = indexerService;
        this.indexerFactory = indexerFactory;
    }

    @Override
    public void index(Collection collection) {
        Organization org = organizationService.get(collection.getOwner());
        Set<String> admins = Sets.newLinkedHashSet();
        admins.add(org.getName());
        Set<Integer> auths = authPersister.getNonStoryAuths(collection.getId(), ROLE_READER);

        NewCollectionIndexer newCollectionIndexer =
                new NewCollectionIndexer(collection, auths, auths, auths, null, admins);
        indexerService.process(newCollectionIndexer);

        for (StoryLink storyLink : collection.getStories()) {
            Integer storyId = storyLink.getStory();
            UpdatedStoryCollectionIndexer updatedStoryCollectionIndexer = indexerFactory.createUpdatedStory(storyId);
            indexerService.process(updatedStoryCollectionIndexer);
        }
    }
}
