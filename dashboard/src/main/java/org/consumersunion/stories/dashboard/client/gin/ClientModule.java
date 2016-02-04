package org.consumersunion.stories.dashboard.client.gin;

import org.consumersunion.stories.common.client.CommonModule;
import org.consumersunion.stories.common.client.place.ClientPlaceManager;
import org.consumersunion.stories.common.client.util.CachingService;
import org.consumersunion.stories.dashboard.client.application.ApplicationModule;
import org.consumersunion.stories.dashboard.client.resource.Resources;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;

import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;

public class ClientModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new DefaultModule.Builder().placeManager(ClientPlaceManager.class).build());
        install(new CommonModule());
        install(new ApplicationModule());

        bind(Resources.class).in(Singleton.class);
        bind(CachingService.class).in(Singleton.class);
        bind(ClientPlaceManager.class).in(Singleton.class);

        bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.stories);
    }
}
