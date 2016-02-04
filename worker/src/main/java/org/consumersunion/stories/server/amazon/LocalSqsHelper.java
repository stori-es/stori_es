package org.consumersunion.stories.server.amazon;

import java.io.IOException;

import org.consumersunion.stories.server.WorkerTierResource;
import org.consumersunion.stories.server.spring.AmazonConfigurator;
import org.consumersunion.stories.common.shared.dto.tasks.Task;
import org.elasticmq.rest.sqs.SQSRestServerBuilder;
import org.springframework.scheduling.annotation.Scheduled;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LocalSqsHelper implements SqsHelper {
    private final AmazonSQS sqsClient;
    private final ObjectMapper objectMapper;
    private final WorkerTierResource workerTierResource;
    private final String queueUrl;

    public LocalSqsHelper(
            AmazonSQS sqsClient,
            ObjectMapper objectMapper,
            WorkerTierResource workerTierResource) {
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
        this.workerTierResource = workerTierResource;

        SQSRestServerBuilder.withPort(AmazonConfigurator.SQS_HELPER_PORT)
                .withInterface("localhost")
                .start();

        this.queueUrl = sqsClient.createQueue(AmazonConfigurator.WORKER_QUEUE_SUFFIX).getQueueUrl();
    }

    @Scheduled(fixedDelay = 5000)
    public void pullQueue() {
        ReceiveMessageRequest messageRequest = new ReceiveMessageRequest(queueUrl);
        messageRequest.setWaitTimeSeconds(10);

        ReceiveMessageResult messageResult = sqsClient.receiveMessage(messageRequest);
        for (Message message : messageResult.getMessages()) {
            try {
                Task task = objectMapper.readValue(message.getBody(), Task.class);
                workerTierResource.onMessage(task);
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            sqsClient.deleteMessage(queueUrl, message.getReceiptHandle());
        }
    }
}
