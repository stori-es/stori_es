package org.consumersunion.stories.dashboard.client.application.ui.block.configurator;

import org.consumersunion.stories.common.shared.model.questionnaire.Question;

import com.google.common.base.Strings;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class DateQuestionConfigurator extends AbstractConfigurator<Question> implements BlockConfigurator<Question> {
    interface Binder extends UiBinder<Widget, DateQuestionConfigurator> {
    }

    interface Driver extends SimpleBeanEditorDriver<Question, DateQuestionConfigurator> {
    }

    @UiField
    TextBox text;
    @UiField
    @Ignore
    Label textError;
    @UiField
    TextBox helpText;
    @UiField
    @Editor.Ignore
    DateBox startDate;
    @UiField
    SimpleCheckBox required;

    private final DateTimeFormat dateFormat;

    @Inject
    DateQuestionConfigurator(
            Binder uiBinder,
            Driver driver,
            @Assisted Question question) {
        super(driver, question);

        this.dateFormat = DateTimeFormat.getFormat("MM/dd/yyyy");

        initWidget(uiBinder.createAndBindUi(this));
        driver.initialize(this);

        if (!Strings.isNullOrEmpty(question.getStartDate())) {
            startDate.setValue(dateFormat.parse(question.getStartDate()));
        }

        setErrorLabels(textError);
        init();
    }

    @Override
    public boolean validate() {
        Question question = driver.flush();
        if (!driver.hasErrors() && !Strings.isNullOrEmpty(text.getText().trim())) {
            if (startDate.getValue() != null) {
                question.setStartDate(dateFormat.format(startDate.getValue()));
            }

            resetErrors();
            return true;
        } else {
            textError.setText(messages.requiredField());
            return false;
        }
    }

    @Override
    public boolean isNew() {
        return Strings.isNullOrEmpty(getEditedValue().getText());
    }

    @Override
    protected void onDone() {
        if (validate()) {
            Question question = driver.flush();
            if (startDate.getValue() != null) {
                question.setStartDate(dateFormat.format(startDate.getValue()));
            }

            doneCallback.onSuccess(question);

            setEditedValue(question);
        }
    }
}
