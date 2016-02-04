package org.consumersunion.stories.common.client.ui.form;

import java.util.List;

import org.consumersunion.stories.common.client.i18n.FormI18nMessages;

import com.google.gwt.core.client.GWT;

public abstract class InputFormControl extends UiFormControl {
    private final FormI18nMessages messages = GWT.create(FormI18nMessages.class);

    public InputFormControl(final String label, final String key, final boolean required, final String rowClass) {
        super(label, key, required, rowClass);
    }

    @Override
    public void validate() {
        final List<String> values = getValues();
        int validValues = 0;

        final Validator validator = getValidator();
        for (final String val : values) {
            if (isValue(val)) {
                checkLength(val);

                if (validator != null) {
                    validator.validate(val);
                }
                validValues++;
            }
        }

        if (isRequired() && validValues == 0) {
            throw new InputValidationException(messages.requiredField());
        }
    }

    private void checkLength(final String value) {
        if (value.length() < getMinLength()) {
            throw new InputValidationException(messages.valueAtLeast(getMinLength()));
        }
        if (value.length() > getMaxLength()) {
            throw new InputValidationException(messages.valueMoreThan(getMaxLength()));
        }
    }

    public abstract List<String> getValues();

    public abstract void setValues(List<String> values);
}
