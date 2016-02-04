package org.consumersunion.stories.dashboard.client.event;

import org.consumersunion.stories.dashboard.client.util.NavigationBarElement;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class NavigationBarSelectionEvent extends GwtEvent<NavigationBarSelectionEvent.NavigationBarSelectionHandler> {
    public interface NavigationBarSelectionHandler extends EventHandler {
        void onNavigationBarSelection(NavigationBarSelectionEvent event);
    }

    public static final Type<NavigationBarSelectionHandler> TYPE = new Type<NavigationBarSelectionHandler>();

    private final NavigationBarElement element;

    private NavigationBarSelectionEvent(NavigationBarElement element) {
        this.element = element;
    }

    public static void fire(HasHandlers source, NavigationBarElement element) {
        source.fireEvent(new NavigationBarSelectionEvent(element));
    }

    public NavigationBarElement getElement() {
        return element;
    }

    @Override
    public Type<NavigationBarSelectionHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NavigationBarSelectionHandler handler) {
        handler.onNavigationBarSelection(this);
    }
}
