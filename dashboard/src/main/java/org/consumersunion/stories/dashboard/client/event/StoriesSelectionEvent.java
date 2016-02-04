package org.consumersunion.stories.dashboard.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class StoriesSelectionEvent extends GwtEvent<StoriesSelectionEvent.StoriesSelectionHandler> {
    public interface StoriesSelectionHandler extends EventHandler {
        void onStoriesSelected(StoriesSelectionEvent event);
    }

    public static final Type<StoriesSelectionHandler> TYPE = new Type<StoriesSelectionHandler>();

    private final int numberOfStories;
    private final boolean selecting;
    private final boolean withCheckboxes;

    private StoriesSelectionEvent(boolean selecting) {
        this(selecting, 0, false);
    }

    private StoriesSelectionEvent(int numberOfStories, boolean withCheckboxes) {
        this(true, numberOfStories, withCheckboxes);
    }

    private StoriesSelectionEvent(boolean selecting, int numberOfStories, boolean withCheckboxes) {
        this.selecting = selecting;
        this.numberOfStories = numberOfStories;
        this.withCheckboxes = withCheckboxes;
    }

    public static void fire(HasHandlers source, boolean selecting) {
        source.fireEvent(new StoriesSelectionEvent(selecting));
    }

    public static void fire(HasHandlers source, int numberOfStories, boolean withCheckboxes) {
        source.fireEvent(new StoriesSelectionEvent(numberOfStories, withCheckboxes));
    }

    public int getNumberOfStories() {
        return numberOfStories;
    }

    public boolean isSelecting() {
        return selecting;
    }

    public boolean isWithCheckboxes() {
        return withCheckboxes;
    }

    @Override
    public Type<StoriesSelectionHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(StoriesSelectionHandler handler) {
        handler.onStoriesSelected(this);
    }
}
