package org.consumersunion.stories.server.business_logic;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.StoryLink;
import org.consumersunion.stories.server.index.story.UpdatedStoryCollectionIndexer;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class CollectionIndexerServiceImplTest {
    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(UpdatedStoryCollectionIndexer.class);
        }
    }

    @Inject
    CollectionIndexerServiceImpl collectionIndexerService;
    @Inject
    OrganizationService organizationService;
    @Inject
    UpdatedStoryCollectionIndexer updatedStoryCollectionIndexer;

    @Test
    public void index_willUpdateStoryCollectionLinks() {
        int orgId = 1;
        given(organizationService.get(orgId)).willReturn(new Organization());
        Collection collection = new Collection();
        collection.setOwner(orgId);

        int storyId = 2;
        Set<StoryLink> stories = new HashSet<StoryLink>();
        stories.add(new StoryLink(storyId));
        collection.setStories(stories);

        // When
        collectionIndexerService.index(collection);

        // Then
        verify(updatedStoryCollectionIndexer).index(storyId);
    }
}
