package org.consumersunion.stories.common.client.api;

import com.gwtplatform.dispatch.rest.client.RestApplicationPath;
import com.gwtplatform.dispatch.rest.client.gin.RestDispatchAsyncModule;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ApiModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new RestDispatchAsyncModule());

        bindConstant().annotatedWith(RestApplicationPath.class).to("api");
    }
}
