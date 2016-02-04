package org.consumersunion.stories.server.notification_channel;

import org.consumersunion.stories.common.shared.model.NotificationTrigger;
import org.consumersunion.stories.common.shared.model.SystemEntity;

public abstract class NotificationEvent {
    private final SystemEntity systemEntity;
    private final NotificationTrigger eventType;

    public NotificationEvent(
            SystemEntity systemEntity,
            NotificationTrigger eventType) {
        this.systemEntity = systemEntity;
        this.eventType = eventType;
    }

    public NotificationTrigger getEventType() {
        return eventType;
    }

    public SystemEntity getSystemEntity() {
        return systemEntity;
    }
}
