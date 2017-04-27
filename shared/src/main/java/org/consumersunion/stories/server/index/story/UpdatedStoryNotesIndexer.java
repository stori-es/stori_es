package org.consumersunion.stories.server.index.story;

import java.util.Date;

import javax.inject.Inject;

import org.consumersunion.stories.server.index.Indexer;
import org.springframework.stereotype.Component;

@Component
public class UpdatedStoryNotesIndexer {
    private final Indexer<StoryDocument> storyIndexer;

    @Inject
    UpdatedStoryNotesIndexer(Indexer<StoryDocument> storyIndexer) {
        this.storyIndexer = storyIndexer;
    }

    public void index(int storyId, String documentText) {
        StoryDocument storyDocument = storyIndexer.get(storyId);
        if (storyDocument != null) {
            storyDocument.getStoryNotes().add(documentText);
            storyDocument.setLastModified(new Date());

            storyIndexer.index(storyDocument);
        }
    }
}
