package org.consumersunion.stories.common.client.ui.block.question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.consumersunion.stories.common.client.ui.block.QuestionElement;
import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.consumersunion.stories.common.shared.model.questionnaire.Option;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;

import com.google.common.base.Strings;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class RadioQuestion extends QuestionWidget implements QuestionElement<Question> {
    private static final String STYLE = "stories-radio";

    private List<RadioButton> radioButtons;
    private Question question;

    @Inject
    RadioQuestion(Binder uiBinder,
            @Assisted Question question) {
        super(uiBinder, question, STYLE);

        this.question = question;
        this.radioButtons = new ArrayList<RadioButton>();

        initRadioQuestion();
    }

    @Override
    public void display(Question question) {
        this.question = question;
        this.radioButtons = new ArrayList<RadioButton>();

        initQuestion(question, STYLE);
        initRadioQuestion();
    }

    @Override
    public Answer get() {
        String selectedValue = getValue();
        if (validate(selectedValue)) {
            clearError();

            Answer answer = new Answer();
            answer.setLabel(question.getLabel());
            answer.setDisplayValue(question.getLabel());
            answer.setReportValues(Collections.singletonList(selectedValue));
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
        String value = reportedValues.isEmpty() ? "" : reportedValues.get(0);
        if (!Strings.isNullOrEmpty(value)) {
            for (RadioButton radioButton : radioButtons) {
                radioButton.setEnabled(enable);

                if (radioButton.getFormValue().equals(value)) {
                    radioButton.setValue(true);
                }
            }
        }
    }

    private String getValue() {
        for (RadioButton radio : radioButtons) {
            if (radio.getValue()) {
                return radio.getFormValue();
            }
        }

        return "";
    }

    private void initRadioQuestion() {
        questionPanel.clear();

        for (Option option : question.getOptions()) {
            RadioButton radio = new RadioButton(question.getLabel(), option.getDisplayValue());
            radio.setFormValue(option.getReportValue());

            questionPanel.add(radio);
            radioButtons.add(radio);
        }
    }
}
