package org.consumersunion.stories.server.business_logic.interceptor;

import javax.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.server.business_logic.UserService;
import org.consumersunion.stories.server.exception.NotAuthorizedException;
import org.springframework.stereotype.Component;

@Component
public class RequiresLoggedUserInterceptor implements MethodInterceptor {
    private final UserService userService;

    @Inject
    RequiresLoggedUserInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        User loggedInUser = userService.getLoggedInUser();

        if (loggedInUser == null) {
            throw new NotAuthorizedException();
        }

        return invocation.proceed();
    }
} 
