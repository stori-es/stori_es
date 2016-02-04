package org.consumersunion.stories.common.client.ui.form.validators;

import org.consumersunion.stories.common.client.i18n.FormI18nMessages;
import org.consumersunion.stories.common.client.ui.form.InputValidationException;
import org.consumersunion.stories.common.client.ui.form.Validator;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;

public class EmailValidator implements Validator {
    private final FormI18nMessages messages = GWT.create(FormI18nMessages.class);

    @Override
    public void validate(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            if (!isEmail(value)) {
                throw new InputValidationException(messages.emailFormat());
            }
        }
    }

    public boolean isEmail(String value) {
        return value.toUpperCase().matches("^\\s*[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\s*$");
    }
}
