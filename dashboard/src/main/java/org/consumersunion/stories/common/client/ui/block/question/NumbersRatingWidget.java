package org.consumersunion.stories.common.client.ui.block.question;

import java.math.BigDecimal;

import org.consumersunion.stories.common.client.widget.RadioButtonGroup;
import org.consumersunion.stories.common.shared.model.questionnaire.RatingQuestion;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.client.IntegerRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class NumbersRatingWidget extends AbstractRatingWidget
        implements ValueChangeHandler<Integer>, AttachEvent.Handler {
    interface Binder extends UiBinder<Widget, NumbersRatingWidget> {
    }

    private static final Binder binder = GWT.create(Binder.class);

    @UiField(provided = true)
    final RadioButtonGroup<Integer> options;

    @UiField
    InlineLabel endLabel;
    @UiField
    InlineLabel startLabel;

    public NumbersRatingWidget(RatingQuestion question) {
        super(question);

        options = new RadioButtonGroup<Integer>(IntegerRenderer.instance(), question.getLabel());

        widget = binder.createAndBindUi(this);
        widget.addAttachHandler(this);

        options.addValueChangeHandler(this);
        updateOptions(question);
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        boolean hasLabels = question.withLabels();
        startLabel.setVisible(hasLabels);
        endLabel.setVisible(hasLabels);
        if (hasLabels) {
            startLabel.setText(question.getStartLabel());
            endLabel.setText(question.getEndLabel());
        }
    }

    @Override
    public void setValue(BigDecimal value) {
        super.setValue(value);

        options.setValue(value.intValue());
    }

    @Override
    public void setQuestion(RatingQuestion question) {
        super.setQuestion(question);

        updateOptions(question);
    }

    @Override
    public void setEnabled(boolean enable) {
        options.setEnabled(enable);
    }

    @Override
    public void onValueChange(ValueChangeEvent<Integer> event) {
        setValue(new BigDecimal(event.getValue()));
    }

    private void updateOptions(RatingQuestion question) {
        options.clear();
        for (int i = question.getMinLength(); i <= question.getMaxLength(); i++) {
            options.add(i);
        }
    }
}
