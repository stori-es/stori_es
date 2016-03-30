package org.consumersunion.stories.dashboard.client.application.questionnaire.widget;

import org.consumersunion.stories.common.client.service.RpcQuestionnaireServiceAsync;
import org.consumersunion.stories.common.client.service.RpcStoryServiceAsync;
import org.consumersunion.stories.common.client.service.response.AnswerSetAndQuestionnaireResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class AnswerSetPresenter extends PresenterWidget<AnswerSetPresenter.MyView> {
    interface MyView extends View {
        void displayData(Questionnaire questionnaire, AnswerSet answerSet);

        void setStoryContent(String storyContent);
    }

    private final RpcQuestionnaireServiceAsync questionnaireService;
    private final RpcStoryServiceAsync storyService;

    @Inject
    AnswerSetPresenter(
            EventBus eventBus,
            MyView view,
            RpcQuestionnaireServiceAsync questionnaireService,
            RpcStoryServiceAsync storyService) {
        super(eventBus, view);

        this.questionnaireService = questionnaireService;
        this.storyService = storyService;
    }

    public void initPresenter(AnswerSet answerSet) {
        if (answerSet != null) {
            setAnswerSet(answerSet.getId());
        }
    }

    private void setAnswerSet(Integer answerSetId) {
        questionnaireService.getAnswerSetAndQuestionnaire(answerSetId,
                new ResponseHandlerLoader<AnswerSetAndQuestionnaireResponse>() {
                    @Override
                    public void handleSuccess(AnswerSetAndQuestionnaireResponse result) {
                        if (result.getGlobalErrorMessages().isEmpty()) {
                            setOriginalStoryContent(result.getAnswerSet().getSystemEntity());
                            getView().displayData(result.getQuestionnaire(), result.getAnswerSet());
                        }
                    }
                });
    }

    private void setOriginalStoryContent(Integer storyId) {
        storyService.getOriginalBodyDocument(storyId, new ResponseHandlerLoader<DatumResponse<Document>>() {
            @Override
            public void handleSuccess(DatumResponse<Document> result) {
                Document datum = result.getDatum();
                getView().setStoryContent(Strings.nullToEmpty(datum == null ? null : datum.getFirstContent()));
            }
        });
    }
}
