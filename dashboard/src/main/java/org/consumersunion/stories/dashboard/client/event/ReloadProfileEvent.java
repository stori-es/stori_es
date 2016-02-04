package org.consumersunion.stories.dashboard.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ReloadProfileEvent extends GwtEvent<ReloadProfileEvent.ProfileHandler> {
    public interface ProfileHandler extends EventHandler {
        void onProfileReload(ReloadProfileEvent event);
    }

    public static final Type<ProfileHandler> TYPE = new Type<ProfileHandler>();

    private final Integer profileId;

    private ReloadProfileEvent(int profileId) {
        this.profileId = profileId;
    }

    public static void fire(HasHandlers source, Integer profileId) {
        source.fireEvent(new ReloadProfileEvent(profileId));
    }

    @Override
    public Type<ProfileHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ProfileHandler handler) {
        handler.onProfileReload(this);
    }

    public int getProfileId() {
        return profileId;
    }
}
