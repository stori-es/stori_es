package org.consumersunion.stories.common.client.ui.block.question;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.ui.form.InputValidationException;
import org.consumersunion.stories.common.client.ui.form.Validator;
import org.consumersunion.stories.common.client.ui.form.validators.MaxLengthValidator;
import org.consumersunion.stories.common.client.ui.form.validators.MinLengthValidator;
import org.consumersunion.stories.common.client.ui.form.validators.RequiredValidator;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;

import com.google.common.base.Strings;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class QuestionWidget extends Composite {
    interface Binder extends UiBinder<Widget, QuestionWidget> {
    }

    @UiField
    protected HTMLPanel questionPanel;
    @UiField
    protected InlineLabel labelText;
    @UiField
    InlineLabel helpText;
    @UiField
    Label error;

    protected List<Validator> validators;

    QuestionWidget(Binder uiBinder,
            Question question,
            String style) {
        this(uiBinder, question, false, style);
    }

    QuestionWidget(Binder uiBinder,
            Question question,
            boolean validateLength,
            String style) {
        initWidget(uiBinder.createAndBindUi(this));
        initQuestion(question, validateLength, style);
    }

    public void clearError() {
        error.setText("");
    }

    protected Boolean validate(String value) {
        clearError();
        for (Validator validator : validators) {
            try {
                validator.validate(value);
            } catch (InputValidationException ex) {
                error.setText(ex.getMessage());

                return false;
            }
        }

        return true;
    }

    protected void initQuestion(Question question, String style) {
        initQuestion(question, false, style);
    }

    protected void initQuestion(Question question, boolean validateLength, String style) {
        initQuestion(question.getText(), question.getHelpText(), question.isRequired(), style);

        if (validateLength) {
            if (question.getMinLength() != null) {
                validators.add(new MinLengthValidator(question.getMinLength()));
            }

            if (question.getMaxLength() != null) {
                validators.add(new MaxLengthValidator(question.getMaxLength()));
            }
        }
    }

    private void initQuestion(String label, String help, Boolean isRequired, String style) {
        questionPanel.setStyleName(style);
        labelText.setText(isRequired ? label + "*" : label);

        if (!Strings.isNullOrEmpty(help)) {
            helpText.getElement().setAttribute("data-tooltip", help);
            helpText.setVisible(true);
        } else {
            helpText.setVisible(false);
        }

        validators = new ArrayList<Validator>();
        if (isRequired) {
            validators.add(new RequiredValidator());
        }
    }
}
