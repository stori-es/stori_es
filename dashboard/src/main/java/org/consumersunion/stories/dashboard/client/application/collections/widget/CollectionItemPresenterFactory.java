package org.consumersunion.stories.dashboard.client.application.collections.widget;

import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;

import com.gwtplatform.mvp.client.PresenterWidget;

public interface CollectionItemPresenterFactory {
    CollectionItemPresenter create(PresenterWidget handler, CollectionData collectionData);
}
