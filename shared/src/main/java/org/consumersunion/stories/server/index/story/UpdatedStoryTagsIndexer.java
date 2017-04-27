package org.consumersunion.stories.server.index.story;

import java.util.Date;
import java.util.LinkedHashSet;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.server.index.Indexer;
import org.springframework.stereotype.Component;

@Component
public class UpdatedStoryTagsIndexer {
    private final Indexer<StoryDocument> storyIndexer;

    @Inject
    UpdatedStoryTagsIndexer(Indexer<StoryDocument> storyIndexer) {
        this.storyIndexer = storyIndexer;
    }

    public void index(Story story, LinkedHashSet<String> tags) {
        StoryDocument storyDocument = storyIndexer.get(story.getId());
        if (storyDocument != null) {
            storyDocument.setTags(tags);
            storyDocument.setLastModified(new Date());

            storyIndexer.index(storyDocument);
        }
    }
}
