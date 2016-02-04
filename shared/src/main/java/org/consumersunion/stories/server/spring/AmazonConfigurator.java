package org.consumersunion.stories.server.spring;

import java.util.List;

import org.consumersunion.stories.server.annotations.Amazon;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;

@Configuration
public class AmazonConfigurator {
    public static final int SQS_HELPER_PORT = 9324;
    public static final String LOCAL_SQS_SERVICE = "sqs";
    public static final String WORKER_QUEUE_SUFFIX = "worker-queue";

    @Bean
    public AmazonSQS amazonSqs() {
        if (isLocalWorker()) {
            AmazonSQSClient client = new AmazonSQSClient(new BasicAWSCredentials("x", "x"));
            client.setEndpoint("http://localhost:" + SQS_HELPER_PORT, LOCAL_SQS_SERVICE, "");

            return client;
        }

        return new AmazonSQSClient();
    }

    @Bean
    public AmazonS3 amazonS3(@Amazon String amazonS3Bucket) {
        AmazonS3 s3 = new AmazonS3Client();

        if (!"TRUE".equals(System.getProperty("sys.noS3"))) {
            List<Bucket> buckets = s3.listBuckets();
            boolean bucketExists = false;
            for (Bucket bucket : buckets) {
                if (bucket.getName().equals(amazonS3Bucket)) {
                    bucketExists = true;
                    break;
                }
            }

            if (!bucketExists) {
                s3.createBucket(
                        new CreateBucketRequest(amazonS3Bucket).withCannedAcl(CannedAccessControlList.PublicRead));
            }
        }

        return s3;
    }

    @Bean
    @Amazon
    public String amazonS3Bucket() {
        return "stories-uploads-" + getEnvironment();
    }

    @Bean
    @Amazon
    public String amazonSqsBounceQueue() {
        return "ses-bounce-" + getEnvironment() + "-queue";
    }

    @Bean
    @Amazon
    public String amazonSqsComplaintQueue() {
        return "ses-complaint-" + getEnvironment() + "-queue";
    }

    @Bean
    @Amazon
    public String amazonSqsWorkerQueue() {
        if (isLocalWorker()) {
            return "http://localhost:" + SQS_HELPER_PORT + "/queue/" + WORKER_QUEUE_SUFFIX;
        }
        else {
        	return getEnvironment() + "-" + WORKER_QUEUE_SUFFIX;
        }
    }

    @Bean
    @Amazon
    public ObjectMapper amazonObjectMapper() {
        return new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private String getEnvironment() {
        String environment = System.getProperty("PARAM2", "devel");
        // PARAM2 can be "".
        if (Strings.isNullOrEmpty(environment)) {
            environment = "devel";
        }
        return environment;
    }

    private boolean isLocalWorker() {
        return "TRUE".equals(System.getProperty("sys.localSQS", "FALSE"));
    }
}
