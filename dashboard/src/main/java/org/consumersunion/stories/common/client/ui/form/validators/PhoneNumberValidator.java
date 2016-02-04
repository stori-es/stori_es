package org.consumersunion.stories.common.client.ui.form.validators;

import org.consumersunion.stories.common.client.i18n.FormI18nMessages;
import org.consumersunion.stories.common.client.ui.form.InputValidationException;
import org.consumersunion.stories.common.client.ui.form.Validator;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;

public class PhoneNumberValidator implements Validator {
    private final FormI18nMessages messages = GWT.create(FormI18nMessages.class);

    @Override
    public void validate(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            if (!isPhoneNumber(value)) {
                throw new InputValidationException(messages.phoneFormat());
            }
        }
    }

    private boolean isPhoneNumber(String value) {
        return value.toUpperCase().trim().matches("^\\(?(\\d{3})\\)?[-. ]?([A-Z0-9]{3})[-. ]?([A-Z0-9]{4})$");
    }
}
