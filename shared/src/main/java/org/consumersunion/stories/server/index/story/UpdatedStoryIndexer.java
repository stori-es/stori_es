package org.consumersunion.stories.server.index.story;

import java.util.Date;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.server.index.Indexer;
import org.springframework.stereotype.Component;

@Component
public class UpdatedStoryIndexer {
    private final Indexer<StoryDocument> storyIndexer;

    @Inject
    UpdatedStoryIndexer(Indexer<StoryDocument> storyIndexer) {
        this.storyIndexer = storyIndexer;
    }

    public void index(Story story) {
        StoryDocument storyDocument = storyIndexer.get(story.getId());
        if (storyDocument != null) {
            storyDocument.setOwner(story.getOwner());
            storyDocument.setPermalink(story.getPermalink());
            storyDocument.setLastModified(new Date());
            storyDocument.setStoryVersion(story.getVersion());

            storyIndexer.index(storyDocument);
        }
    }
}
