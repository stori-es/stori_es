package org.consumersunion.stories.server.util;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class ApplicationProfileInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        ConfigurableEnvironment environment = ctx.getEnvironment();

        if ("cloud".equals(System.getProperty("PARAM1"))) {
            environment.setActiveProfiles("cloud");
        }
    }
}
