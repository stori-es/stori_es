package org.consumersunion.stories.dashboard.client.event;

import org.consumersunion.stories.common.shared.model.Story;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class StorySelectedEvent extends GwtEvent<StorySelectedEvent.StorySelectedHandler> {
    public interface StorySelectedHandler extends EventHandler {
        void onStorySelected(StorySelectedEvent event);
    }

    public static final Type<StorySelectedHandler> TYPE = new Type<StorySelectedHandler>();

    private final Story story;

    private StorySelectedEvent(Story story) {
        this.story = story;
    }

    public static void fire(HasHandlers source, Story story) {
        source.fireEvent(new StorySelectedEvent(story));
    }

    public Story getStory() {
        return story;
    }

    @Override
    public Type<StorySelectedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(StorySelectedHandler handler) {
        handler.onStorySelected(this);
    }
}
