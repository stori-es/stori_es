package org.consumersunion.stories.dashboard.client.application.profile;

import org.consumersunion.stories.dashboard.client.application.profile.ui.ProfileUiModule;
import org.consumersunion.stories.dashboard.client.application.profile.widget.ProfileWidgetModule;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ProfileModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new ProfileWidgetModule());
        install(new ProfileUiModule());

        bindPresenter(ProfilePresenter.class, ProfilePresenter.MyView.class, ProfileView.class,
                ProfilePresenter.MyProxy.class);
    }
}
