package org.consumersunion.stories.server.util;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class SecurityContextProviderImpl implements SecurityContextProvider {
    @Override
    public SecurityContext get() {
        return SecurityContextHolder.getContext();
    }
}
