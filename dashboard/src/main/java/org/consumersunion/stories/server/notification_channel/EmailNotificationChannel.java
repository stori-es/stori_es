package org.consumersunion.stories.server.notification_channel;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.NotificationTrigger;
import org.consumersunion.stories.server.business_logic.EmailService;
import org.consumersunion.stories.server.email.EmailData;
import org.consumersunion.stories.server.email.EmailDataFactory;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

@Component
public class EmailNotificationChannel implements NotificationChannel {
    private final EmailService emailService;
    private final EmailDataFactory emailDataFactory;
    private final Map<NotificationEvent, List<EmailData>> emailData;

    @Inject
    EmailNotificationChannel(
            EmailService emailService,
            EmailDataFactory emailDataFactory) {
        this.emailService = emailService;
        this.emailDataFactory = emailDataFactory;
        this.emailData = Maps.synchronizedBiMap(HashBiMap.<NotificationEvent, List<EmailData>>create());
    }

    @Override
    public boolean canHandle(NotificationTrigger trigger) {
        return trigger == NotificationTrigger.STORY_ADDED;
    }

    @Override
    public void handleAll(NotificationEvent event, List<SubscriptionSummary> subscriptions) {
        synchronized (this.emailData) {
            generateEmailData(event, subscriptions);
        }
    }

    @Override
    public void flush() {
        Map<NotificationEvent, List<EmailData>> emailDataToFlush;

        synchronized (this.emailData) {
            emailDataToFlush = Maps.newHashMap(emailData);
            emailData.clear();
        }

        if (!emailDataToFlush.isEmpty()) {
            doFlush(emailDataToFlush);
        }
    }

    private void generateEmailData(NotificationEvent event, List<SubscriptionSummary> subscriptions) {
        List<EmailData> emailDatas = emailDataFactory.create(event, subscriptions);
        emailData.put(event, emailDatas);
    }

    private void doFlush(Map<NotificationEvent, List<EmailData>> emailDatas) {
        Preconditions.checkArgument(!emailDatas.isEmpty());

        for (Map.Entry<NotificationEvent, List<EmailData>> entry : emailDatas.entrySet()) {
            for (EmailData emailData : entry.getValue()) {
                if (!Strings.isNullOrEmpty(emailData.getEmail())) {
                    emailService.sendEmail(emailData);
                }
            }
        }
    }
}
