package org.consumersunion.stories.dashboard.client.application.ui.block;

import org.consumersunion.stories.common.client.ui.block.ContentElement;
import org.consumersunion.stories.common.client.util.Callback;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.DocumentBlock;
import org.consumersunion.stories.dashboard.client.application.ui.block.configurator.BlockConfigurator;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class DocumentBlockBuilder extends BlockBuilder {
    private final ContentElement<DocumentBlock> preview;

    private DocumentBlock value;

    @Inject
    DocumentBlockBuilder(
            Binder uiBinder,
            @Assisted ContentElement<DocumentBlock> contentElement,
            @Assisted BlockConfigurator<DocumentBlock> contentConfigurator,
            @Assisted("editMode") boolean editMode,
            @Assisted("readOnly") boolean readOnly) {
        super(uiBinder, contentElement, contentConfigurator, editMode, readOnly);

        this.preview = contentElement;
        this.value = contentConfigurator.getEditedValue();

        setShowDuplicate(value.getBlockType().isCustom());

        contentConfigurator.setDoneCallback(new Callback<DocumentBlock>() {
            @Override
            public void onSuccess(DocumentBlock document) {
                value = document;
                preview.display(value);
                handler.onBlockEdited(DocumentBlockBuilder.this);
                switchTo(PREVIEW);
            }

            @Override
            public void onCancel() {
                switchTo(PREVIEW);
            }
        });
    }

    @Override
    public Block getValue() {
        return value;
    }
}
