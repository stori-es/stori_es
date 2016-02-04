package org.consumersunion.stories.questionnaire.client.application.questionnaire;

import org.consumersunion.stories.common.client.ui.questionnaire.QuestionnaireRenderer;
import org.consumersunion.stories.common.client.util.URLUtils;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.questionnaire.client.place.NameTokens;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class EndQuestionnaireView extends ViewImpl implements EndQuestionnairePresenter.MyView {
    interface Binder extends UiBinder<Widget, EndQuestionnaireView> {
    }

    @UiField
    SimplePanel confirmation;

    private final PlaceManager placeManager;
    private final QuestionnaireRenderer questionnaireRenderer;

    @Inject
    EndQuestionnaireView(
            Binder uiBinder,
            PlaceManager placeManager,
            QuestionnaireRenderer questionnaireRenderer) {
        this.placeManager = placeManager;
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

    @UiHandler("back")
    void onReturnClicked(ClickEvent event) {
        PlaceRequest place = new PlaceRequest.Builder().nameToken(NameTokens.getQuestionnaire()).build();
        placeManager.revealPlace(place);
    }
}
