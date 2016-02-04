package org.consumersunion.stories.dashboard.client.application.widget;

import com.gwtplatform.mvp.client.UiHandlers;

public interface TagsUiHandlers extends UiHandlers {
    void onTagAdded(String tag);

    void onTagRemoved(String tag);

    void onTagClicked(String tag);

    void onEditComplete();
}
