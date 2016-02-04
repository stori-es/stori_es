package org.consumersunion.stories;

import org.consumersunion.stories.server.util.ApplicationContextProvider;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import junit.framework.TestCase;

public class SpringTestCase extends TestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        beanFactory.autowireBean(this);
    }
}
