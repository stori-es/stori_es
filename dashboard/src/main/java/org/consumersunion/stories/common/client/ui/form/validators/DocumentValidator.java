package org.consumersunion.stories.common.client.ui.form.validators;

import org.consumersunion.stories.common.client.i18n.FormI18nMessages;
import org.consumersunion.stories.common.client.ui.form.InputValidationException;
import org.consumersunion.stories.common.client.ui.form.Validator;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.regexp.shared.RegExp;

public class DocumentValidator implements Validator {
    private final FormI18nMessages messages = GWT.create(FormI18nMessages.class);

    private static final String URL_DOC = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+" +
            "([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";

    @Override
    public void validate(String value) {
        if (Strings.isNullOrEmpty(value.trim())) {
            throw new InputValidationException(messages.requiredField());
        }

        RegExp imgExp = RegExp.compile(URL_DOC);
        if (!imgExp.test(value)) {
            throw new InputValidationException(messages.invalidDocumentUrl());
        }
    }
}
