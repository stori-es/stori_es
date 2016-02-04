package org.consumersunion.stories.common.client.ui.block.question;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.ui.block.QuestionElement;
import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.consumersunion.stories.common.shared.model.questionnaire.Option;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;

import com.google.gwt.user.client.ui.ListBox;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class SelectQuestion extends QuestionWidget implements QuestionElement<Question> {
    private final static String BLANK_VALUE = "------";
    private final static Integer SINGLE_SELECT = 1;
    private final static Integer MULTI_SELECT = 4;
    private static final String STYLE = "stories-selectBox";

    private final ListBox listBox;

    private Question question;

    @Inject
    SelectQuestion(
            Binder uiBinder,
            @Assisted Question question) {
        super(uiBinder, question, STYLE);

        this.question = question;
        this.listBox = new ListBox(question.isMultiselect());

        questionPanel.add(listBox);
        initSelectQuestion();
    }

    @Override
    public void display(Question question) {
        this.question = question;

        initQuestion(question, STYLE);
        initSelectQuestion();
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
        listBox.setEnabled(enable);
        for (Integer i = 0; i < listBox.getItemCount(); i++) {
            if (reportedValues.contains(listBox.getValue(i))) {
                listBox.setItemSelected(i, true);
            }
        }
    }

    private List<String> getValues() {
        List<String> values = new ArrayList<String>();
        Integer itemCount = listBox.getItemCount();

        for (Integer i = 0; i < itemCount; i++) {
            if (listBox.isItemSelected(i)) {
                String value = listBox.getValue(i);
                values.add(value);
            }
        }

        if (values.isEmpty()) {
            values.add("");
        }

        return values;
    }

    private void initSelectQuestion() {
        listBox.clear();
        listBox.addItem(BLANK_VALUE, "");
        listBox.setVisibleItemCount(question.isMultiselect() ? MULTI_SELECT : SINGLE_SELECT);

        for (Option option : question.getOptions()) {
            listBox.addItem(option.getDisplayValue(), option.getReportValue());
        }
    }
}
