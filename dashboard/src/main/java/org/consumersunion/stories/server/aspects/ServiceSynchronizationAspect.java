package org.consumersunion.stories.server.aspects;

import javax.inject.Inject;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.consumersunion.stories.server.business_logic.interceptor.EndOfQueue;
import org.consumersunion.stories.server.business_logic.interceptor.OnlyIfEmptyQueue;
import org.consumersunion.stories.server.cache.MemcacheService;

@Aspect
public class ServiceSynchronizationAspect {
    private static final int SLEEP_MS = 1000;

    private final MemcacheService memcacheService;

    @Inject
    ServiceSynchronizationAspect(MemcacheService memcacheService) {
        this.memcacheService = memcacheService;
    }

    @Pointcut("execution(@org.consumersunion.stories.server.business_logic.interceptor.OnlyIfEmptyQueue void *(..))")
    public void onlyIfEmptyQueueExecution() {
    }

    @Pointcut("execution(@org.consumersunion.stories.server.business_logic.interceptor.EndOfQueue void *(..))")
    public void endOfQueueExecution() {
    }

    @Around(value = "onlyIfEmptyQueueExecution() && @annotation(annotation)", argNames = "annotation")
    public Object executeOnlyIfEmptyQueue(ProceedingJoinPoint joinPoint, OnlyIfEmptyQueue annotation) throws Throwable {
        String key = annotation.value();
        Object result = null;

        if (!memcacheService.getBoolean(key, false)) {
            result = doInvoke(joinPoint, key);
        }

        return result;
    }

    @Around(value = "endOfQueueExecution() && @annotation(annotation)", argNames = "annotation")
    public Object executeAtEndOfQueue(ProceedingJoinPoint joinPoint, EndOfQueue annotation) throws Throwable {
        String key = annotation.value();

        while (memcacheService.getBoolean(key, false)) {
            try {
                Thread.sleep(SLEEP_MS);
            } catch (InterruptedException ignore) {
            }
        }

        return doInvoke(joinPoint, key);
    }

    private Object doInvoke(ProceedingJoinPoint joinPoint, String key) throws Throwable {
        memcacheService.put(key, true);

        try {
            return joinPoint.proceed();
        } finally {
            memcacheService.put(key, false);
        }
    }
}
