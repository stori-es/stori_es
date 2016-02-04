package org.consumersunion.stories.dashboard.client.application.widget.addto;

import org.consumersunion.stories.common.shared.model.StorySelectField;

import com.gwtplatform.mvp.client.UiHandlers;

public interface AddToUiHandlers extends UiHandlers {
    void onGoClicked();

    void onStorySelectFieldChanged(StorySelectField currentSelectField);

    void reset();

    int getNumberOfSelectedStories();
}
