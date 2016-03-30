package org.consumersunion.stories.dashboard.client.application.collection.widget.navbar;

import org.consumersunion.stories.common.client.event.StateSearchEvent;
import org.consumersunion.stories.dashboard.client.event.AddNewDocumentEvent;
import org.consumersunion.stories.dashboard.client.event.CancelNewDocumentEvent;
import org.consumersunion.stories.dashboard.client.event.CreateContentEvent;
import org.consumersunion.stories.dashboard.client.event.NavigationBarSelectionEvent;
import org.consumersunion.stories.dashboard.client.util.NavigationBarElement;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

import static org.consumersunion.stories.dashboard.client.util.NavigationBarElement.STORIES;

public class NavigationBarPresenter extends PresenterWidget<NavigationBarPresenter.MyView>
        implements NavigationBarUiHandlers, CancelNewDocumentEvent.CancelNewDocumentHandler,
        AddNewDocumentEvent.AddNewDocumentHandler, CreateContentEvent.CreateNewContentHandler,
        StateSearchEvent.SearchHandler {
    interface MyView extends View, HasUiHandlers<NavigationBarUiHandlers> {
        void setAddDocumentVisible(boolean visible);

        void select(NavigationBarElement element);
    }

    @Inject
    NavigationBarPresenter(
            EventBus eventBus,
            MyView view) {
        super(eventBus, view);

        getView().setUiHandlers(this);
    }

    @Override
    public void goTo(NavigationBarElement element) {
        NavigationBarSelectionEvent.fire(this, element);
    }

    public void select(NavigationBarElement element) {
        getView().select(element);
    }

    @Override
    public void onCancelNewDocument(CancelNewDocumentEvent event) {
        getView().setAddDocumentVisible(true);
    }

    @Override
    public void onCreateNewContent(CreateContentEvent event) {
        getView().setAddDocumentVisible(true);
    }

    @Override
    public void onAddNewDocument(AddNewDocumentEvent event) {
        getView().setAddDocumentVisible(false);
    }

    @Override
    public void onStateSearch(StateSearchEvent event) {
        select(STORIES);
        goTo(STORIES);
    }

    @Override
    protected void onBind() {
        super.onBind();

        addVisibleHandler(CancelNewDocumentEvent.TYPE, this);
        addVisibleHandler(AddNewDocumentEvent.TYPE, this);
        addVisibleHandler(CreateContentEvent.TYPE, this);
        addVisibleHandler(StateSearchEvent.TYPE, this);
    }
}
