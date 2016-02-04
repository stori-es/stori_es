package org.consumersunion.stories.common.client.ui.stories;

import com.google.gwt.view.client.HasRows;
import com.gwtplatform.mvp.client.UiHandlers;

public interface ListStoriesUiHandlers extends UiHandlers {
    void loadStories(int page);

    HasRows getRowHandler();
}
