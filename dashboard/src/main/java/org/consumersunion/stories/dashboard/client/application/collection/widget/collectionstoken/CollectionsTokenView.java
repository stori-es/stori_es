package org.consumersunion.stories.dashboard.client.application.collection.widget.collectionstoken;

import java.util.List;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.View;

interface CollectionsTokenView<Summary, Dto, Data>
        extends View, HasUiHandlers<CollectionsTokenUiHandlers<Summary, Dto, Data>> {
    void setData(List<Summary> data);

    void clear();

    void showQuestionnaireIcon();
}
