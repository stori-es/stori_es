package org.consumersunion.stories.server.api.rest.merger;

import org.consumersunion.stories.common.shared.dto.post.QuestionnairePut;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class QuestionnairePutMergerTest {
    private static final String A_NEW_TITLE = "A_NEW_TITLE";
    private static final String A_NEW_SUMMARY = "A_NEW_SUMMARY";

    private QuestionnairePutMerger merger;

    @Before
    public void setUp() {
        merger = new QuestionnairePutMerger();
    }

    @Test
    public void merge_mergesData() throws Exception {
        QuestionnaireI15d questionnaire = new QuestionnaireI15d();
        questionnaire.setPublished(false);
        QuestionnairePut dto = QuestionnairePut.builder()
                .withPublished(true)
                .withTitle(A_NEW_TITLE)
                .withSummary(A_NEW_SUMMARY)
                .build();

        merger.merge(questionnaire, dto);

        assertThat(questionnaire.isPublished()).isTrue();
        assertThat(questionnaire.getBodyDocument().getTitle()).isEqualTo(A_NEW_TITLE);
        assertThat(questionnaire.getBodyDocument().getSummary()).isEqualTo(A_NEW_SUMMARY);
    }

    @Test
    public void merge_dataNotProvided_doesNotUpdate() throws Exception {
        QuestionnaireI15d questionnaire = new QuestionnaireI15d();
        questionnaire.setPublished(false);
        questionnaire = spy(questionnaire);
        QuestionnairePut dto = QuestionnairePut.builder().build();

        merger.merge(questionnaire, dto);

        verify(questionnaire, never()).setPublished(anyBoolean());
        verify(questionnaire, never()).getBodyDocument();
    }
}
