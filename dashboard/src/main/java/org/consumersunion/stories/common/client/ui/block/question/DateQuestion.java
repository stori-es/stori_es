package org.consumersunion.stories.common.client.ui.block.question;

import java.util.Collections;
import java.util.List;

import org.consumersunion.stories.common.client.ui.block.QuestionElement;
import org.consumersunion.stories.common.client.ui.form.validators.DateValidator;
import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;

import com.google.common.base.Strings;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class DateQuestion extends QuestionWidget implements QuestionElement<Question> {
    private static final String STYLE = "stories-date";

    private final DateBox dateBox;
    private final DateTimeFormat dateFormat;

    private Question question;

    @Inject
    DateQuestion(
            Binder uiBinder,
            @Assisted Question question) {
        super(uiBinder, question, STYLE);

        this.dateBox = new DateBox();
        this.question = question;
        this.dateFormat = DateTimeFormat.getFormat("MM/dd/yyyy");

        questionPanel.add(dateBox);
        dateBox.setFormat(new DateBox.DefaultFormat(dateFormat));

        if (question.getStartDate() != null) {
            validators.add(new DateValidator(question.getStartDate()));
        }
    }

    @Override
    public void display(Question question) {
        this.question = question;

        initQuestion(question, STYLE);
    }

    @Override
    public Answer get() {
        String date = dateBox.getTextBox().getText();

        if (validate(date)) {
            clearError();

            Answer answer = new Answer();
            answer.setLabel(question.getLabel());
            answer.setDisplayValue(question.getLabel());
            answer.setReportValues(Collections.singletonList(date));
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
        if (!Strings.isNullOrEmpty(reportedValues.get(0))) {
            dateBox.setValue(dateFormat.parse(reportedValues.get(0)));
        }
        dateBox.setEnabled(enable);
    }
}
