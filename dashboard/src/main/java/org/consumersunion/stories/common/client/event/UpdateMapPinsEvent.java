package org.consumersunion.stories.common.client.event;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class UpdateMapPinsEvent extends GwtEvent<UpdateMapPinsEvent.UpdateMapPinsHandler> {
    public interface UpdateMapPinsHandler extends EventHandler {
        void onStoriesLoaded(UpdateMapPinsEvent event);
    }

    public static final Type<UpdateMapPinsHandler> TYPE = new Type<UpdateMapPinsHandler>();

    private final List<Integer> storyIds;

    private UpdateMapPinsEvent(List<Integer> storyIds) {
        this.storyIds = storyIds;
    }

    public static void fire(HasHandlers source, List<Integer> storyIds) {
        source.fireEvent(new UpdateMapPinsEvent(storyIds));
    }

    public List<Integer> getStoryIds() {
        return storyIds;
    }

    @Override
    public Type<UpdateMapPinsHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UpdateMapPinsHandler handler) {
        handler.onStoriesLoaded(this);
    }
}
