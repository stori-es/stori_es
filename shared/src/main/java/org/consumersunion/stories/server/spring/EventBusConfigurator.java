package org.consumersunion.stories.server.spring;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

@Configuration
public class EventBusConfigurator {
    @Inject
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Bean
    public EventBus eventBus() {
        return new AsyncEventBus("async", threadPoolTaskExecutor);
    }
}
