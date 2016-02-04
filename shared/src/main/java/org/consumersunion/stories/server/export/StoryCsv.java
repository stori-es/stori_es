package org.consumersunion.stories.server.export;

import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;

import com.google.common.base.Joiner;

public class StoryCsv extends BaseStoryExportCsv {
    private final AnswerSet answerSet;
    private final String text;
    private final String tags;
    private final String notes;
    private final String title;

    public StoryCsv(StorySummary story, String sanitizedText, AnswerSet answerSet) {
        super(story.getStoryId(), story.getStory().getCreated(), story.getStory().getUpdated());

        this.answerSet = answerSet;
        text = sanitizedText;
        title = story.getTitle();

        tags = Joiner.on(", ").join(story.getTags());
        notes = Joiner.on('\n').join(story.getNotes());
    }

    public AnswerSet getAnswerSet() {
        return answerSet;
    }

    public String getTags() {
        return tags;
    }

    public String getText() {
        return text;
    }

    public String getNotes() {
        return notes;
    }

    public String getTitle() {
        return title;
    }
}
