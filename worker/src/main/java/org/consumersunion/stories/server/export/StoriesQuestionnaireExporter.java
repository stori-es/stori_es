package org.consumersunion.stories.server.export;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.consumersunion.stories.common.shared.dto.tasks.ExportTask;
import org.consumersunion.stories.common.shared.model.StorySortField;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;
import org.consumersunion.stories.server.business_logic.StoryService;
import org.consumersunion.stories.server.export.renderers.story.StoryColumnsRenderer;
import org.consumersunion.stories.server.persistence.QuestionnaireI15dPersister;
import org.consumersunion.stories.server.persistence.TaskPersister;

import com.google.common.collect.Lists;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_EXPLICIT;
import static org.consumersunion.stories.server.export.StoryAnswerSetCsvWriter.ANSWERS_START_INDEX;

public class StoriesQuestionnaireExporter extends StoriesCollectionExporter {
    private static final int windowSize = 75;

    private final QuestionnaireI15d questionnaireI15d;
    private final Map<BlockType, String> blockLabels;
    private final List<String> questionHeaders;

    StoriesQuestionnaireExporter(
            StoryService storyService,
            CsvWriterFactory csvWriterFactory,
            TaskPersister taskPersister,
            QuestionnaireI15dPersister questionnaireI15dPersister,
            AmazonS3ExportService amazonS3ExportService,
            QuestionnaireI15dPersister questionnairePersister,
            ExportTask exportTask,
            StoryColumnsRenderer storyColumnsRenderer) throws IOException {
        super(storyService, csvWriterFactory, taskPersister, questionnaireI15dPersister, amazonS3ExportService,
                exportTask, storyColumnsRenderer);

        questionnaireI15d = questionnairePersister.get(objectId);
        blockLabels = getBlockKeyMap();
        questionHeaders = getQuestionHeaders();
        questionHeaders.removeAll(blockLabels.values());
    }

    @Override
    protected StoryAnswerSetCsvWriter createWriter() throws IOException {
        return new StoryAnswerSetCsvWriter(writer, CSV_PREFERENCE, storyColumnsRenderer);
    }

    @Override
    public void exportData(StoryAnswerSetCsvWriter writer) throws IOException {
        if (questionnaireI15d != null) {
            StorySearchParameters parameters = new StorySearchParameters(0, windowSize, null, objectId, null, null,
                    null, ACCESS_MODE_EXPLICIT, StorySortField.CREATED_NEW);

            doExportData(writer, parameters);
        }
    }

    @Override
    protected void writeStory(StoryAnswerSetCsvWriter writer, StoryCsv story) throws IOException {
        writer.write(story, questionHeaders, blockLabels);
    }

    @Override
    protected String[] getHeaders() {
        List<String> headers = Lists.newArrayList();
        headers.addAll(storyColumnsRenderer.getColumns());

        List<String> questionHeaders = getQuestionHeaders();
        questionHeaders.removeAll(getBlockKeyMap().values());
        headers.addAll(ANSWERS_START_INDEX, questionHeaders);

        return headers.toArray(new String[headers.size()]);
    }

    private List<String> getQuestionHeaders() {
        List<String> headers = Lists.newArrayList();

        List<Block> blocks = questionnaireI15d.getSurvey().getBlocks();
        for (Block block : blocks) {
            if (block instanceof Question && blockNotTitleOrText(block)) {
                headers.add(((Question) block).getLabel());
            }
        }

        return headers;
    }

    private boolean blockNotTitleOrText(Block block) {
        return !BlockType.STORY_ASK.equals(block.getStandardMeaning())
                && !BlockType.STORY_TITLE.equals(block.getStandardMeaning());
    }

    private Map<BlockType, String> getBlockKeyMap() {
        return getBlockKeyMap(questionnaireI15d);
    }
}
