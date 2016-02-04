package org.consumersunion.stories.dashboard.client.application.story;

import org.consumersunion.stories.dashboard.client.application.story.popup.StoryPopupModule;
import org.consumersunion.stories.dashboard.client.application.story.ui.StoryUiModule;
import org.consumersunion.stories.dashboard.client.application.story.widget.StoryWidgetModule;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class StoryModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new StoryPopupModule());
        install(new StoryUiModule());
        install(new StoryWidgetModule());

        bindPresenter(StoryPresenter.class, StoryPresenter.MyView.class, StoryView.class, StoryPresenter.MyProxy.class);
    }
}
