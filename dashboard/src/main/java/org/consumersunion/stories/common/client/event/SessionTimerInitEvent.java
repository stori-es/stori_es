package org.consumersunion.stories.common.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class SessionTimerInitEvent extends GwtEvent<SessionTimerInitEvent.SessionTimerInitHandler> {
    public interface SessionTimerInitHandler extends EventHandler {
        void onSessionTimerInit(SessionTimerInitEvent event);
    }

    public static final Type<SessionTimerInitHandler> TYPE = new Type<SessionTimerInitHandler>();

    private SessionTimerInitEvent() {
    }

    public static void fire(HasHandlers source) {
        source.fireEvent(new SessionTimerInitEvent());
    }

    @Override
    public Type<SessionTimerInitHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SessionTimerInitHandler handler) {
        handler.onSessionTimerInit(this);
    }
}
