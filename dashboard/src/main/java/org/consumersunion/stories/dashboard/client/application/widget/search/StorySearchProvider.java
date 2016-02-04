package org.consumersunion.stories.dashboard.client.application.widget.search;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.model.StorySortFieldDropDownItem;
import org.consumersunion.stories.common.shared.model.SortDropDownItem;

public class StorySearchProvider implements SearchProvider {
    private final String searchPlaceholder;

    @Inject
    StorySearchProvider(StoryTellerDashboardI18nLabels labels) {
        searchPlaceholder = labels.searchStories();
    }

    @Override
    public String getSearchPlaceholder() {
        return searchPlaceholder;
    }

    @Override
    public List<? extends SortDropDownItem> getSortFields() {
        return StorySortFieldDropDownItem.sortList();
    }

    @Override
    public String getDefaultSortFieldLabel() {
        return StorySortFieldDropDownItem.defaultSortField().getLabel();
    }
}
