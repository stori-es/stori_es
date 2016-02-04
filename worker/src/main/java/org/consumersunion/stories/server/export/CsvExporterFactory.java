package org.consumersunion.stories.server.export;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Provider;

import org.consumersunion.stories.common.shared.ExportContainer;
import org.consumersunion.stories.common.shared.dto.tasks.ExportTask;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.business_logic.ProfileService;
import org.consumersunion.stories.server.business_logic.StoryService;
import org.consumersunion.stories.server.export.renderers.story.StoryColumnsRenderer;
import org.consumersunion.stories.server.export.renderers.storyteller.StoryTellerColumnsRenderer;
import org.consumersunion.stories.server.persistence.QuestionnaireI15dPersister;
import org.consumersunion.stories.server.persistence.TaskPersister;
import org.springframework.stereotype.Component;

@Component
public class CsvExporterFactory {
    private final Provider<StoryService> storyServiceProvider;
    private final Provider<AmazonS3ExportService> amazonS3ExportServiceProvider;
    private final Provider<ProfileService> profileServiceProvider;
    private final Provider<TaskPersister> taskPersisterProvider;
    private final Provider<QuestionnaireI15dPersister> questionnaireI15dPersisterProvider;
    private final CsvWriterFactory csvWriterFactory;
    private final StoryColumnsRenderer storyColumnsRenderer;
    private final StoryTellerColumnsRenderer storyTellerColumnsRenderer;

    @Inject
    public CsvExporterFactory(
            Provider<StoryService> storyServiceProvider,
            Provider<AmazonS3ExportService> amazonS3ExportServiceProvider,
            Provider<ProfileService> profileServiceProvider,
            Provider<TaskPersister> taskPersisterProvider,
            Provider<QuestionnaireI15dPersister> questionnaireI15dPersisterProvider,
            CsvWriterFactory csvWriterFactory,
            StoryColumnsRenderer storyColumnsRenderer,
            StoryTellerColumnsRenderer storyTellerColumnsRenderer) {
        this.storyServiceProvider = storyServiceProvider;
        this.amazonS3ExportServiceProvider = amazonS3ExportServiceProvider;
        this.profileServiceProvider = profileServiceProvider;
        this.taskPersisterProvider = taskPersisterProvider;
        this.questionnaireI15dPersisterProvider = questionnaireI15dPersisterProvider;
        this.csvWriterFactory = csvWriterFactory;
        this.storyColumnsRenderer = storyColumnsRenderer;
        this.storyTellerColumnsRenderer = storyTellerColumnsRenderer;
    }

    public CsvExporter create(ExportTask exportTask) throws IOException {
        boolean isCollection = ExportContainer.COLLECTION.equals(exportTask.getContainer());
        switch (exportTask.getKind()) {
            case STORIES:
                StoryService storyService = storyServiceProvider.get();
                if (isCollection) {
                    return new StoriesCollectionExporter(storyService, csvWriterFactory, taskPersisterProvider.get(),
                            questionnaireI15dPersisterProvider.get(), amazonS3ExportServiceProvider.get(), exportTask,
                            storyColumnsRenderer);
                } else {
                    return new StoriesQuestionnaireExporter(storyService, csvWriterFactory, taskPersisterProvider.get(),
                            questionnaireI15dPersisterProvider.get(), amazonS3ExportServiceProvider.get(),
                            questionnaireI15dPersisterProvider.get(), exportTask, storyColumnsRenderer);
                }
            case STORYTELLERS:
                return new AuthorsExporter(profileServiceProvider.get(), csvWriterFactory, taskPersisterProvider.get(),
                        amazonS3ExportServiceProvider.get(), exportTask, isCollection, storyTellerColumnsRenderer);
            case UNKNOWN:
            default:
                throw new GeneralException("Unknown export kind: " + exportTask.getKind());
        }
    }
}
