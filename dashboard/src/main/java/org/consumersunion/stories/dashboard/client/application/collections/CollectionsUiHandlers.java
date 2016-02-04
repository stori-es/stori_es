package org.consumersunion.stories.dashboard.client.application.collections;

import com.google.gwt.view.client.HasRows;
import com.gwtplatform.mvp.client.UiHandlers;

public interface CollectionsUiHandlers extends UiHandlers {
    void addNewCollection();

    void addNewQuestionnaire();

    void goToPage(Integer pageNumber);

    HasRows getRowHandler();
}
