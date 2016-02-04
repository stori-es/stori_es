package org.consumersunion.stories.common.client.ui.block.question;

import java.util.Collections;
import java.util.List;

import org.consumersunion.stories.common.client.ui.block.QuestionElement;
import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;

import com.google.gwt.user.client.ui.TextArea;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class TextAreaQuestion extends QuestionWidget implements QuestionElement<Question> {
    private static final String STYLE = "stories-textArea";

    private final TextArea textArea;

    private Question question;

    @Inject
    TextAreaQuestion(
            Binder uiBinder,
            @Assisted Question question) {
        super(uiBinder, question, true, STYLE);

        this.question = question;
        this.textArea = new TextArea();

        questionPanel.add(textArea);
    }

    @Override
    public void display(Question question) {
        this.question = question;

        initQuestion(question, true, STYLE);
    }

    @Override
    public Answer get() {
        if (validate(textArea.getText())) {
            clearError();

            Answer answer = new Answer();
            answer.setLabel(question.getLabel());
            answer.setDisplayValue(question.getLabel());
            answer.setReportValues(Collections.singletonList(textArea.getText()));
            return answer;
        }

        return null;
    }

    @Override
    public String getKey() {
        return question.getLabel();
    }

    @Override
    public void setAnswer(List<String> reportedValues, Boolean enable) {
        textArea.setText(reportedValues.get(0));
        textArea.setEnabled(enable);
    }
}
