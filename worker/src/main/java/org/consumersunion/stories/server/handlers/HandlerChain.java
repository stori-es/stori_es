package org.consumersunion.stories.server.handlers;

public interface HandlerChain<T> {
    void process(T element) throws Exception;
}
