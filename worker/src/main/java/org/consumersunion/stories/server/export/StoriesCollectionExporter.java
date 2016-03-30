package org.consumersunion.stories.server.export;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.consumersunion.stories.common.shared.dto.tasks.ExportTask;
import org.consumersunion.stories.common.shared.model.StorySortField;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;
import org.consumersunion.stories.server.business_logic.StoryService;
import org.consumersunion.stories.server.exception.NotFoundException;
import org.consumersunion.stories.server.export.renderers.story.StoryColumnsRenderer;
import org.consumersunion.stories.server.persistence.QuestionnaireI15dPersister;
import org.consumersunion.stories.server.persistence.TaskPersister;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_EXPLICIT;
import static org.consumersunion.stories.common.shared.model.document.BlockType.CITY;
import static org.consumersunion.stories.common.shared.model.document.BlockType.FIRST_NAME;
import static org.consumersunion.stories.common.shared.model.document.BlockType.LAST_NAME;
import static org.consumersunion.stories.common.shared.model.document.BlockType.STATE;
import static org.consumersunion.stories.common.shared.model.document.BlockType.STREET_ADDRESS_1;
import static org.consumersunion.stories.common.shared.model.document.BlockType.ZIP_CODE;

public class StoriesCollectionExporter extends CsvExporter<StoryAnswerSetCsvWriter> {
    private static final int windowSize = 75;
    private static final Set<BlockType> STD_BLOCK_TYPES =
            Sets.newHashSet(FIRST_NAME, LAST_NAME, STREET_ADDRESS_1, CITY, STATE, ZIP_CODE);

    protected final StoryService storyService;
    protected final StoryColumnsRenderer storyColumnsRenderer;

    private final QuestionnaireI15dPersister questionnaireI15dPersister;
    private final Map<Integer, Map<BlockType, String>> questionnaires = Maps.newHashMap();

    StoriesCollectionExporter(
            StoryService storyService,
            CsvWriterFactory csvWriterFactory,
            TaskPersister taskPersister,
            QuestionnaireI15dPersister questionnaireI15dPersister,
            AmazonS3ExportService amazonS3ExportService,
            ExportTask exportTask,
            StoryColumnsRenderer storyColumnsRenderer) throws IOException {
        super(csvWriterFactory, taskPersister, amazonS3ExportService, exportTask, "stories");

        this.questionnaireI15dPersister = questionnaireI15dPersister;
        this.storyColumnsRenderer = storyColumnsRenderer;
        this.storyService = storyService;
    }

    @Override
    protected StoryAnswerSetCsvWriter createWriter() throws IOException {
        return csvWriterFactory.createStoryAnswerSetCsvWriter(writer, CSV_PREFERENCE, storyColumnsRenderer);
    }

    @Override
    public void exportData(StoryAnswerSetCsvWriter writer) throws IOException {
        StorySearchParameters parameters = new StorySearchParameters(0, windowSize, objectId, null, null, null, null,
                ACCESS_MODE_EXPLICIT, StorySortField.CREATED_NEW);

        doExportData(writer, parameters);
    }

    protected void doExportData(
            StoryAnswerSetCsvWriter writer,
            StorySearchParameters parameters) throws IOException {
        StoryExport<StoryCsv> stories;
        do {
            stories = storyService.exportStories(profileId, parameters);
            if (parameters.getStart() == 0) {
                exportTask.setTotal(stories.getTotal());
                taskPersister.setTotal(exportTask);
            }

            for (StoryCsv story : stories.getValues()) {
                writeStory(writer, story);
            }

            exportTask.setCount(exportTask.getCount() + stories.getValues().size());
            taskPersister.updateCount(exportTask);

            parameters.setStart(parameters.getStart() + parameters.getLength());
        } while (!stories.getValues().isEmpty());
    }

    protected void writeStory(StoryAnswerSetCsvWriter writer, StoryCsv story) throws IOException {
        Map<BlockType, String> blockKeyMap;
        if (story.getAnswerSet() != null) {
            int id = story.getAnswerSet().getQuestionnaire();
            blockKeyMap = questionnaires.get(id);
            if (blockKeyMap == null) {
                try {
                    QuestionnaireI15d questionnaireI15d = questionnaireI15dPersister.getIncludeDeleted(id);

                    blockKeyMap = getBlockKeyMap(questionnaireI15d);
                    questionnaires.put(id, blockKeyMap);
                } catch (NotFoundException ignored) {
                    blockKeyMap = Maps.newHashMap();
                }
            }
        } else {
            blockKeyMap = Maps.newHashMap();
        }

        writer.write(story, Lists.<String>newArrayList(), blockKeyMap);
    }

    @Override
    protected String[] getHeaders() {
        List<String> columns = storyColumnsRenderer.getColumns();

        return columns.toArray(new String[columns.size()]);
    }

    protected Map<BlockType, String> getBlockKeyMap(QuestionnaireI15d questionnaireI15d) {
        Map<BlockType, String> blockLabels = Maps.newHashMap();
        for (Block block : questionnaireI15d.getBlocks()) {
            if (STD_BLOCK_TYPES.contains(block.getBlockType())) {
                blockLabels.put(block.getBlockType(), ((Question) block).getLabel());
            }
        }

        return blockLabels;
    }
}
