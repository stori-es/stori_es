package org.consumersunion.stories.common.client.ui.block.question;

import java.util.Collections;
import java.util.List;

import org.consumersunion.stories.common.client.ui.block.QuestionElement;
import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;

import com.google.common.base.Strings;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class TextQuestion extends QuestionWidget implements QuestionElement<Question> {
    private static final String STYLE = "stories-textBox";

    private final TextBox textBox;

    private Question question;

    @Inject
    TextQuestion(
            Binder uiBinder,
            @Assisted Question question,
            @Assisted Boolean withPlaceHolder) {
        super(uiBinder, question, true, STYLE);

        this.question = question;
        this.textBox = new TextBox();
        textBox.setMaxLength(255);

        questionPanel.add(textBox);

        if (withPlaceHolder && !Strings.isNullOrEmpty(question.getHelpText())) {
            textBox.getElement().setAttribute("placeholder", question.getHelpText());
        }
    }

    @Override
    public void display(Question question) {
        this.question = question;

        initQuestion(question, true, STYLE);
    }

    @Override
    public Answer get() {
        if (validate(textBox.getText())) {
            clearError();

            Answer answer = new Answer();
            answer.setLabel(question.getLabel());
            answer.setDisplayValue(question.getLabel());
            answer.setReportValues(Collections.singletonList(textBox.getText()));
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
        textBox.setText(reportedValues.get(0));
        textBox.setEnabled(enable);
    }
}
