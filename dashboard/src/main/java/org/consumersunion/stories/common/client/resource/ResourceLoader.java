package org.consumersunion.stories.common.client.resource;

import javax.inject.Inject;

public class ResourceLoader {
    @Inject
    ResourceLoader(CommonResources commonResources) {
        commonResources.generalStyleCss().ensureInjected();
        commonResources.messagesStyle().ensureInjected();
        commonResources.simptip().ensureInjected();
    }
}
