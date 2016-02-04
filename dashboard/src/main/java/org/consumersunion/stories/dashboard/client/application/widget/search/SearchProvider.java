package org.consumersunion.stories.dashboard.client.application.widget.search;

import java.util.List;

import org.consumersunion.stories.common.shared.model.SortDropDownItem;

public interface SearchProvider {
    String getSearchPlaceholder();

    List<? extends SortDropDownItem> getSortFields();

    String getDefaultSortFieldLabel();
}
