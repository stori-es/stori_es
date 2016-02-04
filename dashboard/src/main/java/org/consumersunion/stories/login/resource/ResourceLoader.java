package org.consumersunion.stories.login.resource;

import javax.inject.Inject;

public class ResourceLoader {
    @Inject
    ResourceLoader(LoginResources loginResources) {
        loginResources.loginStyle().ensureInjected();
    }
}
