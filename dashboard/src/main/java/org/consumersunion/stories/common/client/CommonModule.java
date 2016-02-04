package org.consumersunion.stories.common.client;

import org.consumersunion.stories.common.client.api.ApiModule;
import org.consumersunion.stories.common.client.api.RestCallbackAdapter;
import org.consumersunion.stories.common.client.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.common.client.i18n.CommonI18nMessages;
import org.consumersunion.stories.common.client.i18n.FormI18nMessages;
import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.model.StorySortFieldDropDownItem;
import org.consumersunion.stories.common.client.resource.CommonResources;
import org.consumersunion.stories.common.client.resource.ResourceLoader;
import org.consumersunion.stories.common.client.ui.UiModule;
import org.consumersunion.stories.common.client.ui.form.Form;
import org.consumersunion.stories.common.client.ui.stories.StoriesModule;
import org.consumersunion.stories.common.client.util.CurrentUser;
import org.consumersunion.stories.common.client.util.CustomHasRowsFactory;
import org.consumersunion.stories.common.client.util.MessageDispatcher;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.client.widget.LoadingPresenter;
import org.consumersunion.stories.common.client.widget.LoadingView;
import org.consumersunion.stories.common.client.widget.WidgetsModule;
import org.consumersunion.stories.common.shared.dto.tasks.TasksModule;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.CollectionSortDropDownItem;
import org.consumersunion.stories.common.shared.model.Locale;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class CommonModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new ApiModule());
        install(new UiModule());
        install(new StoriesModule());
        install(new WidgetsModule());
        install(new TasksModule());

        bindI18n();

        bind(ResourceLoader.class).asEagerSingleton();

        bind(CommonResources.class).in(Singleton.class);
        requestStaticInjection(Form.class);

        requestStaticInjection(Locale.class);
        requestStaticInjection(CollectionSortDropDownItem.class);
        requestStaticInjection(StorySortFieldDropDownItem.class);

        bindPresenterWidget(LoadingPresenter.class, LoadingPresenter.MyView.class, LoadingView.class);

        bind(MessageDispatcher.class).in(Singleton.class);

        bind(CurrentUser.class).asEagerSingleton();

        install(new GinFactoryModuleBuilder().build(CustomHasRowsFactory.class));

        requestStaticInjection(ResponseHandler.class);
        requestStaticInjection(RestCallbackAdapter.class);
    }

    @Provides
    Scheduler getScheduler() {
        return Scheduler.get();
    }

    private void bindI18n() {
        bind(CommonI18nErrorMessages.class).in(Singleton.class);
        bind(CommonI18nLabels.class).in(Singleton.class);
        bind(CommonI18nMessages.class).in(Singleton.class);
        bind(FormI18nMessages.class).in(Singleton.class);
        bind(StoryTellerDashboardI18nLabels.class).in(Singleton.class);
    }
}
