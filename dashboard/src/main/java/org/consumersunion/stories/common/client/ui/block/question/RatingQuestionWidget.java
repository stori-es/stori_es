package org.consumersunion.stories.common.client.ui.block.question;

import java.math.BigDecimal;
import java.util.List;

import org.consumersunion.stories.common.client.ui.block.QuestionElement;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.consumersunion.stories.common.shared.model.questionnaire.RatingQuestion;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class RatingQuestionWidget extends QuestionWidget implements QuestionElement<RatingQuestion> {
    private AbstractRatingWidget ratingWidget;
    private RatingQuestion question;

    @Inject
    RatingQuestionWidget(
            Binder uiBinder,
            @Assisted RatingQuestion question) {
        super(uiBinder, question, "");

        this.question = question;

        questionPanel.add(new Label());
        createAndAddRatingWidget(question);
    }

    @Override
    public void display(RatingQuestion question) {
        if (!this.question.getFormType().equals(question.getFormType())) {
            ratingWidget.asWidget().removeFromParent();
            createAndAddRatingWidget(question);
        } else {
            this.question = question;
            ratingWidget.setQuestion(question);
        }
        initQuestion(question, "");
    }

    @Override
    public Answer get() {
        BigDecimal ratingValue = ratingWidget.getValue();
        String value = null;
        if (ratingValue != null) {
            value = String.valueOf(ratingValue);
        }

        if (validate(value)) {
            clearError();

            Answer answer = new Answer();
            answer.setLabel(question.getLabel());
            answer.setDisplayValue(question.getLabel());
            answer.setReportValues(Lists.newArrayList(value));
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
            ratingWidget.setValue(new BigDecimal(reportedValues.get(0)));
        }
        ratingWidget.setEnabled(enable);
    }

    private void createAndAddRatingWidget(RatingQuestion question) {
        ratingWidget = createRatingWidget(question);
        questionPanel.add(ratingWidget);
    }

    private AbstractRatingWidget createRatingWidget(RatingQuestion question) {
        if (BlockType.STARS.equals(question.getFormType())) {
            return new StarsRatingWidget(question);
        } else {
            return new NumbersRatingWidget(question);
        }
    }
}
