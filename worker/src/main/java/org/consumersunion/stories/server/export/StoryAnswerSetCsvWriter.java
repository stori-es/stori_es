package org.consumersunion.stories.server.export;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.server.export.renderers.ColumnsRenderer;
import org.jsoup.Jsoup;
import org.supercsv.io.AbstractCsvWriter;
import org.supercsv.prefs.CsvPreference;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class StoryAnswerSetCsvWriter extends AbstractCsvWriter {
    public static final int ANSWERS_START_INDEX = 9;

    private final ColumnsRenderer<StoryCsv> storyColumnsRenderer;

    public StoryAnswerSetCsvWriter(
            Writer writer,
            CsvPreference preference,
            ColumnsRenderer<StoryCsv> storyColumnsRenderer) {
        super(writer, preference);

        this.storyColumnsRenderer = storyColumnsRenderer;
    }

    public void write(StoryCsv story) throws IOException {
        write(story, new ArrayList<String>(), new HashMap<BlockType, String>());
    }

    public void write(
            StoryCsv story,
            List<String> questionHeaders,
            Map<BlockType, String> blockLabels) throws IOException {
        super.incrementRowAndLineNo();

        List<String> values = storyColumnsRenderer.render(story);

        AnswerSet answerSet = story.getAnswerSet();
        List<Answer> answers = answerSet == null ? Lists.<Answer>newArrayList() : answerSet.getAnswers();
        Map<String, Answer> answerMap = Maps.newHashMap();
        for (Answer answer : answers) {
            answerMap.put(answer.getLabel(), answer);
        }

        List<String> contactValues = Lists.newArrayList();
        contactValues.add(findBlockValue(BlockType.FIRST_NAME, blockLabels, answerMap));
        contactValues.add(findBlockValue(BlockType.LAST_NAME, blockLabels, answerMap));
        contactValues.add(findBlockValue(BlockType.STREET_ADDRESS_1, blockLabels, answerMap));
        contactValues.add(findBlockValue(BlockType.CITY, blockLabels, answerMap));
        contactValues.add(findBlockValue(BlockType.STATE, blockLabels, answerMap));
        contactValues.add(findBlockValue(BlockType.ZIP_CODE, blockLabels, answerMap));

        values.addAll(1, contactValues);

        List<String> answersValues = Lists.newArrayList();
        for (String header : questionHeaders) {
            Answer answer = answerMap.get(header);

            answersValues.add(getAnswerValue(answer));
        }

        values.addAll(ANSWERS_START_INDEX, answersValues);

        super.writeRow(values);
    }

    private String getAnswerValue(Answer answer) {
        return answer == null ? "" : Jsoup.parse(Joiner.on("\n").join(answer.getReportValues())).body().text();
    }

    private String findBlockValue(
            BlockType blockType,
            Map<BlockType, String> blockLabels,
            Map<String, Answer> answerMap) {
        String blockKey = blockLabels.get(blockType);
        if (blockKey == null) {
            return "";
        }

        String answerValue = getAnswerValue(answerMap.get(blockKey));

        answerMap.remove(blockKey);

        return answerValue;
    }
}
