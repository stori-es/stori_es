package org.consumersunion.stories.server.util;

import javax.inject.Provider;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class HttpSessionProvider implements Provider<HttpSession> {
    @Override
    public HttpSession get() {
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return attr.getRequest().getSession();
        } catch (IllegalStateException e) {
            return null;
        }
    }
}
