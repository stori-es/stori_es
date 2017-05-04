package org.consumersunion.stories.server.index.elasticsearch;

import org.apache.http.client.fluent.Request;

public class HttpRequestFactory {
    private static final int TIMEOUT = 60000;

    private final String baseUrl;

    public HttpRequestFactory(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Request createRequest(String method, String path) {
        String url = baseUrl + path;
        if ("GET".equals(method)) {
            return Request.Get(url).socketTimeout(TIMEOUT);
        } else if ("POST".equals(method)) {
            return Request.Post(url).socketTimeout(TIMEOUT);
        } else if ("PUT".equals(method)) {
            return Request.Put(url).socketTimeout(TIMEOUT);
        } else if ("DELETE".equals(method)) {
            return Request.Delete(url).socketTimeout(TIMEOUT);
        }

        throw new UnsupportedOperationException();
    }
}
