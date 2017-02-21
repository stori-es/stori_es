package org.consumersunion.stories.questionnaire.client.application.questionnaire;

import org.consumersunion.stories.common.client.service.RpcQuestionnaireServiceAsync;
import org.consumersunion.stories.common.client.service.response.QuestionnaireSurveyResponse;
import org.consumersunion.stories.common.client.util.PublicResponseHandler;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.questionnaire.client.application.ApplicationPresenter;
import org.consumersunion.stories.questionnaire.client.place.NameTokens;

import com.google.gwt.i18n.client.Dictionary;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class EndQuestionnairePresenter
        extends Presenter<EndQuestionnairePresenter.MyView, EndQuestionnairePresenter.MyProxy> {
    interface MyView extends View {
        void setConfirmation(String confirmationMessage);

        void setConfirmation(Document confirmationDocument);

        void goToUrl(String url);
    }

    @ProxyStandard
    @NameToken(NameTokens.endquestionnaire)
    interface MyProxy extends ProxyPlace<EndQuestionnairePresenter> {
    }

    private final CommonI18nLabels labels;
    private final RpcQuestionnaireServiceAsync questionnaireService;

    @Inject
    EndQuestionnairePresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            CommonI18nLabels labels,
            RpcQuestionnaireServiceAsync questionnaireService) {
        super(eventBus, view, proxy, ApplicationPresenter.TYPE_SetMainContent);

        this.labels = labels;
        this.questionnaireService = questionnaireService;
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        Dictionary params = Dictionary.getDictionary("requestParameters");
        String collectionId = params.get("permalink");

        questionnaireService.getQuestionnaireSurvey(collectionId,
                new PublicResponseHandler<QuestionnaireSurveyResponse>() {
                    @Override
                    public void handleSuccess(QuestionnaireSurveyResponse result) {
                        onQuestionnaireFetched(result);
                    }
                });
    }

    private void onQuestionnaireFetched(QuestionnaireSurveyResponse result) {
        Document confirmationDocument = result.getQuestionnaire().getNextDocument();
        if (confirmationDocument.isNew()) {
            getView().setConfirmation(labels.defaultConfirmation());
        } else {
            if (SystemEntityRelation.ATTACHMENT.equals(confirmationDocument.getSystemEntityRelation())) {
                getView().goToUrl(confirmationDocument.getOnlyContent());
            } else {
                getView().setConfirmation(confirmationDocument);
            }
        }
    }
}
