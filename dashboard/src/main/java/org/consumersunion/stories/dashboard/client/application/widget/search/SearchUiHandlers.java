package org.consumersunion.stories.dashboard.client.application.widget.search;

import com.gwtplatform.mvp.client.UiHandlers;

public interface SearchUiHandlers extends UiHandlers {
    void onSearchTextChanged();

    void onSortChanged();
}
