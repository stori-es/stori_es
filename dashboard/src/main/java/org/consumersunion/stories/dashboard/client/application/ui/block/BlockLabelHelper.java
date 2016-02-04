package org.consumersunion.stories.dashboard.client.application.ui.block;

import java.util.Collections;
import java.util.List;

import org.consumersunion.stories.common.shared.model.HasBlocks;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.common.shared.model.type.DataType;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Ordering;

public class BlockLabelHelper {
    public String getUniqueLabel(HasBlocks hasBlocks) {
        List<Integer> questionsIds = getQuestionIds(hasBlocks, new Function<Block, Integer>() {
            @Override
            public Integer apply(Block input) {
                if (input instanceof Question) {
                    String stripped = retrieveQuestionId((Question) input);
                    return getIntegerValue(stripped);
                }
                return null;
            }
        });

        return "question" + getAvailableId(questionsIds);
    }

    public int getUniqueId(HasBlocks hasBlocks, final DataType dataType) {
        List<Integer> questionsIds = getQuestionIds(hasBlocks, new Function<Block, Integer>() {
            @Override
            public Integer apply(Block input) {
                if (input instanceof Question && dataType.code().equals(((Question) input).getDataType())) {
                    String stripped = retrieveQuestionId((Question) input);
                    return getIntegerValue(stripped);
                }
                return null;
            }
        });

        return getAvailableId(questionsIds);
    }

    private List<Integer> getQuestionIds(HasBlocks hasBlocks, Function<Block, Integer> function) {
        return FluentIterable.from(hasBlocks.getBlocks())
                .transform(function)
                .filter(Predicates.notNull())
                .toSortedList(Ordering.natural());
    }

    private String retrieveQuestionId(Question input) {
        return input.getLabel().replaceAll("\\D*", "");
    }

    private Integer getIntegerValue(String number) {
        try {
            return Integer.valueOf(number);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private int getAvailableId(List<Integer> sortedQuestionsId) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            int found = Collections.binarySearch(sortedQuestionsId, i);
            if (found < 0) {
                return i;
            }
        }

        return 0;
    }
}
