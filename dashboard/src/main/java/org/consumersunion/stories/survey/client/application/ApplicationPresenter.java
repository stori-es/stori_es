package org.consumersunion.stories.survey.client.application;

import org.consumersunion.stories.common.client.event.HideLoadingEvent;
import org.consumersunion.stories.common.client.event.ShowLoadingEvent;
import org.consumersunion.stories.common.client.widget.LoadingPresenter;

import com.google.gwt.event.shared.GwtEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class ApplicationPresenter extends Presenter<ApplicationPresenter.MyView, ApplicationPresenter.MyProxy>
        implements ShowLoadingEvent.ShowLoadingHandler, HideLoadingEvent.HideLoadingHandler {
    interface MyView extends View {
    }

    @ProxyStandard
    interface MyProxy extends Proxy<ApplicationPresenter> {
    }

    @ContentSlot
    public static final GwtEvent.Type<RevealContentHandler<?>> TYPE_SetMainContent = new GwtEvent
            .Type<RevealContentHandler<?>>();

    private final LoadingPresenter loadingPresenter;

    @Inject
    ApplicationPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            LoadingPresenter loadingPresenter) {
        super(eventBus, view, proxy, RevealType.Root);

        this.loadingPresenter = loadingPresenter;
    }

    @Override
    public void onShowLoading(ShowLoadingEvent event) {
        addToPopupSlot(loadingPresenter, true);
    }

    @Override
    public void onHideLoading(HideLoadingEvent event) {
        if (loadingPresenter.isVisible()) {
            removeFromPopupSlot(loadingPresenter);
        }
    }

    @Override
    protected void onBind() {
        super.onBind();

        addRegisteredHandler(ShowLoadingEvent.TYPE, this);
        addRegisteredHandler(HideLoadingEvent.TYPE, this);
    }
}
