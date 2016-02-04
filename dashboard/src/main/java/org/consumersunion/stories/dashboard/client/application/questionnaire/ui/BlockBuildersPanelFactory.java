package org.consumersunion.stories.dashboard.client.application.questionnaire.ui;

import org.consumersunion.stories.dashboard.client.application.ui.block.BlockBuilder;
import org.consumersunion.stories.dashboard.client.application.ui.block.BlockBuilderHandler;

public interface BlockBuildersPanelFactory {
    BlockBuilderPanel create(BlockBuilder.Handler handler);

    BlockBuilderHandler createHandler(BlockBuilderHandler.SaveHandler saveHandler);
}
