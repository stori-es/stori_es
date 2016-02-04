package org.consumersunion.stories.server.business_logic.interceptor;

import java.lang.reflect.Method;

import javax.inject.Inject;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.stereotype.Component;

@Component
public class RequiresLoggedUserAdvisor extends AbstractPointcutAdvisor {
    private final StaticMethodMatcherPointcut pointcut = new StaticMethodMatcherPointcut() {
        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            return method.isAnnotationPresent(RequiresLoggedUser.class);
        }
    };

    private final RequiresLoggedUserInterceptor interceptor;

    @Inject
    RequiresLoggedUserAdvisor(RequiresLoggedUserInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.interceptor;
    }
} 
