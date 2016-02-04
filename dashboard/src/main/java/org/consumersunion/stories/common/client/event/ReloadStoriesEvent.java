package org.consumersunion.stories.common.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ReloadStoriesEvent extends GwtEvent<ReloadStoriesEvent.ReloadStoriesHandler> {
    public interface ReloadStoriesHandler extends EventHandler {
        void onReloadStories(ReloadStoriesEvent event);
    }

    public static final Type<ReloadStoriesHandler> TYPE = new Type<ReloadStoriesHandler>();

    private ReloadStoriesEvent() {
    }

    public static void fire(HasHandlers source) {
        source.fireEvent(new ReloadStoriesEvent());
    }

    @Override
    public Type<ReloadStoriesHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ReloadStoriesHandler handler) {
        handler.onReloadStories(this);
    }
}
