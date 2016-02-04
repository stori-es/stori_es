package org.consumersunion.stories.common.client.resource;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface CommonResources extends ClientBundle {
    @Source("images/loading.gif")
    ImageResource loading();

    @Source("images/question_mark.png")
    ImageResource questionMark();

    @Source({"css/generalStyle.gss",
            "com/arcbees/gsss/mixin/client/mixins.gss"})
    GeneralStyle generalStyleCss();

    @Source({"css/messagesStyle.gss",
            "com/arcbees/gsss/mixin/client/mixins.gss"})
    MessagesStyle messagesStyle();

    @Source("css/simptip.css")
    SimptipStyle simptip();

    @Source("css/toolbarStyle.css")
    RichTextToolbarStyle richTextToolbarStyle();
}
