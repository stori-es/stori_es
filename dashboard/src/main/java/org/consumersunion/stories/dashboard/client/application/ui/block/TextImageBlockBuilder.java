package org.consumersunion.stories.dashboard.client.application.ui.block;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.ui.block.ContentElement;
import org.consumersunion.stories.common.client.util.Callback;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.TextImageBlock;
import org.consumersunion.stories.dashboard.client.application.ui.block.configurator.BlockConfigurator;

import com.google.inject.assistedinject.Assisted;

public class TextImageBlockBuilder extends BlockBuilder {
    private final ContentElement<TextImageBlock> element;

    private TextImageBlock value;

    @Inject
    TextImageBlockBuilder(
            Binder uiBinder,
            @Assisted ContentElement<TextImageBlock> element,
            @Assisted BlockConfigurator<TextImageBlock> configurator,
            @Assisted("editMode") boolean editMode,
            @Assisted("showDuplicate") boolean showDuplicate,
            @Assisted("showRemove") boolean showRemove,
            @Assisted("readOnly") boolean readOnly) {
        super(uiBinder, element, configurator, editMode, readOnly);

        this.element = element;
        this.value = configurator.getEditedValue();
        this.element.display(value);

        if (value.getBlockType().isCustom()) {
            setShowDuplicate(showDuplicate);
        } else {
            setShowDuplicate(false);
        }

        showRemove(showRemove);

        configurator.setDoneCallback(new Callback<TextImageBlock>() {
            @Override
            public void onSuccess(TextImageBlock content) {
                value = content;
                TextImageBlockBuilder.this.element.display(value);
                handler.onBlockEdited(TextImageBlockBuilder.this);
                switchTo(PREVIEW);
            }

            @Override
            public void onCancel() {
                switchTo(PREVIEW);
            }

            @Override
            public void onFailure() {
                super.onFailure();

                TextImageBlockBuilder.this.element.display(value);
                switchTo(PREVIEW);
            }
        });
    }

    @Override
    public Block getValue() {
        return value;
    }
}
