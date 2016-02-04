package org.consumersunion.stories.common.client.ui.questionnaire;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.consumersunion.stories.common.client.ui.block.QuestionElement;
import org.consumersunion.stories.common.shared.model.questionnaire.Answer;

public class AnswerExtractor {
    public List<Answer> extractAnswers(Collection<QuestionElement<?>> questions) throws QuestionnaireNotValidException {
        List<Answer> answers = new ArrayList<Answer>();

        for (QuestionElement<?> question : questions) {
            Answer answer = question.get();
            if (answer == null) {
                throw new QuestionnaireNotValidException();
            } else {
                answers.add(answer);
            }
        }

        return answers;
    }
}
