package org.consumersunion.stories.login.gin;

import org.consumersunion.stories.common.client.CommonModule;
import org.consumersunion.stories.login.application.ApplicationModule;
import org.consumersunion.stories.login.place.NameTokens;
import org.consumersunion.stories.login.resource.ResourceLoader;

import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.annotations.ErrorPlace;
import com.gwtplatform.mvp.client.annotations.UnauthorizedPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import com.gwtplatform.mvp.client.proxy.DefaultPlaceManager;

public class ClientModule extends AbstractPresenterModule {

    @Override
    protected void configure() {
        install(new DefaultModule.Builder().placeManager(DefaultPlaceManager.class).build());
        install(new CommonModule());
        install(new ApplicationModule());

        bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.login);
        bindConstant().annotatedWith(UnauthorizedPlace.class).to(NameTokens.login);
        bindConstant().annotatedWith(ErrorPlace.class).to(NameTokens.login);

        bind(ResourceLoader.class).asEagerSingleton();
    }
}
