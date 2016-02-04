package org.consumersunion.stories.server.business_logic.interceptor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * Executes this method only if no other method associated with the given key is executing. If another method is
 * executing in the queue associated with the key, this execution will be skipped.
 * <p/>
 * In order to prevent {@link NullPointerException}s, a method annotated with {@code OnlyIfEmptyQueue} must return
 * {@code void}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(METHOD)
public @interface OnlyIfEmptyQueue {
    /**
     * The service key identifying this method. If another method is identified by the same key,
     * all other methods will be cancelled.
     */
    String value();
}
