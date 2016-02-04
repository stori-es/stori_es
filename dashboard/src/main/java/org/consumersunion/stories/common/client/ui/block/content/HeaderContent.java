package org.consumersunion.stories.common.client.ui.block.content;

import org.consumersunion.stories.common.client.ui.block.ContentElement;
import org.consumersunion.stories.common.shared.model.document.Content;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class HeaderContent extends Composite implements ContentElement<Content> {
    interface Binder extends UiBinder<Widget, HeaderContent> {
    }

    @UiField
    Label header;

    @Inject
    HeaderContent(Binder uiBinder,
            @Assisted Content content) {
        initWidget(uiBinder.createAndBindUi(this));
        display(content);
    }

    @Override
    public void display(Content content) {
        header.setText(content.getContent());
    }
}
