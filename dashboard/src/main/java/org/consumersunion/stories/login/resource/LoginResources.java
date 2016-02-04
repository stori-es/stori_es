package org.consumersunion.stories.login.resource;

import com.google.gwt.resources.client.ClientBundle;

public interface LoginResources extends ClientBundle {
    @Source({"org/consumersunion/stories/login/resource/css/loginStyle.gss",
            "com/arcbees/gsss/mixin/client/mixins.gss"})
    LoginStyle loginStyle();
}
