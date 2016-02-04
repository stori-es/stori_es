package org.consumersunion.stories.common.client.ui.form.validators;

import org.consumersunion.stories.common.client.i18n.FormI18nMessages;
import org.consumersunion.stories.common.client.ui.form.InputValidationException;
import org.consumersunion.stories.common.client.ui.form.Validator;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;

public class MinLengthValidator implements Validator {
    private final FormI18nMessages messages = GWT.create(FormI18nMessages.class);
    private final int minLength;

    public MinLengthValidator(int minLength) {
        this.minLength = minLength;
    }

    @Override
    public void validate(final String value) {
        if (Strings.nullToEmpty(value).length() < minLength) {
            throw new InputValidationException(messages.valueAtLeast(minLength));
        }
    }
}
