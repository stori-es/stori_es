package org.consumersunion.stories.dashboard.client.application.ui.block;

import org.consumersunion.stories.common.client.ui.block.ContentElement;
import org.consumersunion.stories.common.client.util.Callback;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.dashboard.client.application.ui.block.configurator.BlockConfigurator;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class ContentBlockBuilder extends BlockBuilder {
    private final ContentElement<Content> preview;

    private Content value;

    @Inject
    ContentBlockBuilder(
            Binder uiBinder,
            @Assisted ContentElement<Content> contentElement,
            @Assisted BlockConfigurator<Content> contentConfigurator,
            @Assisted("editMode") boolean editMode,
            @Assisted("showDuplicate") boolean showDuplicate,
            @Assisted("showRemove") boolean showRemove,
            @Assisted("readOnly") boolean readOnly) {
        super(uiBinder, contentElement, contentConfigurator, editMode, readOnly);

        this.preview = contentElement;
        this.value = contentConfigurator.getEditedValue();

        if (value.getBlockType().isCustom()) {
            setShowDuplicate(showDuplicate);
        } else {
            setShowDuplicate(false);
        }

        showRemove(showRemove);

        contentConfigurator.setDoneCallback(new Callback<Content>() {
            @Override
            public void onSuccess(Content content) {
                value = content;
                preview.display(value);
                handler.onBlockEdited(ContentBlockBuilder.this);
                switchTo(PREVIEW);
            }

            @Override
            public void onCancel() {
                switchTo(PREVIEW);
            }

            @Override
            public void onFailure() {
                super.onFailure();

                preview.display(value);
                switchTo(PREVIEW);
            }
        });
    }

    @Override
    public Block getValue() {
        return value;
    }
}
