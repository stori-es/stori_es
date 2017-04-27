package org.consumersunion.stories.server.index.story;

import java.util.Date;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.server.index.Indexer;
import org.springframework.stereotype.Component;

@Component
public class UpdatedStoryDocumentIndexer {
    private final Indexer<StoryDocument> storyIndexer;

    @Inject
    UpdatedStoryDocumentIndexer(Indexer<StoryDocument> storyIndexer) {
        this.storyIndexer = storyIndexer;
    }

    public void index(int storyId, Document document) {
        StoryDocument storyDocument = storyIndexer.get(storyId);
        if (storyDocument != null) {
            if (document instanceof AnswerSet) {
                updateFromAnswerSet(storyDocument, document);
            } else {
                updateFromDocument(storyDocument, document);
            }

            storyIndexer.index(storyDocument);
        }
    }

    private void updateFromAnswerSet(StoryDocument storyDocument, Document document) {
        storyDocument.setLastModified(document.getUpdated());
    }

    private void updateFromDocument(StoryDocument storyDocument, Document document) {
        if (document != null) {
            storyDocument.setTitle(document.getTitle());
            storyDocument.setPrimaryText(document.getFirstContent());
            storyDocument.setDefaultContentId(document.getId());
            storyDocument.setLastModified(document.getUpdated());
        } else {
            storyDocument.setTitle(null);
            storyDocument.setPrimaryText(null);
            storyDocument.setDefaultContentId(null);
            storyDocument.setLastModified(new Date());
        }
    }
}
