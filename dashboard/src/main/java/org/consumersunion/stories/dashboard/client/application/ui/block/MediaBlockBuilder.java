package org.consumersunion.stories.dashboard.client.application.ui.block;

import org.consumersunion.stories.common.client.ui.block.ContentElement;
import org.consumersunion.stories.common.client.util.Callback;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.MediaBlock;
import org.consumersunion.stories.dashboard.client.application.ui.block.configurator.BlockConfigurator;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Wrapper for {@link MediaBlock} configurators and Content preview elements.
 *
 * @see org.consumersunion.stories.common.client.ui.block.content.VideoContent
 * @see org.consumersunion.stories.dashboard.client.application.ui.block.configurator.VideoConfigurator
 * @see org.consumersunion.stories.common.client.ui.block.content.AudioContent
 * @see org.consumersunion.stories.dashboard.client.application.ui.block.configurator.AudioConfigurator
 */
public class MediaBlockBuilder extends BlockBuilder {
    private final ContentElement<MediaBlock> preview;

    private MediaBlock value;

    @Inject
    MediaBlockBuilder(
            Binder uiBinder,
            @Assisted ContentElement<MediaBlock> contentElement,
            @Assisted BlockConfigurator<MediaBlock> contentConfigurator,
            @Assisted("editMode") boolean editMode,
            @Assisted("readOnly") boolean readOnly) {
        super(uiBinder, contentElement, contentConfigurator, editMode, readOnly);

        this.preview = contentElement;
        this.value = contentConfigurator.getEditedValue();

        setShowDuplicate(value.getBlockType().isCustom());

        contentConfigurator.setDoneCallback(new Callback<MediaBlock>() {
            @Override
            public void onSuccess(MediaBlock media) {
                value = media;
                preview.display(value);
                handler.onBlockEdited(MediaBlockBuilder.this);
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
