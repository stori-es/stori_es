package org.consumersunion.stories.survey.client.application.survey;

import org.consumersunion.stories.common.client.ui.questionnaire.QuestionnaireRenderer;
import org.consumersunion.stories.common.client.util.URLUtils;
import org.consumersunion.stories.common.shared.model.document.Document;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class EndSurveyView extends ViewImpl implements EndSurveyPresenter.MyView {
    interface Binder extends UiBinder<Widget, EndSurveyView> {
    }

    @UiField
    SimplePanel confirmation;

    private final QuestionnaireRenderer questionnaireRenderer;

    @Inject
    EndSurveyView(
            Binder uiBinder,
            QuestionnaireRenderer questionnaireRenderer) {
        this.questionnaireRenderer = questionnaireRenderer;

        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setConfirmation(String confirmationMessage) {
        confirmation.setWidget(new HTML(confirmationMessage));
    }

    @Override
    public void setConfirmation(Document confirmationDocument) {
        questionnaireRenderer.init(confirmationDocument);
        confirmation.setWidget(questionnaireRenderer);
    }

    @Override
    public void goToUrl(String url) {
        Window.Location.replace(URLUtils.appendDefaultProtocol(url));
    }

    @Override
    public void scrollToTop() {
        Window.scrollTo(0, 0);
    }
}
