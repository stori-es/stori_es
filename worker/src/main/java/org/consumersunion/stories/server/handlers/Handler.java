package org.consumersunion.stories.server.handlers;

public interface Handler<T> {
    boolean canHandle(Object element);

    void handle(T element) throws Exception;
}
