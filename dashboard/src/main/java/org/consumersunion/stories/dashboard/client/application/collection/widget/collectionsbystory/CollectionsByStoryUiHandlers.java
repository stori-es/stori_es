package org.consumersunion.stories.dashboard.client.application.collection.widget.collectionsbystory;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;

import com.google.common.base.Predicate;
import com.gwtplatform.mvp.client.UiHandlers;

public interface CollectionsByStoryUiHandlers extends UiHandlers, Predicate<CollectionData> {
    void removeFromCollection(CollectionSummary collectionData);

    void addStoryToCollection(Collection collection);

    void collectionDetails(Integer collection);

    boolean canRemoveCollection(CollectionSummary collection);

    void redraw();
}
