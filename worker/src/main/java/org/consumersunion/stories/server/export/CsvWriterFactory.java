package org.consumersunion.stories.server.export;

import java.io.Writer;

import org.consumersunion.stories.server.export.renderers.story.StoryColumnsRenderer;
import org.consumersunion.stories.server.export.renderers.storyteller.StoryTellerColumnsRenderer;
import org.springframework.stereotype.Component;
import org.supercsv.prefs.CsvPreference;

@Component
public class CsvWriterFactory {
    public StoryTellerCsvWriter createStoryTellerCsvWriter(
            Writer writer,
            CsvPreference csvPreference,
            StoryTellerColumnsRenderer storyTellerColumnsRenderer) {
        return new StoryTellerCsvWriter(writer, csvPreference, storyTellerColumnsRenderer);
    }

    public StoryAnswerSetCsvWriter createStoryAnswerSetCsvWriter(
            Writer writer,
            CsvPreference csvPreference,
            StoryColumnsRenderer storyColumnsRenderer) {
        return new StoryAnswerSetCsvWriter(writer, csvPreference, storyColumnsRenderer);
    }
}
