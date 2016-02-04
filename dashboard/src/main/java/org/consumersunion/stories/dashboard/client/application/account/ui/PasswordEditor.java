package org.consumersunion.stories.dashboard.client.application.account.ui;

import org.consumersunion.stories.common.client.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.common.client.util.MessageDispatcher;
import org.consumersunion.stories.common.client.widget.MessageStyle;

import com.google.common.base.Strings;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class PasswordEditor extends Composite implements Editor<PasswordDto> {
    interface Binder extends UiBinder<Widget, PasswordEditor> {
    }

    interface Driver extends SimpleBeanEditorDriver<PasswordDto, PasswordEditor> {
    }

    @UiField
    PasswordTextBox oldPassword;
    @UiField
    PasswordTextBox newPassword;
    @UiField
    PasswordTextBox passwordConfirmation;

    private final CommonI18nErrorMessages errorMessages;
    private final MessageDispatcher messageDispatcher;
    private final Driver driver;

    @Inject
    PasswordEditor(
            Binder uiBinder,
            Driver driver,
            CommonI18nErrorMessages errorMessages,
            MessageDispatcher messageDispatcher) {
        this.driver = driver;
        this.errorMessages = errorMessages;
        this.messageDispatcher = messageDispatcher;

        initWidget(uiBinder.createAndBindUi(this));
        driver.initialize(this);
    }

    public void edit() {
        driver.edit(new PasswordDto());
    }

    public PasswordDto get() {
        PasswordDto password = driver.flush();

        if (Strings.isNullOrEmpty(password.getNewPassword())
                || Strings.isNullOrEmpty(password.getOldPassword())
                || Strings.isNullOrEmpty(password.getPasswordConfirmation())) {
            messageDispatcher.displayMessage(MessageStyle.ERROR, errorMessages.requiredFields());
            password = null;
        } else if (!password.getNewPassword().equals(password.getPasswordConfirmation())) {
            messageDispatcher.displayMessage(MessageStyle.ERROR, errorMessages.confirmationPassword());
            password = null;
        }

        return password;
    }
}
