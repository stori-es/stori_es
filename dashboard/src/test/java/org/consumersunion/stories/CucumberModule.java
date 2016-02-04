package org.consumersunion.stories;

import java.util.logging.Logger;

import org.consumersunion.stories.cucumber.utils.WebDriverManager;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class CucumberModule extends AbstractModule {
    private static Logger logger = Logger.getLogger(CucumberModule.class.getName());

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public WebDriverManager getWebDriverManager() {
        return new WebDriverManager();
    }
}
