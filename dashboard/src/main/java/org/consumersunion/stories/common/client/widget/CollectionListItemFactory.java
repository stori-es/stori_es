package org.consumersunion.stories.common.client.widget;

import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;

import com.google.inject.assistedinject.Assisted;

public interface CollectionListItemFactory {
    CollectionListItem create(
            CollectionSummary collectionSummary,
            CollectionListItem.ListItemHandler handler,
            boolean canRemove);

    CollectionListItem create(
            CollectionSummary collectionSummary,
            CollectionListItem.ListItemHandler handler,
            @Assisted("canRemove") boolean canRemove,
            @Assisted("withClickAction") boolean withClickAction);
}
