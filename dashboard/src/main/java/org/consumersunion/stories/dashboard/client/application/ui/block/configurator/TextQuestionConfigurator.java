package org.consumersunion.stories.dashboard.client.application.ui.block.configurator;

import java.util.Collection;

import org.consumersunion.stories.common.client.i18n.TextBoxFormatLabels;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.questionnaire.Question;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import static com.google.gwt.query.client.GQuery.$;

public class TextQuestionConfigurator extends AbstractConfigurator<Question> implements BlockConfigurator<Question> {
    interface Binder extends UiBinder<Widget, TextQuestionConfigurator> {
    }

    interface Driver extends SimpleBeanEditorDriver<Question, TextQuestionConfigurator> {
    }

    private enum TextBoxFormat {
        SINGLE_LINE(BlockType.TEXT_INPUT),
        PLAIN_TEXT(BlockType.TEXT_AREA),
        RICH_TEXT(BlockType.RICH_TEXT_AREA);

        private final BlockType code;

        TextBoxFormat(
                BlockType formType) {
            this.code = formType;
        }

        public BlockType getFormType() {
            return code;
        }

        public static TextBoxFormat fromQuestion(Question question) {
            for (TextBoxFormat format : values()) {
                if (format.code.equals(question.getFormType())) {
                    return format;
                }
            }

            throw new GeneralException("Unexpected form type");
        }

        public static Collection<TextBoxFormat> validValues(BlockType standardMeaning) {
            if (BlockType.STORY_ASK.equals(standardMeaning)) {
                return Lists.newArrayList(PLAIN_TEXT, RICH_TEXT);
            } else {
                return Lists.newArrayList(values());
            }
        }
    }

    @UiField(provided = true)
    @Ignore
    final ValueListBox<TextBoxFormat> format;

    @UiField
    TextBox text;
    @UiField
    @Ignore
    Label textError;
    @UiField
    TextBox helpText;
    @UiField
    IntegerBox minLength;
    @UiField
    IntegerBox maxLength;
    @UiField
    SimpleCheckBox required;
    @UiField
    DivElement advancedPanelContent;
    @UiField
    DivElement advancedToggle;
    @UiField
    DivElement advancedPanel;
    @UiField
    Resources resources;
    @UiField
    CommonI18nLabels labels;
    @UiField
    DivElement formatContainer;

    @Inject
    TextQuestionConfigurator(
            Binder uiBinder,
            Driver driver,
            final TextBoxFormatLabels labels,
            @Assisted Question question) {
        super(driver, question);

        format = new ValueListBox<TextBoxFormat>(new AbstractRenderer<TextBoxFormat>() {
            @Override
            public String render(TextBoxFormat value) {
                return labels.getString(value.name());
            }
        });

        format.setValue(TextBoxFormat.fromQuestion(question));
        format.setAcceptableValues(TextBoxFormat.validValues(question.getStandardMeaning()));

        initWidget(uiBinder.createAndBindUi(this));
        driver.initialize(this);

        setErrorLabels(textError);
        init();

        if (hideFormatSelection(question)) {
            formatContainer.removeFromParent();
        }

        bind();
    }

    @Override
    public boolean validate() {
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
    protected void onAttach() {
        super.onAttach();

        $(advancedPanelContent).hide();
        $(advancedPanel).removeClass(resources.blockConfigurators().advancedOpen());
        resetErrors();
    }

    @Override
    protected void onDone() {
        Question question = driver.flush();
        if (validate()) {
            resetErrors();
            question.setFormType(format.getValue().getFormType());
            doneCallback.onSuccess(question);

            setEditedValue(question);
        }
    }

    private boolean hideFormatSelection(Question question) {
        BlockType standardMeaning = question.getStandardMeaning();

        return standardMeaning != null && !BlockType.STORY_ASK.equals(standardMeaning);
    }

    private void bind() {
        if (hasAdvancedPanel()) {
            $(advancedToggle).click(new Function() {
                @Override
                public void f() {
                    String advancedOpen = resources.blockConfigurators().advancedOpen();
                    GQuery advancedPanelSelector = $(advancedPanel);

                    advancedPanelSelector.toggleClass(advancedOpen);
                    $(advancedPanelContent).slideToggle(200);

                    if (advancedPanelSelector.hasClass(advancedOpen)) {
                        $(advancedToggle).attr("data-tooltip", labels.clickToClose());
                    } else {
                        $(advancedToggle).attr("data-tooltip", labels.clickToOpen());
                    }
                }
            });

            $(advancedPanelContent).hide();
        } else {
            advancedPanel.removeFromParent();
        }
    }

    private boolean hasAdvancedPanel() {
        return !BlockType.ZIP_CODE.equals(getEditedValue().getStandardMeaning());
    }
}
