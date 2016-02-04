package org.consumersunion.stories.server.notification_channel;

import com.google.common.eventbus.Subscribe;

public interface NotificationEventHandler {
    @Subscribe
    void onNotification(NotificationEvent event);
}
