package org.consumersunion.stories.common.client.util;

import org.consumersunion.stories.common.client.service.response.Response;

public abstract class PublicResponseHandler<R extends Response> extends ResponseHandler<R> {
    public PublicResponseHandler() {
        super(false);
    }
}
