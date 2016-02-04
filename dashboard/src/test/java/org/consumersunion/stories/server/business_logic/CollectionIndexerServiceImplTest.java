package org.consumersunion.stories.server.business_logic;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.StoryLink;
import org.consumersunion.stories.server.solr.story.UpdatedStoryCollectionIndexer;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class CollectionIndexerServiceImplTest {
    @Inject
    CollectionIndexerServiceImpl collectionIndexerService;
    @Inject
    OrganizationService organizationService;
    @Inject
    IndexerService indexerService;

    @Test
    public void index_willUpdateStoryCollectionLinks() {
        int orgId = 1;
        given(organizationService.get(orgId)).willReturn(new Organization());
        Collection collection = new Collection();
        collection.setOwner(orgId);

        Set<StoryLink> stories = new HashSet<StoryLink>();
        stories.add(new StoryLink(2));
        collection.setStories(stories);

        // When
        collectionIndexerService.index(collection);

        // Then
        verify(indexerService, Mockito.times(1)).process(isA(UpdatedStoryCollectionIndexer.class));
    }
}
