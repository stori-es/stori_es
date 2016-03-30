package org.consumersunion.stories.dashboard.client.application.ui.block;

import org.consumersunion.stories.common.client.ui.block.ContentElement;
import org.consumersunion.stories.common.client.util.Callback;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.ImageBlock;
import org.consumersunion.stories.dashboard.client.application.ui.block.configurator.BlockConfigurator;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class ImageBlockBuilder extends BlockBuilder {
    private final ContentElement<ImageBlock> preview;

    private ImageBlock value;

    @Inject
    ImageBlockBuilder(
            Binder uiBinder,
            @Assisted ContentElement<ImageBlock> contentElement,
            @Assisted BlockConfigurator<ImageBlock> contentConfigurator,
            @Assisted("editMode") boolean editMode,
            @Assisted("readOnly") boolean readOnly) {
        super(uiBinder, contentElement, contentConfigurator, editMode, readOnly);

        this.preview = contentElement;
        this.value = contentConfigurator.getEditedValue();

        setShowDuplicate(value.getBlockType().isCustom());

        contentConfigurator.setDoneCallback(new Callback<ImageBlock>() {
            @Override
            public void onSuccess(ImageBlock image) {
                value = image;
                preview.display(value);
                handler.onBlockEdited(ImageBlockBuilder.this);
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
