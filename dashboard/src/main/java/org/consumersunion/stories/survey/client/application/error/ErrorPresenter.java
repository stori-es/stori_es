package org.consumersunion.stories.survey.client.application.error;

import org.consumersunion.stories.common.client.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.survey.client.application.ApplicationPresenter;
import org.consumersunion.stories.survey.client.place.NameTokens;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class ErrorPresenter extends Presenter<ErrorPresenter.MyView, ErrorPresenter.MyProxy> {
    interface MyView extends View {
        void displayError(String message);

        void displayError(SafeHtml safeHtml);
    }

    @ProxyStandard
    @NameToken(NameTokens.error)
    public interface MyProxy extends ProxyPlace<ErrorPresenter> {
    }

    private final CommonI18nErrorMessages commonI18nErrorMessages;

    @Inject
    ErrorPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            CommonI18nErrorMessages commonI18nErrorMessages) {
        super(eventBus, view, proxy, ApplicationPresenter.TYPE_SetMainContent);

        this.commonI18nErrorMessages = commonI18nErrorMessages;
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        String errorTypeValue = request.getParameter(ParameterTokens.errortype, null);
        ErrorType errorType = ErrorType.valueOf(errorTypeValue);

        switch (errorType) {
            case NO_SURVEY_ATTR:
                getView().displayError(commonI18nErrorMessages.noSurveyAttribute());
                break;
            case QUESTIONNAIRE_NOT_FOUND:
            case COLLECTION_NOT_FOUND:
                getView().displayError(commonI18nErrorMessages.contentNotPublished());
                break;
        }
    }
}
