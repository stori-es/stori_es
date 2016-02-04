package org.consumersunion.stories.service.client;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.service.RpcCollectionService;
import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.QuestionnaireSurveyResponse;
import org.consumersunion.stories.common.shared.model.Locale;
import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CollectionServiceGwtTest2 extends GWTTestCaseExposed {
    @Override
    public String getModuleName() {
        return "org.consumersunion.stories.storiesJUnit";
    }

    public void testSaveStory() {
        final AnswerSet answerSet = new AnswerSet();
        answerSet.setQuestionnaire(33);
        answerSet.setLocale(Locale.ENGLISH);

        final List<String> values1 = new ArrayList<String>();
        values1.add("testReportValue");
        answerSet.addAnswer(new Answer("starWars", "testDisplayValue", values1));
        final List<String> values2 = new ArrayList<String>();
        values2.add("testReportValue 1");
        answerSet.addAnswer(new Answer("more", "New answer", values2));

        final RpcCollectionServiceAsync collectionService = GWT.create(RpcCollectionService.class);

        // TODO: The logout is a bit of a workaround. The problem is that the thread should not be logged in, as each
        // sequence of nested calls should get it's own session. However, with the update to GWT 2.7.0, this stopped
        // working and we had to set the 'SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_GLOBAL);'.
        // That in turn caused logins from other tests to persist, so we have to explicitly logout or we end up with
        // the logged in user found (from the SecurityContext), but the 'effective subject', which relies on the session
        // and under normal operations is therefore also available when a user is logged in, to be null. Logging out
        // clears the global session and aligns everything. The original test was intended to be with a non-logged in
        // user.
        userService.logout(new AsyncCallback<ActionResponse>() {
            @Override
            public void onFailure(final Throwable t) {
                fail("collectionService.saveStory failed with message: " + t.getMessage());
                finishTest();
            }

            @Override
            public void onSuccess(final ActionResponse result) {
                collectionService.saveAnswersAndStory(answerSet, new AsyncCallback<QuestionnaireSurveyResponse>() {
                    @Override
                    public void onFailure(final Throwable t) {
                        fail("collectionService.saveStory failed with message: " + t.getMessage());
                        finishTest();
                    }

                    @Override
                    public void onSuccess(final QuestionnaireSurveyResponse result) {
                        assertNotNull(result);
                        if (result.isError()) { // Want to print error; nice to do in one line.
                            fail("Unexpected error: " + result.getGlobalErrorMessages().get(0));
                        }
                        assertNotNull(result.getStoryId());
                        finishTest();
                    }
                });
            }
        });

        delayTestFinish(10001);
    }

    public void testSaveStoryMeaningPlain() {
        final AnswerSet answerSet = new AnswerSet();
        answerSet.setQuestionnaire(51);
        answerSet.setLocale(Locale.ENGLISH);

        final List<String> values1 = new ArrayList<String>();
        values1.add("testReportValue");
        answerSet.addAnswer(new Answer("Gender2", "testDisplayValue", values1));
        final List<String> values2 = new ArrayList<String>();
        values2.add("testReportValue 1");
        answerSet.addAnswer(new Answer("Gender3", "New answer", values2));

        final RpcCollectionServiceAsync collectionService = GWT.create(RpcCollectionService.class);
        collectionService.saveAnswersAndStory(answerSet, new AsyncCallback<QuestionnaireSurveyResponse>() {

            @Override
            public void onFailure(final Throwable t) {
                t.printStackTrace();
                // fail("collectionService.saveStory failed with message: " +
                // t.getMessage());
                finishTest();
            }

            @Override
            public void onSuccess(final QuestionnaireSurveyResponse result) {
                assertNotNull(result);
                // assertNotNull(result.getStoryId());
                finishTest();
            }
        });

        delayTestFinish(10002);
    }
}
