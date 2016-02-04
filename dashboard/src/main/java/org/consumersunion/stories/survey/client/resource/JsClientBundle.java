package org.consumersunion.stories.survey.client.resource;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface JsClientBundle extends ClientBundle {
    @Source("js/easyXDM.min.js")
    TextResource easyXdm();

    @Source("js/ga-bootstrap.js")
    TextResource analytics();
}
