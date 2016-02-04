package org.consumersunion.stories.dashboard.client.application.collection;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.dashboard.client.event.ReloadCollectionsEvent;

public class CollectionIdsUtil {
    public boolean isSameCollection(Collection currentCollection, ReloadCollectionsEvent event) {
        Collection sourceCollection = event.getSourceCollection();
        return sourceCollection != null && currentCollection != null
                && sourceCollection.getId() == currentCollection.getId()
                || currentCollection != null && event.getRelatedCollectionIds().contains(currentCollection.getId());
    }

    public boolean isRelatedCollection(Collection currentCollection, Collection other) {
        return other != null && currentCollection != null
                && (currentCollection.getCollectionSources().contains(other.getId())
                || currentCollection.getTargetCollections().contains(other.getId()));
    }
}
