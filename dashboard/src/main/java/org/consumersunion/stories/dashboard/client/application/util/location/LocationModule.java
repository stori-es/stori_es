package org.consumersunion.stories.dashboard.client.application.util.location;

import javax.inject.Singleton;

import com.google.gwt.inject.client.AbstractGinModule;

public class LocationModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(DefaultLocationProvider.class).to(DefaultLocationProviderImpl.class).in(Singleton.class);
    }
}
