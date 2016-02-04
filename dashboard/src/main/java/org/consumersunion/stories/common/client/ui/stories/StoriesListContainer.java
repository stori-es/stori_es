package org.consumersunion.stories.common.client.ui.stories;

import java.util.List;

import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasRows;

public interface StoriesListContainer extends StoriesListHandler {
    void redraw();

    List<StorySummary> getStoriesList();

    HasRows getRowHandler();

    IsWidget getWidgetContainer();
}
