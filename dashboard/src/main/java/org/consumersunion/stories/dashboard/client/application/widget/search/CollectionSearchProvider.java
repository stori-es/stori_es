package org.consumersunion.stories.dashboard.client.application.widget.search;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.shared.model.CollectionSortDropDownItem;
import org.consumersunion.stories.common.shared.model.SortDropDownItem;

public class CollectionSearchProvider implements SearchProvider {
    private final String searchPlaceholder;

    @Inject
    CollectionSearchProvider(StoryTellerDashboardI18nLabels labels) {
        searchPlaceholder = labels.searchCollections();
    }

    @Override
    public String getSearchPlaceholder() {
        return searchPlaceholder;
    }

    @Override
    public List<? extends SortDropDownItem> getSortFields() {
        return CollectionSortDropDownItem.getSortFields();
    }

    @Override
    public String getDefaultSortFieldLabel() {
        return CollectionSortDropDownItem.defaultSortField().getLabel();
    }
}
