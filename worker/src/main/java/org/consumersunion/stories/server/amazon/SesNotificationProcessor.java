package org.consumersunion.stories.server.amazon;

import java.util.List;

import org.consumersunion.stories.server.amazon.model.SesNotification;

public interface SesNotificationProcessor {
    int process(List<SesNotification> notifications);
}
