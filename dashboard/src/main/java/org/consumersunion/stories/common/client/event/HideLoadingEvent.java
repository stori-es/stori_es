package org.consumersunion.stories.common.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class HideLoadingEvent extends GwtEvent<HideLoadingEvent.HideLoadingHandler> {
    public interface HideLoadingHandler extends EventHandler {
        void onHideLoading(HideLoadingEvent event);
    }

    public static final Type<HideLoadingHandler> TYPE = new Type<HideLoadingHandler>();

    private HideLoadingEvent() {
    }

    public static void fire(HasHandlers source) {
        source.fireEvent(new HideLoadingEvent());
    }

    public static Type<HideLoadingHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<HideLoadingHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(HideLoadingHandler handler) {
        handler.onHideLoading(this);
    }
}
