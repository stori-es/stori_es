package org.consumersunion.stories.common.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ShowLoadingEvent extends GwtEvent<ShowLoadingEvent.ShowLoadingHandler> {
    public interface ShowLoadingHandler extends EventHandler {
        void onShowLoading(ShowLoadingEvent event);
    }

    public static final Type<ShowLoadingHandler> TYPE = new Type<ShowLoadingHandler>();

    private final boolean fullScreen;

    private ShowLoadingEvent() {
        this(true);
    }

    private ShowLoadingEvent(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    public static void fire(HasHandlers source) {
        source.fireEvent(new ShowLoadingEvent());
    }

    /**
     * Fire an event to show a Loading modal
     *
     * @param source
     * @param fullScreen If it should be shown over everything (Default: true)
     */
    public static void fire(HasHandlers source, boolean fullScreen) {
        source.fireEvent(new ShowLoadingEvent(fullScreen));
    }

    public static Type<ShowLoadingHandler> getType() {
        return TYPE;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    @Override
    public Type<ShowLoadingHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ShowLoadingHandler handler) {
        handler.onShowLoading(this);
    }
}
