package org.consumersunion.stories.server.exception;

public class PermalinkAlreadyExistsException extends BadRequestException {
    public PermalinkAlreadyExistsException(String message) {
        super(message);
    }

    public PermalinkAlreadyExistsException() {
        this("Permalink already exists.");
    }
}
