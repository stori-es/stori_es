package org.consumersunion.stories.dashboard.client.application.story.popup;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class StoryPopupModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenterWidget(AddAttachmentPresenter.class, AddAttachmentPresenter.MyView.class,
                AddAttachmentView.class);
    }
}
