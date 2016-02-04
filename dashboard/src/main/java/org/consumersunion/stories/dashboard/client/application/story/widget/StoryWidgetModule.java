package org.consumersunion.stories.dashboard.client.application.story.widget;

import org.consumersunion.stories.dashboard.client.application.story.widget.navbar.NavBarModule;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class StoryWidgetModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new NavBarModule());

        bindPresenterWidget(StoriesByAuthorPresenter.class, StoriesByAuthorPresenter.MyView.class,
                StoriesByAuthorView.class);
        bindPresenterWidget(AttachmentsPresenter.class, AttachmentsPresenter.MyView.class,
                AttachmentsView.class);
    }
}
