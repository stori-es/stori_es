package org.consumersunion.stories.common.client.widget;

import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;

import com.google.inject.assistedinject.Assisted;

public interface CollectionListItemFactory {
    CollectionListItem create(
            CollectionSummary collectionSummary,
            ListItemHandler<CollectionSummary> handler,
            boolean canRemove);

    CollectionListItem create(
            CollectionSummary collectionSummary,
            ListItemHandler<CollectionSummary> handler,
            @Assisted("canRemove") boolean canRemove,
            @Assisted("withClickAction") boolean withClickAction);
}
