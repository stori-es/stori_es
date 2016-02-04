package org.consumersunion.stories.server.spring;

import org.consumersunion.stories.server.WorkerTierResource;
import org.consumersunion.stories.server.amazon.LocalSqsHelper;
import org.consumersunion.stories.server.amazon.SqsHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ComponentScan("org.consumersunion.stories.server")
@EnableScheduling
public class WebApplicationConfiguration {
    @Bean
    public SqsHelper sqsHelper(
            AmazonSQS amazonSqs,
            ObjectMapper objectMapper,
            WorkerTierResource workerTierResource) {
        if ("TRUE".equals(System.getProperty("sys.localSQS", "FALSE"))) {
            return new LocalSqsHelper(amazonSqs, objectMapper, workerTierResource);
        }

        return new SqsHelper() {};
    }
}
