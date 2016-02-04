package org.consumersunion.stories.server.amazon;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.server.amazon.model.SesNotification;
import org.consumersunion.stories.server.business_logic.EmailService;
import org.consumersunion.stories.server.business_logic.EmailServiceImpl;
import org.consumersunion.stories.server.email.EmailData;
import org.consumersunion.stories.server.spring.AmazonConfigurator;
import org.consumersunion.stories.server.annotations.Amazon;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.name.Names;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class SqsNotificationProcessorImplTest {
    // We were getting intermittent failures at 30. Average at the moment (2015-01-07) is about 45 for both tests.
    private static final long TIMEOUT = 60000L;

    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            AmazonConfigurator amazonConfigurator = new AmazonConfigurator();

            bind(ObjectMapper.class).annotatedWith(Amazon.class).toInstance(amazonConfigurator.amazonObjectMapper());
            bind(AmazonSQS.class).toInstance(amazonConfigurator.amazonSqs());
            bind(EmailService.class).to(EmailServiceImpl.class);
            bind(String.class).annotatedWith(Names.named("sqsComplaintQueue")).toInstance(
                    amazonConfigurator.amazonSqsComplaintQueue());
            bind(String.class).annotatedWith(Names.named("sqsBounceQueue")).toInstance(
                    amazonConfigurator.amazonSqsBounceQueue());
        }
    }

    @Inject
    SqsNotificationProcessorImpl notificationProcessor;

    @Captor
    private ArgumentCaptor<List<SesNotification>> sesNotificationsCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /* http://docs.aws.amazon.com/ses/latest/DeveloperGuide/mailbox-simulator.html */
    @Test(timeout = TIMEOUT)
    public void processBouncedEmails(
            EmailService emailService,
            SesNotificationProcessor sesNotificationProcessor) throws Exception {
        emailService.sendEmail(
                new EmailData("Bounced email", "Bounced email", "bounce@simulator.amazonses.com", "Bounce"));

        int processed = 0;
        for (int i = 0; processed == 0 && i < 5; i += 1) {
            sleep(2000);
            processed = notificationProcessor.processBouncedEmails();
        }

        verify(sesNotificationProcessor, atLeastOnce()).process(sesNotificationsCaptor.capture());
        assertFalse(sesNotificationsCaptor.getValue().isEmpty());
    }

    @Test(timeout = TIMEOUT)
    public void processComplaintsEmails(
            EmailService emailService,
            SesNotificationProcessor sesNotificationProcessor) throws Exception {
        emailService.sendEmail(
                new EmailData("Complaint email", "Complaint email", "complaint@simulator.amazonses.com", "Complaint"));

        int processed = 0;
        for (int i = 0; processed == 0 && i < 5; i += 1) {
            sleep(2000);
            processed = notificationProcessor.processComplaintsEmails();
        }

        verify(sesNotificationProcessor, atLeastOnce()).process(sesNotificationsCaptor.capture());
        assertFalse(sesNotificationsCaptor.getValue().isEmpty());
    }
}
