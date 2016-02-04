package org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab;

import com.google.gwt.regexp.shared.RegExp;

public class PermalinkValidator {
    private static final RegExp PERMALINK_PATTERN = RegExp.compile("^((?:[A-Za-z0-9-_])*?)$");

    public boolean isValidLink(String link) {
        try {
            return PERMALINK_PATTERN.test(link);
        } catch (RuntimeException e) {
            return false;
        }
    }
}
