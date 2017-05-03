package org.consumersunion.stories.server.index.elasticsearch;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.rest.RestRequest.Method;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;

import static com.google.common.base.Charsets.UTF_8;

@Component
public class ElasticsearchRestClient {
    private static final Splitter SPLITTER = Splitter.on('&').trimResults().omitEmptyStrings();

    private final Provider<RestClient> restClientProvider;
    private final AwsSigner awsSigner;

    @Inject
    public ElasticsearchRestClient(
            Provider<RestClient> restClientProvider,
            AwsSigner awsSigner) {
        this.restClientProvider = restClientProvider;
        this.awsSigner = awsSigner;
    }

    public Response performRequest(Method method, String path, String body) throws IOException {
        String methodName = method.name();
        Map<String, String> signedHeaders = awsSigner.getSignedHeaders(path, methodName, params(path),
                ImmutableMap.<String, String>of(), payload(body));

        return restClientProvider.get()
                .performRequest(methodName, path, Collections.<String, String>emptyMap(),
                        new NStringEntity(body, Charsets.UTF_8),
                        headers(signedHeaders));
    }

    public Response performRequest(Method method, String path) throws IOException {
        String methodName = method.name();
        Map<String, String> signedHeaders = awsSigner.getSignedHeaders(path, methodName, params(path),
                ImmutableMap.<String, String>of(), Optional.<byte[]>absent());

        return restClientProvider.get()
                .performRequest(methodName, path, Collections.<String, String>emptyMap(), headers(signedHeaders));
    }

    public void performRequestAsync(Method method, String path, String body, ResponseListener responseListener)
            throws IOException {
        String methodName = method.name();
        Map<String, String> signedHeaders = awsSigner.getSignedHeaders(path, methodName, params(path),
                ImmutableMap.<String, String>of(), payload(body));

        restClientProvider.get()
                .performRequestAsync(methodName, path, Collections.<String, String>emptyMap(),
                        new NStringEntity(body, UTF_8),
                        responseListener, headers(signedHeaders));
    }

    private Header[] headers(Map<String, String> signedHeaders) {
        return FluentIterable.from(signedHeaders.entrySet())
                .transform(new Function<Map.Entry<String, String>, Header>() {
                    @Override
                    public Header apply(Map.Entry<String, String> input) {
                        return new BasicHeader(input.getKey(), input.getValue());
                    }
                })
                .toArray(Header.class);
    }

    private Multimap<String, String> params(String path) {
        ImmutableListMultimap.Builder<String, String> queryParams = ImmutableListMultimap.builder();

        int queryIndex = path.indexOf("?");
        String query = queryIndex >= 0 ? path.substring(queryIndex + 1) : null;

        if (!Strings.isNullOrEmpty(query)) {
            for (String pair : SPLITTER.split(query)) {
                int index = pair.indexOf('=');
                if (index > 0 && pair.length() > index + 1) {
                    String key = pair.substring(0, index);
                    String value = pair.substring(index + 1);
                    queryParams.put(key, value);
                } else if (pair.length() > 0) {
                    queryParams.put(pair, "");
                }
            }
        }

        return queryParams.build();
    }

    private Optional<byte[]> payload(String content) {
        return Optional.of(content).transform(new Function<String, byte[]>() {
            @Override
            public byte[] apply(String content) {
                return content.getBytes(UTF_8);
            }
        });
    }
}
