package org.consumersunion.stories.dashboard.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class StoryChangedEvent extends GwtEvent<StoryChangedEvent.StoryChangedHandler> {

    public interface StoryChangedHandler extends EventHandler {
        void onStoryChanged(StoryChangedEvent event);
    }

    public static final Type<StoryChangedHandler> TYPE = new Type<StoryChangedHandler>();

    private StoryChangedEvent() {
    }

    public static void fire(HasHandlers source) {
        source.fireEvent(new StoryChangedEvent());
    }

    @Override
    public Type<StoryChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(StoryChangedHandler handler) {
        handler.onStoryChanged(this);
    }
}
