package org.consumersunion.stories.dashboard.client.application.stories.widget;

import org.consumersunion.stories.common.shared.model.document.Document;

import com.gwtplatform.mvp.client.UiHandlers;

public interface StoryItemUiHandlers extends UiHandlers {
    void onStorySelected();

    void enableTagsEdit();

    void closeCard();

    void openCard(boolean addContent);

    void save(boolean done);

    void addContent();

    void setEditMode(boolean editMode);

    void removeDefaultContent();

    void cancelEdit();

    void edit();

    void setDefaultContent(Document document);
}
