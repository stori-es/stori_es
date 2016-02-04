package org.consumersunion.stories.dashboard.client.application.ui.block.configurator;

import java.util.Arrays;

import org.consumersunion.stories.common.client.i18n.MultipleChoiceFormatLabels;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.common.shared.service.GeneralException;

import com.google.common.base.Strings;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class MultiSelectQuestionConfigurator extends AbstractConfigurator<Question>
        implements BlockConfigurator<Question> {
    interface Binder extends UiBinder<Widget, MultiSelectQuestionConfigurator> {
    }

    interface Driver extends SimpleBeanEditorDriver<Question, MultiSelectQuestionConfigurator> {
    }

    private enum MultipleChoiceFormat {
        RADIO_BUTTONS(BlockType.RADIO, false),
        SELECT_SINGLE(BlockType.SELECT, false),
        SELECT_MULTIPLE(BlockType.SELECT, true),
        CHECKBOXES(BlockType.CHECKBOX, true);

        private final BlockType code;
        private final boolean isMultiSelect;

        MultipleChoiceFormat(
                BlockType formType,
                boolean isMultiSelect) {
            this.code = formType;
            this.isMultiSelect = isMultiSelect;
        }

        public BlockType getFormType() {
            return code;
        }

        public boolean isMultiSelect() {
            return isMultiSelect;
        }

        public static MultipleChoiceFormat fromQuestion(Question question) {
            BlockType formType = question.getFormType();
            if (BlockType.RADIO.equals(formType)) {
                return RADIO_BUTTONS;
            } else if (BlockType.CHECKBOX.equals(formType)) {
                return CHECKBOXES;
            } else if (BlockType.SELECT.equals(formType)) {
                if (question.isMultiselect()) {
                    return SELECT_MULTIPLE;
                } else {
                    return SELECT_SINGLE;
                }
            }

            throw new GeneralException("Unexpected form type");
        }
    }

    @UiField(provided = true)
    final OptionListEditor options;
    @UiField(provided = true)
    @Ignore
    final ValueListBox<MultipleChoiceFormat> format;

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
    DivElement formatContainer;
    @UiField
    @Ignore
    Label optionsError;
    @UiField
    DivElement requiredContainer;

    @Inject
    MultiSelectQuestionConfigurator(
            Binder uiBinder,
            Driver driver,
            OptionListEditor options,
            final MultipleChoiceFormatLabels labels,
            @Assisted Question question) {
        super(driver, question);

        this.options = options;
        format = new ValueListBox<MultipleChoiceFormat>(new AbstractRenderer<MultipleChoiceFormat>() {
            @Override
            public String render(MultipleChoiceFormat value) {
                return labels.getString(value.name());
            }
        });

        format.setValue(MultipleChoiceFormat.fromQuestion(question));
        format.setAcceptableValues(Arrays.asList(MultipleChoiceFormat.values()));

        initWidget(uiBinder.createAndBindUi(this));
        driver.initialize(this);

        setErrorLabels(textError, optionsError);
        init();

        boolean allowEdit = !BlockType.STATE.equals(question.getStandardMeaning());
        options.allowEdit(allowEdit);

        if (!allowEdit || hideFormatSelection(question)) {
            formatContainer.removeFromParent();
        }

        boolean hideRequiredCheckbox =
                BlockType.UPDATES_OPT_IN.equals(question.getStandardMeaning());

        if (hideRequiredCheckbox) {
            requiredContainer.removeFromParent();
        }
    }

    @Override
    public boolean validate() {
        boolean emptyText = Strings.isNullOrEmpty(text.getText().trim());
        if (!driver.hasErrors() && !emptyText && !options.getValue().isEmpty()) {
            resetErrors();

            return true;
        } else {
            if (emptyText) {
                textError.setText(messages.requiredField());
            } else {
                optionsError.setText(messages.requiredField());
            }

            return false;
        }
    }

    @Override
    public boolean isNew() {
        return Strings.isNullOrEmpty(getEditedValue().getText());
    }

    @Override
    protected void onDone() {
        Question question = driver.flush();
        if (validate()) {
            MultipleChoiceFormat choiceFormat = format.getValue();
            question.setFormType(choiceFormat.getFormType());
            question.setMultiselect(choiceFormat.isMultiSelect());

            doneCallback.onSuccess(question);

            setEditedValue(question);
        }
    }

    private boolean hideFormatSelection(Question question) {
        BlockType standardMeaning = question.getStandardMeaning();

        return BlockType.PREFERRED_EMAIL_FORMAT.equals(standardMeaning)
                || BlockType.UPDATES_OPT_IN.equals(standardMeaning);
    }
}
