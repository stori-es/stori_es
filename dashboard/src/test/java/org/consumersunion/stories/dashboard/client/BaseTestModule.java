package org.consumersunion.stories.dashboard.client;

import org.consumersunion.stories.common.client.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.common.client.util.CachingService;
import org.consumersunion.stories.common.client.util.CookiesHelper;
import org.consumersunion.stories.common.client.util.CurrentUser;
import org.consumersunion.stories.common.client.util.MessageDispatcher;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.jukito.JukitoModule;
import org.jukito.TestSingleton;

import com.google.gwt.core.client.Scheduler;
import com.google.inject.Provides;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

public class BaseTestModule extends JukitoModule {
    @Override
    protected void configureTest() {
        bindMock(EventBus.class).in(TestSingleton.class);
        bindMock(CommonI18nErrorMessages.class).in(TestSingleton.class);
        bindMock(CachingService.class).in(TestSingleton.class);
        bindMock(MessageDispatcher.class).in(TestSingleton.class);
        bindMock(CookiesHelper.class).in(TestSingleton.class);
        bindMock(Scheduler.class).in(TestSingleton.class);

        requestStaticInjection(ResponseHandler.class);
    }

    @Provides
    @TestSingleton
    PlaceManager mockPlaceManager() {
        return mock(PlaceManager.class, RETURNS_DEEP_STUBS);
    }

    @Provides
    @TestSingleton
    CurrentUser mockCurrentUser() {
        return mock(CurrentUser.class, RETURNS_DEEP_STUBS);
    }
}
