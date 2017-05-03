package org.consumersunion.stories.server.spring;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.consumersunion.stories.server.annotations.Indexer;
import org.consumersunion.stories.server.index.elasticsearch.AwsSigner;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;
import com.google.common.base.Supplier;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Configuration
public class ElasticsearchConfigurator {
    private static final String BASE_URL = "localhost";

    @Bean
    @Scope("prototype")
    public RestClient restClient() {
        return RestClient.builder(getBaseUrl())
                .setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
                    @Override
                    public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                        return requestConfigBuilder.setSocketTimeout(60000);
                    }
                })
                .setMaxRetryTimeoutMillis(120000)
                .build();
    }

    @Bean
    public AwsSigner awsSigner() {
        return new AwsSigner(DefaultAWSCredentialsProviderChain.getInstance(), getBaseUrl().getHostName(),
                getAwsRegion(), "es", new Supplier<LocalDateTime>() {
            @Override
            public LocalDateTime get() {
                return LocalDateTime.now(DateTimeZone.UTC);
            }
        });
    }

    @Bean
    @Indexer
    public ObjectMapper indexerObjectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .setSerializationInclusion(NON_NULL);
    }

    @Bean
    @Indexer
    public String indexName() {
        return System.getProperty("INDEX_NAME", null);
    }

    private HttpHost getBaseUrl() {
        String baseUrl = System.getProperty("ES_HOSTNAME", null);
        if (Strings.isNullOrEmpty(baseUrl)) {
            return new HttpHost(BASE_URL, 80, "http");
        } else {
            return new HttpHost(baseUrl, 443, "https");
        }
    }

    private String getAwsRegion() {
        return System.getProperty("ES_REGION", "us-east-1");
    }
}
