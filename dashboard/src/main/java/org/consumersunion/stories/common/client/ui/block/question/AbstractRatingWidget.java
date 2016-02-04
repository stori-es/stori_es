package org.consumersunion.stories.common.client.ui.block.question;

import java.math.BigDecimal;

import org.consumersunion.stories.common.shared.model.questionnaire.RatingQuestion;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractRatingWidget implements IsWidget, LeafValueEditor<BigDecimal> {
    protected Widget widget;
    protected RatingQuestion question;
    protected BigDecimal value;

    protected AbstractRatingWidget(RatingQuestion question) {
        this.question = question;
    }

    @Override
    public void setValue(BigDecimal value) {
        this.value = value.setScale(1, BigDecimal.ROUND_FLOOR);
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    public void setQuestion(RatingQuestion question) {
        this.question = question;
    }

    public abstract void setEnabled(boolean enable);
}
