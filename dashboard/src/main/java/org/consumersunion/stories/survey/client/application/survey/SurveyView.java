package org.consumersunion.stories.survey.client.application.survey;

import org.consumersunion.stories.common.client.ui.block.SubmitBlockWidget;
import org.consumersunion.stories.common.client.ui.questionnaire.QuestionnaireNotValidException;
import org.consumersunion.stories.common.client.ui.questionnaire.QuestionnaireRenderer;
import org.consumersunion.stories.common.shared.model.Collection;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public class SurveyView extends ViewWithUiHandlers<SurveyUiHandlers> implements SurveyPresenter.MyView {
    interface Binder extends UiBinder<Widget, SurveyView> {
    }

    @UiField(provided = true)
    final QuestionnaireRenderer questionnaireRenderer;

    @UiField
    Label title;
    @UiField
    Label error;

    @Inject
    SurveyView(
            Binder uiBinder,
            QuestionnaireRenderer questionnaireRenderer) {
        this.questionnaireRenderer = questionnaireRenderer;

        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setData(Collection collection, Boolean displayTitle) {
        error.setVisible(false);
        questionnaireRenderer.init(collection);
        title.setVisible(displayTitle);
        title.setText(collection.getTitle());

        GQuery $btn = getFormButton();

        wrapWithDiv($btn);

        $btn.click(new Function() {
            @Override
            public void f() {
                try {
                    getUiHandlers().save(questionnaireRenderer.getAnswers());
                    error.setVisible(false);
                } catch (QuestionnaireNotValidException ex) {
                    error.setVisible(true);
                }
            }
        });
    }

    @Override
    public void hideSubmit() {
        getFormButton().hide();
    }

    private void wrapWithDiv(GQuery $btn) {
        DivElement divElement = Document.get().createDivElement();
        divElement.addClassName("cu-collectionSubmit");

        $btn.wrap(divElement);
    }

    private GQuery getFormButton() {
        return $("#" + SubmitBlockWidget.BUTTON_ID, questionnaireRenderer);
    }
}
