package org.consumersunion.stories.server.export;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.consumersunion.stories.server.export.renderers.ColumnsRenderer;
import org.supercsv.io.AbstractCsvWriter;
import org.supercsv.prefs.CsvPreference;

public class StoryTellerCsvWriter extends AbstractCsvWriter {
    private final ColumnsRenderer<StoryTellerCsv> storyTellerColumnsRenderer;

    public StoryTellerCsvWriter(
            Writer writer,
            CsvPreference preference,
            ColumnsRenderer<StoryTellerCsv> storyTellerColumnsRenderer) {
        super(writer, preference);

        this.storyTellerColumnsRenderer = storyTellerColumnsRenderer;
    }

    public void write(StoryTellerCsv storyTeller) throws IOException {
        super.incrementRowAndLineNo();

        List<String> values = storyTellerColumnsRenderer.render(storyTeller);

        super.writeRow(values);
    }
}
