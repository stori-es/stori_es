package org.consumersunion.stories.server.email;

import java.util.List;

import org.consumersunion.stories.server.notification_channel.NotificationEvent;
import org.consumersunion.stories.server.notification_channel.SubscriptionSummary;

public interface EmailDataFactory {
    List<EmailData> create(NotificationEvent event, List<SubscriptionSummary> subscriptions);
}
