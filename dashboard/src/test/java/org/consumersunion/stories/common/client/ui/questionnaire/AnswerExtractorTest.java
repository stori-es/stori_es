package org.consumersunion.stories.common.client.ui.questionnaire;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.ui.block.QuestionElement;
import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class AnswerExtractorTest {
    private AnswerExtractor answerExtractor;

    @Before
    public void setUp() {
        answerExtractor = new AnswerExtractor();
    }

    @Test(expected = QuestionnaireNotValidException.class)
    public void extractAnswers_emptyAnswer_isInvalid() throws Exception {
        ArrayList<QuestionElement<?>> questions = new ArrayList<QuestionElement<?>>();
        questions.add(mock(QuestionElement.class));

        answerExtractor.extractAnswers(questions);
    }

    @Test
    public void extractAnswers_returnsAnswers() throws Exception {
        ArrayList<QuestionElement<?>> questions = new ArrayList<QuestionElement<?>>();
        QuestionElement question = mock(QuestionElement.class);
        Answer answer = mock(Answer.class);
        given(question.get()).willReturn(answer);
        questions.add(question);

        List<Answer> result = answerExtractor.extractAnswers(questions);

        assertThat(result).containsExactly(answer);
    }
}
