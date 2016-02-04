package org.consumersunion.stories.service.client;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AsyncFail<T> implements AsyncCallback<T> {
    private final String message;
    private final GWTTestCaseExposed testCase;

    public AsyncFail(GWTTestCaseExposed testCase, String message) {
        this.testCase = testCase;
        this.message = message;
    }

    @Override
    public void onFailure(Throwable caught) {
        GWTTestCase.fail("Unexpected exception: " + message + " - " + caught.getMessage());
        testCase.finishTestB();
    }
}
