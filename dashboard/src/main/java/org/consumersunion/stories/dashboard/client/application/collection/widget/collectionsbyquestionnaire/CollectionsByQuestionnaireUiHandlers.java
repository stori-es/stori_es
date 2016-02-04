package org.consumersunion.stories.dashboard.client.application.collection.widget.collectionsbyquestionnaire;

import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;

import com.gwtplatform.mvp.client.UiHandlers;

public interface CollectionsByQuestionnaireUiHandlers extends UiHandlers {
    void removeTargetCollection(CollectionSummary collectionData);

    void collectionDetails(Integer collection);

    boolean canRemoveCollection(CollectionSummary collection);
}
