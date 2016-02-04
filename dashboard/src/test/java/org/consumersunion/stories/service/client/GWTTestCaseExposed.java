package org.consumersunion.stories.service.client;

import org.consumersunion.stories.common.client.service.RpcUserService;
import org.consumersunion.stories.common.client.service.RpcUserServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

public abstract class GWTTestCaseExposed extends GWTTestCase {
    protected RpcUserServiceAsync userService;

    protected void finishTestB() {
        super.finishTest();
    }

    @Override
    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();

        userService = GWT.create(RpcUserService.class);
    }
}
