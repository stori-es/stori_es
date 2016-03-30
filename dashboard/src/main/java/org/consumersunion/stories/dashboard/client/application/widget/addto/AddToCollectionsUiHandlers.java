package org.consumersunion.stories.dashboard.client.application.widget.addto;

import org.consumersunion.stories.common.client.widget.ListItemHandler;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;

import com.google.common.base.Predicate;

public interface AddToCollectionsUiHandlers
        extends AddToUiHandlers, Predicate<CollectionData>, ListItemHandler<CollectionSummary> {
    void onListItemAdded(CollectionSummary collectionSummary);

    void onCancelClicked();
}
