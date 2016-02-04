package org.consumersunion.stories.dashboard.client.application.ui.block.configurator;

import org.consumersunion.stories.common.shared.model.questionnaire.Question;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.text.client.IntegerRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * {@link BlockConfigurator} for {@link org.consumersunion.stories.common.client.ui.block.question.AttachmentQuestion}s.
 */
public class AttachmentQuestionConfigurator extends AbstractConfigurator<Question>
        implements BlockConfigurator<Question> {
    interface Binder extends UiBinder<Widget, AttachmentQuestionConfigurator> {
    }

    interface Driver extends SimpleBeanEditorDriver<Question, AttachmentQuestionConfigurator> {
    }

    @UiField(provided = true)
    final ValueListBox<Integer> maxLength;

    @UiField
    TextBox text;
    @UiField
    @Ignore
    Label textError;
    @UiField
    TextBox helpText;
    @UiField
    SimpleCheckBox required;

    @Inject
    AttachmentQuestionConfigurator(
            Binder uiBinder,
            Driver driver,
            @Assisted Question question) {
        super(driver, question);

        this.maxLength = new ValueListBox<Integer>(IntegerRenderer.instance());

        initWidget(uiBinder.createAndBindUi(this));

        driver.initialize(this);
        setErrorLabels(textError);

        init();

        if (question.getMaxLength() == null) {
            maxLength.setValue(3);
        }

        maxLength.setAcceptableValues(Lists.newArrayList(1, 2, 3, 4, 5));
    }

    @Override
    public boolean validate() {
        if (!driver.hasErrors() && !Strings.isNullOrEmpty(text.getText().trim())) {
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
        Question question = driver.flush();
        if (validate()) {
            doneCallback.onSuccess(question);

            setEditedValue(question);
        }
    }
}
