package org.consumersunion.stories.dashboard.client.application.story.widget.navbar;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class NavBarModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenterWidget(NavigationBarPresenter.class, NavigationBarPresenter.MyView.class, NavigationBarView.class);
    }
}
