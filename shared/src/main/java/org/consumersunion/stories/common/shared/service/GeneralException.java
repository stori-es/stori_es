package org.consumersunion.stories.common.shared.service;

import java.io.Serializable;

/**
 * Our general purpose runtime exception. We do this to better distinguish our
 * own explicit conversions from checked exceptions vs. third party run-time
 * exceptions. As we continue to develope the system, we will almost surely
 * create specific sub-classes for particular types of problems.
 */
public class GeneralException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public GeneralException(Throwable cause) {
        super(cause);
    }

    public GeneralException(String message) {
        super(message);
    }

    public GeneralException(String message, Throwable cause) {
        super(message, cause);
    }
}
