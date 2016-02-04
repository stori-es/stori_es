package org.consumersunion.stories.dashboard.client.application.ui.block.configurator;

import java.util.Arrays;

import org.consumersunion.stories.common.client.i18n.RatingLabels;
import org.consumersunion.stories.common.client.widget.RadioButtonGroup;
import org.consumersunion.stories.common.shared.model.questionnaire.RatingQuestion;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import static com.google.gwt.query.client.GQuery.$;

public class RatingQuestionConfigurator extends AbstractConfigurator<RatingQuestion>
        implements BlockConfigurator<RatingQuestion> {
    interface Binder extends UiBinder<Widget, RatingQuestionConfigurator> {
    }

    interface Driver extends SimpleBeanEditorDriver<RatingQuestion, RatingQuestionConfigurator> {
    }

    @UiField(provided = true)
    @Ignore
    final RadioButtonGroup<RatingQuestion.DisplayType> displayType;
    @UiField(provided = true)
    final ValueListBox<RatingQuestion.StepType> stepType;

    @UiField
    TextBox text;
    @UiField
    @Ignore
    Label textError;
    @UiField
    TextBox helpText;
    @UiField
    SimpleCheckBox required;
    @UiField
    @Ignore
    SimpleCheckBox labelsCheckbox;
    @UiField
    @Ignore
    TextBox tail;
    @UiField
    @Ignore
    TextBox head;
    @UiField
    @Ignore
    DivElement labelsForStars;
    @UiField
    @Ignore
    TextBox end;
    @UiField
    @Ignore
    TextBox start;
    @UiField
    @Ignore
    DivElement labelsForNumbers;
    @UiField
    ListBox minLength;
    @UiField
    ListBox maxLength;
    @UiField
    DivElement stepTypeWrapper;
    @UiField
    DivElement scaleWrapper;

    @Inject
    RatingQuestionConfigurator(
            Binder uiBinder,
            Driver driver,
            RatingLabels labels,
            @Assisted RatingQuestion question) {
        super(driver, question);

        stepType = new ValueListBox<RatingQuestion.StepType>(this.<RatingQuestion.StepType>createRenderer(labels));
        stepType.setValue(question.getStepType());
        stepType.setAcceptableValues(Arrays.asList(RatingQuestion.StepType.values()));

        displayType = new RadioButtonGroup<RatingQuestion.DisplayType>(
                this.<RatingQuestion.DisplayType>createRenderer(labels), "rating" + question.getIdx());
        displayType.add(RatingQuestion.DisplayType.values());
        displayType.setValue(RatingQuestion.DisplayType.fromFormType(question.getFormType()));

        initWidget(uiBinder.createAndBindUi(this));
        driver.initialize(this);

        setErrorLabels(textError);
        init();
    }

    @Override
    public boolean validate() {
        driver.flush();
        if (!driver.hasErrors() && !Strings.isNullOrEmpty(text.getText().trim())) {
            resetErrors();
            return true;
        } else {
            textError.setText(messages.requiredField());
            return false;
        }
    }

    @Override
    public boolean isNew() {
        return Strings.isNullOrEmpty(getEditedValue().getText());
    }

    @Override
    protected void onDone() {
        if (validate()) {
            RatingQuestion question = driver.flush();
            RatingQuestion.DisplayType displayType = this.displayType.getValue();
            if (labelsCheckbox.getValue()) {
                if (RatingQuestion.DisplayType.STARS.equals(displayType)) {
                    question.setStartLabel(head.getText().trim());
                    question.setEndLabel(tail.getText().trim());
                } else {
                    question.setStartLabel(start.getText().trim());
                    question.setEndLabel(end.getText().trim());
                }
            } else {
                question.setStartLabel(null);
                question.setEndLabel(null);
                start.setText("");
                end.setText("");
                head.setText("");
                tail.setText("");
            }

            if (RatingQuestion.DisplayType.STARS.equals(displayType)) {
                question.setMinLength(null);
                question.setMaxLength(null);
            } else {
                question.setMinLength(Integer.valueOf(minLength.getSelectedValue()));
                question.setMaxLength(Integer.valueOf(maxLength.getSelectedValue()));
            }

            question.setFormType(displayType.toFormType());
            doneCallback.onSuccess(question);

            setEditedValue(question);
        }
    }

    @Override
    protected void init() {
        super.init();

        RatingQuestion editedValue = getEditedValue();

        updateForDisplayType(RatingQuestion.DisplayType.fromFormType(editedValue.getFormType()));

        boolean hasLabels = editedValue.withLabels();
        labelsCheckbox.setValue(hasLabels, true);
        updateForLabels(hasLabels);

        if (RatingQuestion.DisplayType.STARS.equals(displayType.getValue())) {
            head.setText(editedValue.getStartLabel());
            tail.setText(editedValue.getEndLabel());
        } else {
            start.setText(editedValue.getStartLabel());
            end.setText(editedValue.getEndLabel());
        }
    }

    @UiHandler("labelsCheckbox")
    void onLabelsChecked(ValueChangeEvent<Boolean> event) {
        updateForLabels(event.getValue());
    }

    @UiHandler("displayType")
    void onDisplayTypeChanged(ValueChangeEvent<RatingQuestion.DisplayType> event) {
        updateForDisplayType(event.getValue());
    }

    private void updateForLabels(Boolean checked) {
        if (checked) {
            $(labelsCheckbox).siblings()
                    .removeClass(commonResources.generalStyleCss().disabled())
                    .find("input")
                    .removeAttr("disabled");
        } else {
            $(labelsCheckbox).siblings()
                    .addClass(commonResources.generalStyleCss().disabled())
                    .find("input")
                    .attr("disabled", "disabled");
        }
    }

    private void updateForDisplayType(RatingQuestion.DisplayType value) {
        displayType.setValue(value);
        switch (value) {
            case NUMBERS:
                $(Lists.newArrayList(stepTypeWrapper, labelsForStars)).hide();
                $(Lists.newArrayList(scaleWrapper, labelsForNumbers)).show();
                break;
            case STARS:
                $(Lists.newArrayList(scaleWrapper, labelsForNumbers)).hide();
                $(Lists.newArrayList(stepTypeWrapper, labelsForStars)).show();
                break;
        }
    }

    private <T extends Enum<T>> Renderer<T> createRenderer(final ConstantsWithLookup labels) {
        return new AbstractRenderer<T>() {
            @Override
            public String render(T object) {
                return labels.getString(object.name());
            }
        };
    }
}
