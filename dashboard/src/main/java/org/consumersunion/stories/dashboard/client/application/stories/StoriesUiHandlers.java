package org.consumersunion.stories.dashboard.client.application.stories;

import org.consumersunion.stories.common.shared.model.StorySortField;

import com.google.gwt.view.client.HasRows;
import com.gwtplatform.mvp.client.UiHandlers;

public interface StoriesUiHandlers extends UiHandlers {
    void filterStories(String searchToken);

    void onSortChanged(StorySortField item);

    HasRows getRowHandler();
}
