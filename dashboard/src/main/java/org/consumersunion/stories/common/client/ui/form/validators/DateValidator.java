package org.consumersunion.stories.common.client.ui.form.validators;

import java.util.Date;

import org.consumersunion.stories.common.client.i18n.FormI18nMessages;
import org.consumersunion.stories.common.client.ui.form.InputValidationException;
import org.consumersunion.stories.common.client.ui.form.Validator;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;

public class DateValidator implements Validator {
    private final FormI18nMessages messages = GWT.create(FormI18nMessages.class);
    private final String startDate;
    private final DateTimeFormat format;

    public DateValidator(String startDate) {
        this.startDate = startDate;
        this.format = DateTimeFormat.getFormat("MM/dd/yyyy");
    }

    @Override
    public void validate(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            try {
                Date current = format.parse(value);

                if (!Strings.isNullOrEmpty(startDate)) {
                    Date start = format.parse(startDate);

                    if (current.getTime() < start.getTime()) {
                        throw new InputValidationException(messages.dateInvalid(format.format(start)));
                    }
                }
            } catch (IllegalArgumentException ex) {
                throw new InputValidationException(messages.dateFormatInvalid());
            }
        }
    }
}
