package org.consumersunion.stories.common.client.ui.block.question;

import java.util.Collections;
import java.util.List;

import org.consumersunion.stories.common.client.ui.block.QuestionElement;
import org.consumersunion.stories.common.client.ui.form.validators.EmailValidator;
import org.consumersunion.stories.common.client.ui.form.validators.PhoneNumberValidator;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.consumersunion.stories.common.shared.model.questionnaire.ContactBlock;

import com.google.common.base.Strings;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class ContactQuestion extends QuestionWidget implements QuestionElement<ContactBlock> {
    private static final String STYLE = "stories-contact";

    private final TextBox textBox;

    private ContactBlock contactBlock;

    @Inject
    ContactQuestion(Binder uiBinder,
            @Assisted ContactBlock contactBlock) {
        super(uiBinder, contactBlock, STYLE);

        this.contactBlock = contactBlock;
        this.textBox = new TextBox();

        questionPanel.add(textBox);

        if (!Strings.isNullOrEmpty(contactBlock.getHelpText())) {
            textBox.getElement().setAttribute("placeholder", contactBlock.getHelpText());
        }

        if (contactBlock.getBlockType().isEmail()) {
            validators.add(new EmailValidator());
        } else if (contactBlock.getBlockType().isPhone()) {
            validators.add(new PhoneNumberValidator());
        }
    }

    @Override
    public void display(ContactBlock contactBlock) {
        this.contactBlock = contactBlock;

        initQuestion(contactBlock, STYLE);
    }

    @Override
    public Answer get() {
        if (validate(textBox.getText())) {
            clearError();

            Answer answer = new Answer();
            answer.setLabel(contactBlock.getLabel());
            answer.setDisplayValue(contactBlock.getLabel());
            answer.setReportValues(Collections.singletonList(textBox.getText()));
            return answer;
        }

        return null;
    }

    @Override
    public String getKey() {
        return contactBlock.getLabel();
    }

    @Override
    public void setAnswer(List<String> reportedValues, Boolean enable) {
        textBox.setText(reportedValues.get(0));
        textBox.setEnabled(enable);
    }
}
