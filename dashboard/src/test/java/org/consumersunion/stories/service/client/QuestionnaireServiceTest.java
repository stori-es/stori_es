package org.consumersunion.stories.service.client;

import java.util.LinkedList;
import java.util.List;

import org.consumersunion.stories.common.client.service.RpcQuestionnaireService;
import org.consumersunion.stories.common.client.service.RpcQuestionnaireServiceAsync;
import org.consumersunion.stories.common.client.service.response.CollectionSurveyI15dResponse;
import org.consumersunion.stories.common.client.service.response.QuestionnaireSurveyResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

public class QuestionnaireServiceTest extends GWTTestCase {
    @Override
    public String getModuleName() {
        return "org.consumersunion.stories.storiesJUnit";
    }

    ;

    // share2 has null theme, which should result in the default theme of
    // 'questionnaire.jsp'
    public void testDefaultTheme() {

        final RpcQuestionnaireServiceAsync questionnaireService = GWT.create(RpcQuestionnaireService.class);

        questionnaireService.getQuestionnaireSurvey("share2", new ResponseHandler<QuestionnaireSurveyResponse>() {
            @Override
            public void onFailure(final Throwable t) {
                super.onFailure(t);
                fail("Get Logged In User failed with message: " + t.getMessage());
                finishTest();
            }

            @Override
            public void handleSuccess(final QuestionnaireSurveyResponse result) {
                assertNotNull("No questionnaire found.", result.getQuestionnaire());
                assertEquals("Unexpected theme page.", "sys.jsp", result.getQuestionnaire().getTheme());
                finishTest();
            }
        });

        delayTestFinish(10013);
    }

    public void testCreateNewQuestionnaire() {
        final RpcQuestionnaireServiceAsync questionnaireService = GWT.create(RpcQuestionnaireService.class);

        final QuestionnaireI15d questionnaire = new QuestionnaireI15d();
        questionnaire.getBodyDocument().setTitle("Test questionnaire");
        final List<Block> elements = new LinkedList<Block>();

        final Collection collection = new Collection(4, 1);

        final Question question = new Question();
        question.setText("Favorite color?");
        question.setLabel("favoriteColor");
        question.setFormType(BlockType.TEXT_INPUT);
        question.setMinLength(3);
        question.setMaxLength(16);
        question.setRequired(true);
        elements.add(question);
        questionnaire.getSurvey().setBlocks(elements);

        questionnaireService.saveQuestionnaire(collection, questionnaire,
                new ResponseHandler<CollectionSurveyI15dResponse>() {
                    @Override
                    public void onFailure(final Throwable t) {
                        super.onFailure(t);
                        fail("CreateNewCollectionSurvey failed with message: " + t.getMessage());
                        finishTest();
                    }

                    @Override
                    public void handleSuccess(final CollectionSurveyI15dResponse result) {
                        assertNotNull("Collection not null", result.getCollection());
                        assertNotNull("Questionnaire not null", result.getQuestionnaire());
                        assertSame("Collection should no be updated", collection.getVersion(), result.getCollection()
                                .getVersion());
                        assertNotSame("Questionnaire save", -1, result.getQuestionnaire().getId());
                        assertFalse(result.getQuestionnaire().isPublished());
                        finishTest();
                    }
                });

        delayTestFinish(10014);
    }

    public void testUpdateExistingQuestionnaire() {
        final RpcQuestionnaireServiceAsync questionnaireService = GWT.create(RpcQuestionnaireService.class);

        final QuestionnaireI15d questionnaire = new QuestionnaireI15d();
        questionnaire.getBodyDocument().setTitle("Title");
        final List<Block> elemets = new LinkedList<Block>();

        final Collection collection = new Collection(4, 1);

        final Question question = new Question();
        question.setLabel("favoriteColor");
        question.setText("foo");
        question.setHelpText("FOO");
        question.setFormType(BlockType.TEXT_INPUT);
        question.setMinLength(3);
        question.setMaxLength(16);
        question.setRequired(true);
        elemets.add(question);
        questionnaire.getSurvey().setBlocks(elemets);

        questionnaireService.saveQuestionnaire(collection, questionnaire,
                new ResponseHandler<CollectionSurveyI15dResponse>() {
                    @Override
                    public void onFailure(final Throwable caught) {
                        fail("testUpdateExistingCollectionSurvey failed with message: " + caught.getMessage());
                        finishTest();
                    }

                    @Override
                    public void handleSuccess(final CollectionSurveyI15dResponse result) {
                        assertNotNull("Collection not null", result.getCollection());
                        assertNotNull("Questionnaire not null", result.getQuestionnaire());
                        assertNotSame("Collection save", -1, result.getCollection().getId());
                        assertNotSame("Questionnaire save", -1, result.getQuestionnaire().getId());
                        assertFalse(result.getQuestionnaire().isPublished());
                        result.getCollection().getBodyDocument().setTitle("Collection Title Modified");
                        question.setLabel("favoriteColor Modified");
                        elemets.clear();
                        elemets.add(question);
                        result.getQuestionnaire().getSurvey().setBlocks(elemets);
                        questionnaire.setPublished(true);

                        questionnaireService.saveQuestionnaire(result.getCollection(), result.getQuestionnaire(),
                                new ResponseHandler<CollectionSurveyI15dResponse>() {
                                    @Override
                                    public void onFailure(final Throwable t) {
                                        super.onFailure(t);
                                        t.printStackTrace();
                                        fail("testUpdateExistingCollectionSurvey failed with message: "
                                                + t.getMessage());
                                        finishTest();
                                    }

                                    @Override
                                    public void handleSuccess(final CollectionSurveyI15dResponse result) {
                                        assertNotSame("Collection value not the save", "Collection Title", result
                                                .getCollection().getTitle());
                                        assertNotSame("Question value not the save", "favoriteColor", result
                                                .getQuestionnaire().getQuestions().get(0).getLabel());
                                        assertTrue(result.getQuestionnaire().isPublished());
                                        finishTest();
                                    }
                                });
                    }
                });
        delayTestFinish(10015);
    }
}
