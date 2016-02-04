package org.consumersunion.stories.common.client.ui.block.question;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.ui.block.QuestionElement;
import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.consumersunion.stories.common.shared.model.questionnaire.Option;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class CheckBoxQuestion extends QuestionWidget implements QuestionElement<Question> {
    private static final String STYLE = "stories-checkBox";

    private List<CheckBox> checkBoxList;
    private Question question;

    @Inject
    CheckBoxQuestion(Binder uiBinder,
            @Assisted Question question) {
        super(uiBinder, question, STYLE);

        this.question = question;
        this.checkBoxList = new ArrayList<CheckBox>();

        initCheckBoxQuestion();
    }

    @Override
    public void display(Question question) {
        this.question = question;
        this.checkBoxList = new ArrayList<CheckBox>();

        initQuestion(question, STYLE);
        initCheckBoxQuestion();
    }

    @Override
    public Answer get() {
        List<String> selectedValues = getValues();

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
        for (CheckBox checkBox : checkBoxList) {
            checkBox.setEnabled(enable);

            if (reportedValues.contains(checkBox.getFormValue())) {
                checkBox.setValue(true);
            }
        }
    }

    private List<String> getValues() {
        List<String> values = new ArrayList<String>();

        for (CheckBox cb : checkBoxList) {
            if (cb.getValue()) {
                values.add(cb.getFormValue());
            }
        }

        if (values.isEmpty()) {
            values.add("");
        }

        return values;
    }

    private void initCheckBoxQuestion() {
        questionPanel.clear();

        for (Option option : question.getOptions()) {
            CheckBox checkBox = new CheckBox(option.getDisplayValue());
            checkBox.setFormValue(option.getReportValue());

            questionPanel.add(checkBox);
            checkBoxList.add(checkBox);
        }
    }
}
