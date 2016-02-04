package org.consumersunion.stories.common.client.util;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.event.UserChangedEvent;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.User;

import com.google.web.bindery.event.shared.EventBus;

public class CurrentUser implements UserChangedEvent.UserChangedHandler {
    private User user;
    private Profile currentProfile;

    @Inject
    CurrentUser(EventBus eventBus) {
        eventBus.addHandler(UserChangedEvent.TYPE, this);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public int getCurrentProfileId() {
        return getCurrentProfile().getId();
    }

    public Profile getCurrentProfile() {
        return currentProfile;
    }

    public void setCurrentProfile(Profile currentProfile) {
        this.currentProfile = currentProfile;
    }

    @Override
    public void onUserChanged(UserChangedEvent event) {
        setUser(event.getUser());
    }
}
