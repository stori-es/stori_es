package org.consumersunion.stories.server.business_logic;

import org.consumersunion.stories.common.shared.model.NotificationTrigger;

public interface NotificationService {
    void subscribe(int target, NotificationTrigger event);

    void unsubscribe(int target, NotificationTrigger event);

    void cleanupSubscriptions();
}
