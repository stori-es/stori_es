package org.consumersunion.stories.common.client.ui.block.content;

import org.consumersunion.stories.common.client.ui.block.ContentElement;
import org.consumersunion.stories.common.shared.model.document.DocumentBlock;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.ObjectElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class DocumentContent extends Composite implements ContentElement<DocumentBlock> {
    interface Binder extends UiBinder<Widget, DocumentContent> {
    }

    @UiField
    ObjectElement pdfContainer;
    @UiField
    AnchorElement pdfLink;
    @UiField
    Label documentTitle;

    @Inject
    DocumentContent(Binder uiBinder,
            @Assisted DocumentBlock documentBlock) {
        initWidget(uiBinder.createAndBindUi(this));
        display(documentBlock);
    }

    @Override
    public void display(DocumentBlock documentBlock) {
        pdfContainer.setData(documentBlock.getUrl());
        pdfLink.setHref(documentBlock.getUrl());
        documentTitle.setText(documentBlock.getTitle());
    }
}
