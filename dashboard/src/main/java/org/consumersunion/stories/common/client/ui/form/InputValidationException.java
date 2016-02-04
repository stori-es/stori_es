package org.consumersunion.stories.common.client.ui.form;

public class InputValidationException extends RuntimeException {
    private static final long serialVersionUID = 7120162250080108298L;

    public InputValidationException(final String message) {
        super(message);
    }
}
