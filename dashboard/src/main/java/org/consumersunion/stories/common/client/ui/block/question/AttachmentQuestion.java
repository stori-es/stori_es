package org.consumersunion.stories.common.client.ui.block.question;

import java.util.List;

import org.consumersunion.stories.common.client.ui.block.QuestionElement;
import org.consumersunion.stories.common.client.ui.questionnaire.ui.AttachmentInput;
import org.consumersunion.stories.common.client.ui.questionnaire.ui.AttachmentInputFactory;
import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class AttachmentQuestion extends QuestionWidget implements QuestionElement<Question> {
    private static final String STYLE = "stories-attachments";

    private final AttachmentInput attachmentInput;

    private Question question;

    @Inject
    AttachmentQuestion(Binder uiBinder,
            AttachmentInputFactory attachmentInputFactory,
            @Assisted Question question) {
        super(uiBinder, question, STYLE);

        this.question = question;
        this.attachmentInput = attachmentInputFactory.createAttachmentInput(question.getMaxLength());

        questionPanel.add(attachmentInput);
    }

    @Override
    public void display(Question question) {
        this.question = question;

        attachmentInput.clear();
        initQuestion(question, STYLE);
    }

    @Override
    public Answer get() {
        List<String> selectedValues = attachmentInput.getValues();

        if (selectedValues.isEmpty() && question.isRequired()) {
            validate(null);
            return null;
        }

        for (String value : selectedValues) {
            if (!validate(value)) {
                return null;
            }
        }

        Answer answer = new Answer();
        answer.setLabel(question.getLabel());
        answer.setDisplayValue(question.getLabel());
        answer.setReportValues(selectedValues);
        return answer;
    }

    @Override
    public String getKey() {
        return question.getLabel();
    }

    @Override
    public void setAnswer(List<String> reportedValues, Boolean enable) {
        attachmentInput.setValues(reportedValues);
        attachmentInput.setEnabled(enable);
    }
}
