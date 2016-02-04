package org.consumersunion.stories.common.client.widget;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

public class LoadingPresenter extends PresenterWidget<LoadingPresenter.MyView> {

    public interface MyView extends PopupView {
        void setFullScreen(boolean fullScreen);
    }

    @Inject
    public LoadingPresenter(EventBus eventBus, MyView view) {
        super(eventBus, view);
    }

    public void setFullScreen(boolean fullScreen) {
        getView().setFullScreen(fullScreen);
    }
}
