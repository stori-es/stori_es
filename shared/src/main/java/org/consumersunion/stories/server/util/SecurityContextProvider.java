package org.consumersunion.stories.server.util;

import javax.inject.Provider;

import org.springframework.security.core.context.SecurityContext;

public interface SecurityContextProvider extends Provider<SecurityContext> {
}
