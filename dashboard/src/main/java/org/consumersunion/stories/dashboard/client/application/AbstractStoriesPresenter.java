package org.consumersunion.stories.dashboard.client.application;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.place.ClientPlaceManager;

import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public abstract class AbstractStoriesPresenter<V extends View, P extends Proxy<?>> extends Presenter<V, P> {
    @Inject
    public ClientPlaceManager placeManager;

    public AbstractStoriesPresenter(boolean autoBind, EventBus eventBus, V view,
            P proxy) {
        super(autoBind, eventBus, view, proxy);
    }

    public AbstractStoriesPresenter(EventBus eventBus, V view, P proxy) {
        super(eventBus, view, proxy);
    }

    public AbstractStoriesPresenter(EventBus eventBus, V view, P proxy, RevealType revealType) {
        super(eventBus, view, proxy, revealType);
    }

    public AbstractStoriesPresenter(EventBus eventBus, V view, P proxy,
            GwtEvent.Type<RevealContentHandler<?>> slot) {
        super(eventBus, view, proxy, slot);
    }

    public AbstractStoriesPresenter(EventBus eventBus, V view, P proxy, RevealType revealType,
            GwtEvent.Type<RevealContentHandler<?>> slot) {
        super(eventBus, view, proxy, revealType, slot);
    }

    @Override
    protected final void onReset() {
        super.onReset();

        PlaceRequest historyPlace = placeManager.getCurrentHistoryPlace();
        PlaceRequest placeRequest = placeManager.getCurrentPlaceRequest();

        if (historyPlace == null || placeRequest.matchesNameToken(historyPlace.getNameToken())) {
            onReset(placeRequest);
        }
    }

    protected void onReset(PlaceRequest placeRequest) {
    }
}
