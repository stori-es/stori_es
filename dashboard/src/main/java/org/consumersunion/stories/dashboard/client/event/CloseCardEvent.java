package org.consumersunion.stories.dashboard.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

/**
 * Event used to notify the container that the detail section should be hidden (related to Cards open/close actions)
 */
public class CloseCardEvent extends GwtEvent<CloseCardEvent.CloseCardHandler> {
    public interface CloseCardHandler extends EventHandler {
        void onCardClosed(CloseCardEvent event);
    }

    public static final Type<CloseCardHandler> TYPE = new Type<CloseCardHandler>();

    private final Object slot;
    private final PresenterWidget presenterWidget;
    private final PlaceRequest placeRequest;
    private final Object object;

    private CloseCardEvent(Object slot, PresenterWidget presenterWidget, PlaceRequest placeRequest, Object object) {
        this.slot = slot;
        this.presenterWidget = presenterWidget;
        this.placeRequest = placeRequest;
        this.object = object;
    }

    public static void fire(HasHandlers source,
            Object slot,
            PresenterWidget presenterWidget,
            PlaceRequest placeRequest,
            Object object) {
        source.fireEvent(new CloseCardEvent(slot, presenterWidget, placeRequest, object));
    }

    public PlaceRequest getPlaceRequest() {
        return placeRequest;
    }

    public PresenterWidget getPresenterWidget() {
        return presenterWidget;
    }

    public Object getObject() {
        return object;
    }

    public Object getSlot() {
        return slot;
    }

    @Override
    public Type<CloseCardHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CloseCardHandler handler) {
        handler.onCardClosed(this);
    }
}
