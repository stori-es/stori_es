package org.consumersunion.stories.common.client.ui.form.validators;

import org.consumersunion.stories.common.client.i18n.FormI18nMessages;
import org.consumersunion.stories.common.client.ui.form.InputValidationException;
import org.consumersunion.stories.common.client.ui.form.Validator;

import com.google.gwt.core.client.GWT;

public class ZipCodeValidator implements Validator {
    private final FormI18nMessages messages = GWT.create(FormI18nMessages.class);

    @Override
    public void validate(final String value) {
        if (value != null && !value.matches("^\\s*\\d{5}([ _.,-]*\\d{4})?\\s*$")) {
            throw new InputValidationException(messages.zipFormat());
        }
    }
}
