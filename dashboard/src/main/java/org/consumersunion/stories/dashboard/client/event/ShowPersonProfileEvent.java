package org.consumersunion.stories.dashboard.client.event;

import org.consumersunion.stories.common.shared.model.Profile;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ShowPersonProfileEvent extends GwtEvent<ShowPersonProfileEvent.ShowPersonProfileHandler> {
    public interface ShowPersonProfileHandler extends EventHandler {
        void onShowPersonProfile(ShowPersonProfileEvent event);
    }

    public static final Type<ShowPersonProfileHandler> TYPE = new Type<ShowPersonProfileHandler>();

    private final Profile profile;

    private ShowPersonProfileEvent(Profile profile) {
        this.profile = profile;
    }

    public static void fire(HasHandlers source, Profile profile) {
        source.fireEvent(new ShowPersonProfileEvent(profile));
    }

    public Profile getProfile() {
        return profile;
    }

    @Override
    public Type<ShowPersonProfileHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ShowPersonProfileHandler handler) {
        handler.onShowPersonProfile(this);
    }
}
