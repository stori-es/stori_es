package org.consumersunion.stories.dashboard.client.application.collections.widget;

import org.consumersunion.stories.common.client.widget.ContentKind;

public interface NewCollectionPresenterFactory {
    NewCollectionPresenter create(ContentKind contentKind);
}
