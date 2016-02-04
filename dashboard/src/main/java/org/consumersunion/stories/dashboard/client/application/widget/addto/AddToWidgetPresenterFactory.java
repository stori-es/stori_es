package org.consumersunion.stories.dashboard.client.application.widget.addto;

import org.consumersunion.stories.common.client.ui.stories.StoriesListContainer;

public interface AddToWidgetPresenterFactory {
    AddToWidgetPresenter create(StoriesListContainer storiesListContainer);
}
