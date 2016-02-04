package org.consumersunion.stories.common.client.ui.block.content;

import org.consumersunion.stories.common.client.ui.block.ContentElement;
import org.consumersunion.stories.common.shared.model.document.ImageBlock;

import com.google.common.base.Strings;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class ImageContent extends Composite implements ContentElement<ImageBlock> {
    interface Binder extends UiBinder<Widget, ImageContent> {
    }

    @UiField
    Image image;
    @UiField
    HTML caption;

    @Inject
    ImageContent(
            Binder uiBinder,
            @Assisted ImageBlock imageBlock) {
        initWidget(uiBinder.createAndBindUi(this));

        if (imageBlock.getUrl() != null) {
            display(imageBlock);
        }
    }

    @Override
    public void display(ImageBlock content) {
        image.setAltText(content.getAltText());
        image.setUrl(content.getUrl());
        caption.setHTML(SafeHtmlUtils.fromTrustedString(Strings.nullToEmpty(content.getCaption())));
    }
}
