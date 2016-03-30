package org.consumersunion.stories.dashboard.client.application.ui.block.configurator;

import java.util.Arrays;
import java.util.Collection;

import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.questionnaire.ContactBlock;
import org.consumersunion.stories.common.shared.model.type.ContactType;
import org.consumersunion.stories.common.shared.model.type.DataType;

import com.google.common.base.Strings;
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

public class ContactConfigurator extends AbstractConfigurator<ContactBlock> implements BlockConfigurator<ContactBlock> {
    interface Binder extends UiBinder<Widget, ContactConfigurator> {
    }

    interface Driver extends SimpleBeanEditorDriver<ContactBlock, ContactConfigurator> {
    }

    @UiField(provided = true)
    @Ignore
    final ValueListBox<ContactType> contactType;

    @UiField
    TextBox text;
    @UiField
    @Ignore
    Label textError;
    @UiField
    TextBox helpText;
    @UiField
    SimpleCheckBox required;

    @Inject
    ContactConfigurator(
            Binder uiBinder,
            Driver driver,
            CommonI18nLabels labels,
            @Assisted ContactBlock contactBlock) {
        super(driver, contactBlock);

        this.contactType = new ValueListBox<ContactType>(new AbstractRenderer<ContactType>() {
            @Override
            public String render(ContactType contactType) {
                return contactType == null ? "" : contactType.code();
            }
        });

        initWidget(uiBinder.createAndBindUi(this));
        driver.initialize(this);

        if (!Strings.isNullOrEmpty(contactBlock.getOption())) {
            contactType.setValue(ContactType.valueOfCode(contactBlock.getOption()));
        } else {
            contactType.setValue(ContactType.HOME);
        }

        if (DataType.DATA_EMAIL.code().equals(contactBlock.getDataType())) {
            contactType.setAcceptableValues(ContactType.getEmailValues());
        } else {
            contactType.setAcceptableValues(Arrays.asList(ContactType.values()));
        }

        setErrorLabels(textError);
        init();

        if (Strings.isNullOrEmpty(text.getText())) {
            String value;
            if (DataType.DATA_EMAIL.code().equals(contactBlock.getDataType())) {
                value = labels.standQuestEmail();
            } else {
                value = labels.standQuestPhone();
            }

            text.setText(value);
        }
    }

    public void setOptions(Collection<ContactType> options) {
        contactType.setAcceptableValues(options);
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
    protected void onDone() {
        ContactBlock contactBlock = driver.flush();
        if (validate()) {
            updateType(contactBlock);
            contactBlock.setOption(contactType.getValue().code());
            doneCallback.onSuccess(contactBlock);

            setEditedValue(contactBlock);
        }
    }

    private void updateType(ContactBlock contactBlock) {
        BlockType standardType = null;
        if (DataType.DATA_EMAIL.code().equals(contactBlock.getDataType())) {
            switch (contactType.getValue()) {
                case HOME:
                    standardType = BlockType.EMAIL;
                    break;
                case OTHER:
                    standardType = BlockType.EMAIL_OTHER;
                    break;
                case WORK:
                    standardType = BlockType.EMAIL_WORK;
                    break;
            }
        } else if (DataType.DATA_PHONE_NUMBER.code().equals(contactBlock.getDataType())) {
            switch (contactType.getValue()) {
                case HOME:
                    standardType = BlockType.PHONE;
                    break;
                case MOBILE:
                    standardType = BlockType.PHONE_MOBILE;
                    break;
                case OTHER:
                    standardType = BlockType.PHONE_OTHER;
                    break;
                case WORK:
                    standardType = BlockType.PHONE_WORK;
                    break;
            }
        }

        if (standardType != null) {
            contactBlock.setBlockType(standardType);
        }
    }
}
