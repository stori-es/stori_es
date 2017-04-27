package org.consumersunion.stories.server.index.story;

import java.util.Date;

import javax.inject.Inject;

import org.consumersunion.stories.server.index.Indexer;
import org.springframework.stereotype.Component;

@Component
public class UpdatedAuthorNotesIndexer {
    private final Indexer<StoryDocument> storyIndexer;

    @Inject
    UpdatedAuthorNotesIndexer(Indexer<StoryDocument> storyIndexer) {
        this.storyIndexer = storyIndexer;
    }

    public void index(int storyId, String documentText) {
        StoryDocument storyDocument = storyIndexer.get(storyId);
        if (storyDocument != null) {
            storyDocument.getAuthorNotes().add(documentText);
            storyDocument.setLastModified(new Date());

            storyIndexer.index(storyDocument);
        }
    }
}
