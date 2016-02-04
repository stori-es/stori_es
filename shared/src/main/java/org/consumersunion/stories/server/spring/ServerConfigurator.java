package org.consumersunion.stories.server.spring;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.security.acls.domain.EhCacheBasedAclCache;

import net.sf.ehcache.CacheManager;

@Configuration
@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy
public class ServerConfigurator implements SchedulingConfigurer, AsyncConfigurer {
    private static final int CORE_POOL_SIZE = 10;

    @Bean
    public EhCacheBasedAclCache aclCache() throws IOException {
        EhCacheManagerFactoryBean cacheManagerFactory = new EhCacheManagerFactoryBean();
        cacheManagerFactory.setCacheManagerName(CacheManager.DEFAULT_NAME);
        cacheManagerFactory.setShared(true);
        cacheManagerFactory.afterPropertiesSet();

        EhCacheFactoryBean cacheFactory = new EhCacheFactoryBean();
        cacheFactory.setCacheManager(cacheManagerFactory.getObject());
        cacheFactory.setCacheName("aclCache");
        cacheFactory.afterPropertiesSet();

        return new EhCacheBasedAclCache(cacheFactory.getObject());
    }

    @Bean
    public String geocoderClientId() {
        return "gme-consumersunionof1";
    }

    @Bean
    public String geocoderKey() {
        return "pWvJPZMiN7lgGHWdo3KlonDphwg=";
    }

    @Bean
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(CORE_POOL_SIZE);
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        return (ThreadPoolTaskExecutor) getAsyncExecutor();
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(CORE_POOL_SIZE);
        executor.initialize();

        return executor;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());
    }
}
