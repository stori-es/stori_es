package org.consumersunion.stories.server.amazon;

import java.util.List;

import com.amazonaws.services.sqs.model.Message;

public interface SqsNotificationProcessor {
    int processEmailNotifications(List<Message> messages);
}
