package org.consumersunion.stories.server.util;

import org.consumersunion.stories.server.rest.api.convio.ConvioDataSynchronizationWorker;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationContextProvider implements ApplicationContextAware {
    private static final Object mutex = new Object();

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        applicationContext = ctx;
    }

    public static ApplicationContext getApplicationContext() {
        synchronized (mutex) {
            if (applicationContext == null) {
                applicationContext = new ClassPathXmlApplicationContext("classpath:META-INF/applicationContext.xml");
            }
        }
        return applicationContext;
    }

    public static ConvioDataSynchronizationWorker getConvioSyncWorker() {
        return getApplicationContext().getBean(ConvioDataSynchronizationWorker.class);
    }
}
