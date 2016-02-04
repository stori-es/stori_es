package org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab;

import org.consumersunion.stories.dashboard.client.application.ui.block.BlockBuilderHandler;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ContentTabUiHandlers extends UiHandlers, BlockBuilderHandler.SaveHandler {
    void updateTheme(int theme);
}
