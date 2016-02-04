package org.consumersunion.stories.common.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class RedrawEvent<T> extends GwtEvent<RedrawEvent.RedrawHandler> {
    public interface RedrawHandler<T> extends EventHandler {
        void onRedraw(RedrawEvent<T> event);
    }

    public static final Type<RedrawHandler> TYPE = new Type<RedrawHandler>();

    private final T data;

    private RedrawEvent(T data) {
        this.data = data;
    }

    public static void fire(HasHandlers source) {
        source.fireEvent(new RedrawEvent(null));
    }

    public static <T> void fire(HasHandlers source, T data) {
        source.fireEvent(new RedrawEvent<T>(data));
    }

    public T getData() {
        return data;
    }

    @Override
    public Type<RedrawHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RedrawHandler handler) {
        handler.onRedraw(this);
    }
}
