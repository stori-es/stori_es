package org.consumersunion.stories.dashboard.client.application.collection.widget.collectionstoken;

import com.google.common.base.Predicate;
import com.gwtplatform.mvp.client.UiHandlers;

public interface CollectionsTokenUiHandlers<Summary, Dto, Data> extends UiHandlers, Predicate<Data> {
    void removeCollection(Summary summary);

    void onCollectionSelected(Dto collection);

    void collectionDetails(Summary summary);

    boolean canRemoveCollection(Summary collection);

    void redraw();
}
