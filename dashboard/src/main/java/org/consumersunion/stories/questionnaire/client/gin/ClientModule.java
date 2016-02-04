package org.consumersunion.stories.questionnaire.client.gin;

import org.consumersunion.stories.common.client.CommonModule;
import org.consumersunion.stories.common.client.place.ClientPlaceManager;
import org.consumersunion.stories.common.client.util.CachingService;
import org.consumersunion.stories.questionnaire.client.application.ApplicationModule;
import org.consumersunion.stories.questionnaire.client.application.questionnaire.CollectionHolder;
import org.consumersunion.stories.questionnaire.client.place.NameTokens;

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

        bind(CachingService.class).in(Singleton.class);
        bind(CollectionHolder.class).asEagerSingleton();

        bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.questionnaire);
    }
}
