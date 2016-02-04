package org.consumersunion.stories.questionnaire.client.application.questionnaire;

import org.consumersunion.stories.common.client.ui.block.SubmitBlockWidget;
import org.consumersunion.stories.common.client.ui.questionnaire.QuestionnaireNotValidException;
import org.consumersunion.stories.common.client.ui.questionnaire.QuestionnaireRenderer;
import org.consumersunion.stories.common.shared.model.Collection;

import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public class QuestionnaireView extends ViewWithUiHandlers<QuestionnaireUiHandlers>
        implements QuestionnairePresenter.MyView {
    interface Binder extends UiBinder<Widget, QuestionnaireView> {
    }

    @UiField(provided = true)
    final QuestionnaireRenderer questionnaireRenderer;

    @UiField
    Label title;
    @UiField
    Label error;

    private ButtonElement save;

    @Inject
    QuestionnaireView(
            Binder uiBinder,
            QuestionnaireRenderer questionnaireRenderer) {
        this.questionnaireRenderer = questionnaireRenderer;

        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setData(Collection collection) {
        error.setVisible(false);
        questionnaireRenderer.init(collection);

        save = $("#" + SubmitBlockWidget.BUTTON_ID).elements()[0].cast();
        $(save).click(new Function() {
            @Override
            public void f() {
                onSaveClicked();
            }
        });

        title.setText(collection.getTitle());
        setSubmitEnabled(true);
    }

    @Override
    public void setSubmitEnabled(boolean enabled) {
        save.setDisabled(!enabled);
    }

    @Override
    public void hideSubmit() {
        save.removeFromParent();
    }

    private void onSaveClicked() {
        try {
            getUiHandlers().save(questionnaireRenderer.getAnswers());
            error.setVisible(false);
            setSubmitEnabled(false);
        } catch (QuestionnaireNotValidException ignored) {
            error.setVisible(true);
            setSubmitEnabled(true);
        }
    }
}
