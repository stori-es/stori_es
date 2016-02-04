package org.consumersunion.stories.dashboard.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class DisplayEvent<T> extends GwtEvent<DisplayEvent.DisplayHandler> {
    public interface DisplayHandler<T> extends EventHandler {
        void onDisplay(DisplayEvent<T> event);
    }

    public static final Type<DisplayHandler> TYPE = new Type<DisplayHandler>();

    private final T data;

    private DisplayEvent(T data) {
        this.data = data;
    }

    public static <T> void fire(HasHandlers source, T data) {
        source.fireEvent(new DisplayEvent<T>(data));
    }

    public T get() {
        return data;
    }

    @Override
    public Type<DisplayHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DisplayHandler handler) {
        handler.onDisplay(this);
    }
}
