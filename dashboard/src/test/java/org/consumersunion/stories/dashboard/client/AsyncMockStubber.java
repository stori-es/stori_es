package org.consumersunion.stories.dashboard.client;

import org.consumersunion.stories.common.client.service.response.Response;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class AsyncMockStubber {
    public static <T, M extends AsyncCallback> Stubber callSuccessWith(final T data) {
        return Mockito.doAnswer(new Answer<T>() {
            @Override
            @SuppressWarnings("unchecked")
            public T answer(InvocationOnMock invocationOnMock) throws Throwable {
                final Object[] args = invocationOnMock.getArguments();
                ((M) args[args.length - 1]).onSuccess(data);
                return null;
            }
        });
    }

    public static <T extends Response, M extends ResponseHandler> Stubber callHandleSuccessWith(final T data) {
        return Mockito.doAnswer(new Answer<T>() {
            @Override
            @SuppressWarnings("unchecked")
            public T answer(InvocationOnMock invocationOnMock) throws Throwable {
                final Object[] args = invocationOnMock.getArguments();
                ((M) args[args.length - 1]).handleSuccess(data);
                return null;
            }
        });
    }
}
