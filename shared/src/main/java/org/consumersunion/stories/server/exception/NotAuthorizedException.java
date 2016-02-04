package org.consumersunion.stories.server.exception;

public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException() {
        this("Requested action is not authorized.");
    }

    public NotAuthorizedException(String message) {
        super(message);
    }

    public NotAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
