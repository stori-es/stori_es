package org.consumersunion.stories.survey.client.application.survey;

import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire;
import org.consumersunion.stories.survey.client.application.ApplicationPresenter;
import org.consumersunion.stories.survey.client.gin.SurveyBootstrapper;
import org.consumersunion.stories.survey.client.place.NameTokens;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class EndSurveyPresenter extends Presenter<EndSurveyPresenter.MyView, EndSurveyPresenter.MyProxy> {
    interface MyView extends View {
        void scrollToTop();

        void setConfirmation(String confirmation);

        void setConfirmation(Document document);

        void goToUrl(String url);
    }

    @ProxyStandard
    @NameToken(NameTokens.endsurvey)
    interface MyProxy extends ProxyPlace<EndSurveyPresenter> {
    }

    private final SurveyBootstrapper bootStrapper;
    private final CommonI18nLabels labels;

    @Inject
    EndSurveyPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            SurveyBootstrapper bootStrapper,
            CommonI18nLabels labels) {
        super(eventBus, view, proxy, ApplicationPresenter.TYPE_SetMainContent);

        this.bootStrapper = bootStrapper;
        this.labels = labels;
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        Questionnaire questionnaire = (Questionnaire) bootStrapper.getCollection();

        Document confirmationDocument = questionnaire.getConfirmationDocument();
        if (confirmationDocument.isNew()) {
            getView().setConfirmation(labels.defaultConfirmation());
        } else {
            if (SystemEntityRelation.ATTACHMENT.equals(confirmationDocument.getSystemEntityRelation())) {
                getView().goToUrl(confirmationDocument.getPermalink());
            } else {
                getView().setConfirmation(confirmationDocument);
            }
        }

        getView().scrollToTop();
    }
}
