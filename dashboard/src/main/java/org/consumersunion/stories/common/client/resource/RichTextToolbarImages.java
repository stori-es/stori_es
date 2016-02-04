package org.consumersunion.stories.common.client.resource;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface RichTextToolbarImages extends ClientBundle {
    @Source("images/bold.gif")
    ImageResource bold();

    @Source("images/createLink.gif")
    ImageResource createLink();

    @Source("images/italic.gif")
    ImageResource italic();

    @Source("images/strikeThrough.gif")
    ImageResource strikeThrough();

    @Source("images/ol.gif")
    ImageResource ol();

    @Source("images/removeFormat.gif")
    ImageResource removeFormat();

    @Source("images/removeLink.gif")
    ImageResource removeLink();

    @Source("images/ul.gif")
    ImageResource ul();

    @Source("images/underline.gif")
    ImageResource underline();

    @Source("images/indent.gif")
    ImageResource indent();

    @Source("images/outdent.gif")
    ImageResource outdent();
}
