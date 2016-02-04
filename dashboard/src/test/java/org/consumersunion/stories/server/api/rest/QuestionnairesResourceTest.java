package org.consumersunion.stories.server.api.rest;

import java.util.HashSet;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.consumersunion.stories.common.shared.dto.QuestionnairesApiResponse;
import org.consumersunion.stories.common.shared.dto.post.QuestionnairePost;
import org.consumersunion.stories.common.shared.dto.post.QuestionnairePut;
import org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.server.api.rest.converters.QuestionnaireConverter;
import org.consumersunion.stories.server.api.rest.merger.QuestionnairePutMerger;
import org.consumersunion.stories.server.api.rest.util.ResourceLinksHelper;
import org.consumersunion.stories.server.business_logic.QuestionnaireService;
import org.consumersunion.stories.server.business_logic.TagsService;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class QuestionnairesResourceTest {
    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(QuestionnaireConverter.class);
            forceMock(QuestionnairePutMerger.class);
        }
    }

    private static final int QUESTIONNAIRE_ID = 3;

    @Inject
    private QuestionnairesResource questionnairesResource;
    @Inject
    private TagsService tagsService;
    @Inject
    private QuestionnaireService questionnaireService;
    @Inject
    private QuestionnaireConverter questionnaireConverter;
    @Inject
    private QuestionnairePutMerger questionnairePutMerger;
    @Inject
    private ResourceLinksHelper resourceLinksHelper;

    @Test
    public void getQuestionnaire() throws Exception {
        Questionnaire questionnaire = new Questionnaire(QUESTIONNAIRE_ID, 1);
        given(questionnaireService.getQuestionnaire(QUESTIONNAIRE_ID)).willReturn(questionnaire);

        Response response = questionnairesResource.getQuestionnaire(QUESTIONNAIRE_ID);

        QuestionnairesApiResponse apiResponse = (QuestionnairesApiResponse) response.getEntity();
        assertThat(apiResponse.getData().get(0).getId()).isEqualTo(QUESTIONNAIRE_ID);
    }

    @Test
    public void updateQuestionnaire() throws Exception {
        QuestionnaireI15d questionnaire = new QuestionnaireI15d(QUESTIONNAIRE_ID, 1);
        given(questionnaireService.getQuestionnaireI15d(QUESTIONNAIRE_ID)).willReturn(questionnaire);
        QuestionnairePut questionnairePut = new QuestionnairePut();
        questionnairePut.setTags(new HashSet<String>());
        questionnairePut.setAutoTags(new HashSet<String>());
        QuestionnaireI15d updatedQuestionnaire = new QuestionnaireI15d(QUESTIONNAIRE_ID, 1);
        given(questionnaireService.updateQuestionnaire(same(questionnaire))).willReturn(updatedQuestionnaire);

        Response response = questionnairesResource.updateQuestionnaire(QUESTIONNAIRE_ID, questionnairePut);

        InOrder inOrder = Mockito.inOrder(questionnairePutMerger, questionnaireService, tagsService);
        inOrder.verify(questionnairePutMerger).merge(same(questionnaire), same(questionnairePut));
        inOrder.verify(questionnaireService).updateQuestionnaire(same(questionnaire));
        inOrder.verify(tagsService).setTags(same(updatedQuestionnaire), same(questionnairePut.getTags()));
        inOrder.verify(tagsService).setAutoTags(same(updatedQuestionnaire), same(questionnairePut.getAutoTags()));
        QuestionnairesApiResponse apiResponse = (QuestionnairesApiResponse) response.getEntity();
        assertThat(apiResponse.getData().get(0).getId()).isEqualTo(QUESTIONNAIRE_ID);
    }

    @Test
    public void deleteQuestionnaire() throws Exception {
        questionnairesResource.deleteQuestionnaire(QUESTIONNAIRE_ID);

        verify(questionnaireService).deleteQuestionnaire(QUESTIONNAIRE_ID);
    }

    @Test
    public void createQuestionnaire() throws Exception {
        QuestionnairePost questionnairePost = QuestionnairePost.builder()
                .withTags(new HashSet<String>())
                .withAutoTags(new HashSet<String>())
                .build();
        QuestionnaireI15d questionnaire = new QuestionnaireI15d();
        given(questionnaireConverter.convert(same(questionnairePost))).willReturn(questionnaire);
        QuestionnaireI15d savedQuestionnaire = new QuestionnaireI15d(QUESTIONNAIRE_ID, 1);
        given(questionnaireService.createQuestionnaire(same(questionnaire))).willReturn(savedQuestionnaire);

        Response response = questionnairesResource.createQuestionnaire(questionnairePost);

        QuestionnairesApiResponse apiResponse = (QuestionnairesApiResponse) response.getEntity();
        assertThat(apiResponse.getData().get(0).getId()).isEqualTo(QUESTIONNAIRE_ID);
        verify(tagsService).setTags(same(savedQuestionnaire), same(questionnairePost.getTags()));
        verify(tagsService).setAutoTags(same(savedQuestionnaire), same(questionnairePost.getAutoTags()));
    }
}
