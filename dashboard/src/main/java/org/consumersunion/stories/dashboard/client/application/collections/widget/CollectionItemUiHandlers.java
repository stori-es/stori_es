package org.consumersunion.stories.dashboard.client.application.collections.widget;

import org.consumersunion.stories.common.shared.ExportKind;

import com.gwtplatform.mvp.client.UiHandlers;

public interface CollectionItemUiHandlers extends UiHandlers {
    void goToCollectionDetails();

    void deleteCollection();

    void export(ExportKind value);

    void enableTagsEdit();

    void watchCollection(boolean enable);

    void copyQuestionnaire(CollectionContent data);

    void setEditMode(boolean isEditMode);

    void cancelEdit();

    void save(boolean done);

    void updateBodyDocument(CollectionContent data);

    void openCard();

    void closeCard();
}
