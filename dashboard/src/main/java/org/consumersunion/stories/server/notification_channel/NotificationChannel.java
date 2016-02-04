package org.consumersunion.stories.server.notification_channel;

import java.util.List;

import org.consumersunion.stories.common.shared.model.NotificationTrigger;

/**
 * A <code>NotificationChannel</code> handles converting one or many {@link SubscriptionSummary} to a final message
 * and delivers it to a* recipient through a delivery channel, such as email or SMS. Each
 * <code>NotificationChannel</code> object is built to work with a particular {@link NotificationTrigger} and they each
 * work independently in order that the concrete channel may implement batch sending if necessary.
 */
public interface NotificationChannel {
    boolean canHandle(NotificationTrigger trigger);

    void handleAll(NotificationEvent event, List<SubscriptionSummary> subscriptions);

    void flush();
}
