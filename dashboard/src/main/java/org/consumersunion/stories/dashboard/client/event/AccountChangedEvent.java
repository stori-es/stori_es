package org.consumersunion.stories.dashboard.client.event;

import org.consumersunion.stories.common.shared.model.User;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class AccountChangedEvent extends GwtEvent<AccountChangedEvent.AccountChangedHandler> {
    public interface AccountChangedHandler extends EventHandler {
        void onAccountChanged(AccountChangedEvent event);
    }

    public static final Type<AccountChangedHandler> TYPE = new Type<AccountChangedHandler>();

    private final User user;

    private AccountChangedEvent(User user) {
        this.user = user;
    }

    public static void fire(HasHandlers source, User user) {
        source.fireEvent(new AccountChangedEvent(user));
    }

    @Override
    public Type<AccountChangedHandler> getAssociatedType() {
        return TYPE;
    }

    public User getUser() {
        return user;
    }

    @Override
    protected void dispatch(AccountChangedHandler handler) {
        handler.onAccountChanged(this);
    }
}
