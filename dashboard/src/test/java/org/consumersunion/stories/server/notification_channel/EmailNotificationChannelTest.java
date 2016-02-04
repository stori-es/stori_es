package org.consumersunion.stories.server.notification_channel;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.Subscription;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.server.business_logic.EmailService;
import org.consumersunion.stories.server.email.EmailData;
import org.consumersunion.stories.server.email.EmailDataFactory;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.common.collect.Lists;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class EmailNotificationChannelTest {
    @Inject
    private EmailNotificationChannel channel;
    @Inject
    private EmailDataFactory emailDataFactory;
    @Inject
    private EmailService emailService;

    @Test
    public void flush_noSubscriptions_doesNothing() {
        channel = new EmailNotificationChannel(emailService, emailDataFactory);

        channel.flush();

        verify(emailService, never()).sendEmail(any(EmailData.class));
    }

    @Test
    public void flush_invalidSubscriptions_doesNothing() {
        SubscriptionSummary subscription1 = SubscriptionSummary.builder()
                .profileSummary(new ProfileSummary())
                .subscription(Subscription.builder().withActive(true).build())
                .build();
        SubscriptionSummary subscription2 = SubscriptionSummary.builder()
                .profileSummary(new ProfileSummary(null, "email@domain.com", null, null, null))
                .subscription(Subscription.builder().withActive(false).build())
                .build();

        channel.handleAll(Mockito.mock(NotificationEvent.class), Lists.newArrayList(subscription1, subscription2));
        channel.flush();

        verify(emailService, never()).sendEmail(any(EmailData.class));
    }

    @Test
    public void flush_validSubscriptions_sendsMail(EmailDataFactory emailDataFactory) {
        EmailData emailData1 = new EmailData("Some subject", "Some body", "email1@domain.com", "email1");
        EmailData emailData2 = new EmailData("Some subject", "Some body", "email2@domain.com", "email1");
        given(emailDataFactory.create(any(NotificationEvent.class), anyListOf(SubscriptionSummary.class)))
                .willReturn(Lists.newArrayList(emailData1, emailData2));

        String email1 = "email1@domain.com";
        String name1 = "Bob";
        Profile profile1 = new Profile();
        profile1.setGivenName(name1);

        String email2 = "email2@domain.com";
        String name2 = "Susan";
        Profile profile2 = new Profile();
        profile2.setGivenName(name2);

        SubscriptionSummary subscription1 = buildValidSubscription(email1);
        SubscriptionSummary subscription2 = buildValidSubscription(email2);
        List<SubscriptionSummary> subscriptions = Lists.newArrayList(subscription1, subscription2);

        channel.handleAll(Mockito.mock(StoriesAddedEvent.class), subscriptions);
        channel.flush();

        verify(emailService).sendEmail(emailData1);
        verify(emailService).sendEmail(emailData2);
    }

    private SubscriptionSummary buildValidSubscription(String email) {
        Profile profile = new Profile();
        profile.setGivenName(email);

        return SubscriptionSummary.builder()
                .systemEntity(new SystemEntity(1, 1))
                .profileSummary(new ProfileSummary(profile, email, null, null, null))
                .subscription(Subscription.builder().withActive(true).build())
                .build();
    }
}
