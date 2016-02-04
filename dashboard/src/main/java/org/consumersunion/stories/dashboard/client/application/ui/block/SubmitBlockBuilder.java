package org.consumersunion.stories.dashboard.client.application.ui.block;

import org.consumersunion.stories.common.client.ui.block.BlockElement;
import org.consumersunion.stories.common.client.util.Callback;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock;
import org.consumersunion.stories.dashboard.client.application.ui.block.configurator.BlockConfigurator;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class SubmitBlockBuilder extends BlockBuilder {
    private final BlockElement<SubmitBlock> preview;

    private SubmitBlock value;

    @Inject
    SubmitBlockBuilder(
            Binder uiBinder,
            @Assisted BlockElement<SubmitBlock> submitBlockWidget,
            @Assisted BlockConfigurator<SubmitBlock> submitBlockConfigurator,
            @Assisted("editMode") boolean editMode,
            @Assisted("readOnly") boolean readOnly) {
        super(uiBinder, submitBlockWidget, submitBlockConfigurator, editMode, readOnly);

        this.preview = submitBlockWidget;
        value = submitBlockConfigurator.getEditedValue();

        showRemove(false);
        setShowDuplicate(false);

        submitBlockConfigurator.setDoneCallback(new Callback<SubmitBlock>() {
            @Override
            public void onSuccess(SubmitBlock submitBlock) {
                value = submitBlock;
                preview.display(value);
                handler.onBlockEdited(SubmitBlockBuilder.this);
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
