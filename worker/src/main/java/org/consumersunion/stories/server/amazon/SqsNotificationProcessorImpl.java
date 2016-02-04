package org.consumersunion.stories.server.amazon;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.util.GUID;
import org.consumersunion.stories.server.amazon.model.SesNotification;
import org.consumersunion.stories.server.amazon.model.SnsNotification;
import org.consumersunion.stories.server.annotations.Amazon;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequest;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.DeleteMessageBatchResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.inject.name.Named;

@Component
public class SqsNotificationProcessorImpl implements SqsNotificationProcessor {
    private static final Integer AMAZON_SQS_MESSAGES_COUNT = 10;

    private final ObjectMapper objectMapper;
    private final AmazonSQS amazonSqs;
    private final String amazonSqsBounceQueue;
    private final String amazonSqsComplaintQueue;
    private final SesNotificationProcessor sesNotificationProcessor;

    @Inject
    SqsNotificationProcessorImpl(
            @Amazon ObjectMapper objectMapper,
            AmazonSQS amazonSqs,
            @Named("sqsBounceQueue") String amazonSqsBounceQueue,
            @Named("sqsComplaintQueue") String amazonSqsComplaintQueue,
            SesNotificationProcessor sesNotificationProcessor) {
        this.objectMapper = objectMapper;
        this.amazonSqs = amazonSqs;
        this.amazonSqsBounceQueue = amazonSqsBounceQueue;
        this.amazonSqsComplaintQueue = amazonSqsComplaintQueue;
        this.sesNotificationProcessor = sesNotificationProcessor;
    }

    @Override
    public int processEmailNotifications(List<Message> messages) {
        List<SesNotification> sesNotifications = FluentIterable.from(messages)
                .transform(new Function<Message, SnsNotification>() {
                    @Override
                    public SnsNotification apply(Message input) {
                        return readObject(SnsNotification.class, input.getBody());
                    }
                }).filter(Predicates.notNull())
                .transform(new Function<SnsNotification, SesNotification>() {
                    @Override
                    public SesNotification apply(SnsNotification input) {
                        return readObject(SesNotification.class, input.getMessage());
                    }
                }).filter(Predicates.notNull())
                .toList();

        return sesNotificationProcessor.process(sesNotifications);
    }

    // Spring limits '@Schedule' to void methods, but getting the count of the processed emails is useful for testing
    // (at least), so we have to wrap the method.
    @Scheduled(cron = "0 0/30 * * * *")
    public void processBouncedEmailsScheduled() {
        processBouncedEmails();
    }

    @Scheduled(cron = "0 7/30 * * * *")
    public void processComplaintEmailsScheduled() {
        processComplaintsEmails();
    }

    public int processBouncedEmails() {
        return processEmailNotificationRequest(amazonSqsBounceQueue);
    }

    public int processComplaintsEmails() {
        return processEmailNotificationRequest(amazonSqsComplaintQueue);
    }

    private int processEmailNotificationRequest(String queueUrl) {
        final String noEmail = System.getProperty("NO_EMAIL");
        int processCount = 0;
        if (!"TRUE".equals(noEmail)) {
            int failedDeletes = 0;
            boolean hasNotifications;

            do {
                ReceiveMessageRequest messageRequest = new ReceiveMessageRequest(queueUrl);
                messageRequest.setMaxNumberOfMessages(AMAZON_SQS_MESSAGES_COUNT);

                ReceiveMessageResult messageResult = amazonSqs.receiveMessage(messageRequest);

                List<Message> messages = messageResult.getMessages();
                hasNotifications = !messages.isEmpty();

                if (hasNotifications) {
                    processCount += processEmailNotifications(messages);
                    failedDeletes = deleteMessages(queueUrl, messages);
                }
            } while (failedDeletes == 0 && hasNotifications);
        }

        return processCount;
    }

    private int deleteMessages(String queueUrl, List<Message> messages) {
        List<DeleteMessageBatchRequestEntry> entries = Lists.transform(messages,
                new Function<Message, DeleteMessageBatchRequestEntry>() {
                    @Override
                    public DeleteMessageBatchRequestEntry apply(Message input) {
                        return new DeleteMessageBatchRequestEntry()
                                .withId(GUID.get())
                                .withReceiptHandle(input.getReceiptHandle());
                    }
                });
        DeleteMessageBatchRequest deleteMessages = new DeleteMessageBatchRequest()
                .withQueueUrl(queueUrl)
                .withEntries(entries);

        DeleteMessageBatchResult deleteMessagesResult = amazonSqs.deleteMessageBatch(deleteMessages);

        return deleteMessagesResult.getFailed().size();
    }

    private <T> T readObject(Class<T> clazz, String content) {
        try {
            return objectMapper.reader(clazz).readValue(content);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
