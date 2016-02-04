package org.consumersunion.stories.server.exception;

public class NotLoggedInException extends RuntimeException {
    private static final String NOT_LOGGED_IN = "Not logged in.";

    public NotLoggedInException(String message) {
        super(message);
    }

    public NotLoggedInException() {
        this(NOT_LOGGED_IN);
    }
}
