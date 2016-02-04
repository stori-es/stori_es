package org.consumersunion.stories.server.business_logic.interceptor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Retention(RetentionPolicy.RUNTIME)
@Target(METHOD)
public @interface EndOfQueue {
    /**
     * The service key identifying this method. If another method is identified by the same key,
     * all other methods will be cancelled.
     */
    String value();
}
