package org.consumersunion.stories.dashboard.client.application.ui.block;

import org.consumersunion.stories.common.shared.model.document.BlockType;

import com.google.gwt.user.client.ui.Widget;

public interface MetaBlockHandler {
    void replace(Widget toReplace, Widget newWidget);

    void setupMetaBlock(MetaBlock metaBlock);

    void createBlockAndReplace(BlockType blockType, BlockType formType, MetaBlock metaBlock);
}
