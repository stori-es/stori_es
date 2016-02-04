package org.consumersunion.stories.dashboard.client.application;

import org.consumersunion.stories.dashboard.client.application.account.AccountModule;
import org.consumersunion.stories.dashboard.client.application.administration.AdministrationModule;
import org.consumersunion.stories.dashboard.client.application.collection.CollectionModule;
import org.consumersunion.stories.dashboard.client.application.collections.CollectionsModule;
import org.consumersunion.stories.dashboard.client.application.organization.OrganizationModule;
import org.consumersunion.stories.dashboard.client.application.profile.ProfileModule;
import org.consumersunion.stories.dashboard.client.application.questionnaire.QuestionnaireModule;
import org.consumersunion.stories.dashboard.client.application.stories.StoriesModule;
import org.consumersunion.stories.dashboard.client.application.story.StoryModule;
import org.consumersunion.stories.dashboard.client.application.ui.UiModule;
import org.consumersunion.stories.dashboard.client.application.util.location.LocationModule;
import org.consumersunion.stories.dashboard.client.application.widget.WidgetModule;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ApplicationModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new AdministrationModule());
        install(new AccountModule());
        install(new StoriesModule());
        install(new StoryModule());
        install(new ProfileModule());
        install(new QuestionnaireModule());
        install(new CollectionsModule());
        install(new CollectionModule());
        install(new OrganizationModule());
        install(new WidgetModule());
        install(new LocationModule());
        install(new UiModule());

        bindPresenter(StoriesDashboardPresenter.class, StoriesDashboardPresenter.MyView.class,
                StoriesDashboardView.class, StoriesDashboardPresenter.MyProxy.class);

        bind(ApplicationPresenter.class).asEagerSingleton();
        bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class,
                ApplicationPresenter.MyProxy.class);
    }
}
