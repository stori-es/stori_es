package org.consumersunion.stories.common.client.event;

import org.consumersunion.stories.common.shared.model.User;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class UserChangedEvent extends GwtEvent<UserChangedEvent.UserChangedHandler> {
    public interface UserChangedHandler extends EventHandler {
        void onUserChanged(UserChangedEvent event);
    }

    public static final Type<UserChangedHandler> TYPE = new Type<UserChangedHandler>();

    private final User user;

    private UserChangedEvent(User user) {
        this.user = user;
    }

    public static void fire(HasHandlers source, User user) {
        source.fireEvent(new UserChangedEvent(user));
    }

    public User getUser() {
        return user;
    }

    @Override
    public Type<UserChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UserChangedHandler handler) {
        handler.onUserChanged(this);
    }
}
