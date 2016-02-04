package org.consumersunion.stories.dashboard.client.application.collection.popup;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;

/**
 * Handlers to deal with the selection or creation of a {@link Collection}, especially for
 * {@link CollectionSelectPresenter} and its children.
 */
public interface CollectionSelectUiHandlers extends UiHandlers {
    void associateNotification(List<String> collectionIds);

    void createAndAssociateNotification(String title);
}
