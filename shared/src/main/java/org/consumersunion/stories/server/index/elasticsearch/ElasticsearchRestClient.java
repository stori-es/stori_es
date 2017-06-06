package org.consumersunion.stories.server.index.elasticsearch;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Future;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Async;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
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

    private final HttpRequestFactory httpRequestFactory;
    private final AwsSigner awsSigner;

    @Inject
    public ElasticsearchRestClient(
            HttpRequestFactory httpRequestFactory,
            AwsSigner awsSigner) {
        this.httpRequestFactory = httpRequestFactory;
        this.awsSigner = awsSigner;
    }

    public HttpResponse performRequest(String methodName, String path, String body) throws IOException {
        Map<String, String> signedHeaders = awsSigner.getSignedHeaders(path, methodName, params(path),
                ImmutableMap.<String, String>of(), payload(body));

        HttpResponse response = createRequest(methodName, path)
                .body(new StringEntity(body, Charsets.UTF_8))
                .setHeaders(headers(signedHeaders))
                .execute()
                .returnResponse();

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode >= 200 && statusCode <= 300) {
            return response;
        }

        throw new IOException(IOUtils.toString(response.getEntity().getContent()));
    }

    public HttpResponse performRequest(String methodName, String path) throws IOException {
        Map<String, String> signedHeaders = awsSigner.getSignedHeaders(path, methodName, params(path),
                ImmutableMap.<String, String>of(), Optional.<byte[]>absent());

        return createRequest(methodName, path)
                .setHeaders(headers(signedHeaders))
                .execute()
                .returnResponse();
    }

    public Future<Content> performRequestAsync(String methodName, String path, String body,
            FutureCallback<Content> listener)
            throws IOException {
        Map<String, String> signedHeaders = awsSigner.getSignedHeaders(path, methodName, params(path),
                ImmutableMap.<String, String>of(), payload(body));

        Request request = createRequest(methodName, path)
                .body(new StringEntity(body, Charsets.UTF_8))
                .setHeaders(headers(signedHeaders));

        return Async.newInstance().execute(request, listener);
    }

    private Request createRequest(String methodName, String path) {
        return httpRequestFactory.createRequest(methodName, path);
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
