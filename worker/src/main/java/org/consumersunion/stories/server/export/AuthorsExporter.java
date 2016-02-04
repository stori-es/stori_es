package org.consumersunion.stories.server.export;

import java.io.IOException;
import java.util.List;

import org.consumersunion.stories.server.business_logic.ProfileService;
import org.consumersunion.stories.server.export.renderers.storyteller.StoryTellerColumnsRenderer;
import org.consumersunion.stories.server.persistence.TaskPersister;
import org.consumersunion.stories.common.shared.dto.tasks.ExportTask;

public class AuthorsExporter extends CsvExporter<StoryTellerCsvWriter> {
    private final Integer collectionId;
    private final Integer questionnaireId;
    private final ProfileService profileService;
    private final StoryTellerColumnsRenderer storyTellerColumnsRenderer;

    public AuthorsExporter(
            ProfileService profileService,
            CsvWriterFactory csvWriterFactory,
            TaskPersister taskPersister,
            AmazonS3ExportService amazonS3ExportService,
            ExportTask exportTask,
            boolean isCollection,
            StoryTellerColumnsRenderer storyTellerColumnsRenderer) throws IOException {
        super(csvWriterFactory, taskPersister, amazonS3ExportService, exportTask, "storytellers");

        this.profileService = profileService;
        this.storyTellerColumnsRenderer = storyTellerColumnsRenderer;

        if (isCollection) {
            collectionId = objectId;
            questionnaireId = null;
        } else {
            questionnaireId = objectId;
            collectionId = null;
        }
    }

    @Override
    protected StoryTellerCsvWriter createWriter() throws IOException {
        return csvWriterFactory.createStoryTellerCsvWriter(writer, CSV_PREFERENCE, storyTellerColumnsRenderer);
    }

    @Override
    public void exportData(StoryTellerCsvWriter writer) throws IOException {
        StoryExport<StoryTellerCsv> authors;
        int window = 0;
        do {
            authors = profileService.exportStoryTellers(profileId, collectionId, questionnaireId, window);
            if (window == 0) {
                exportTask.setTotal(authors.getTotal());
                taskPersister.setTotal(exportTask);
            }

            for (StoryTellerCsv author : authors.getValues()) {
                writer.write(author);
            }

            exportTask.setCount(exportTask.getCount() + authors.getValues().size());
            taskPersister.updateCount(exportTask);

            window += 1;
        } while (!authors.getValues().isEmpty());
    }

    @Override
    protected String[] getHeaders() {
        List<String> columns = storyTellerColumnsRenderer.getColumns();

        return columns.toArray(new String[columns.size()]);
    }
}
