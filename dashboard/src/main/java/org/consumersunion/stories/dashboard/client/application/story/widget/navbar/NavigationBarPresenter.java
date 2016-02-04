package org.consumersunion.stories.dashboard.client.application.story.widget.navbar;

import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.service.datatransferobject.Documents;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.dashboard.client.event.StorySummaryLoadedEvent;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class NavigationBarPresenter extends PresenterWidget<NavigationBarPresenter.MyView>
        implements StorySummaryLoadedEvent.StorySummaryLoadedHandler {
    interface MyView extends View {
        void setAddContentEnabled(boolean enable);
    }

    @Inject
    NavigationBarPresenter(
            EventBus eventBus,
            MyView view) {
        super(eventBus, view);
    }

    @Override
    public void onStorySummaryLoaded(StorySummaryLoadedEvent event) {
        StorySummary storySummary = event.getStorySummary();
        Documents documents = storySummary.getDocuments(SystemEntityRelation.BODY);
        getView().setAddContentEnabled(!documents.hasDocuments());
    }

    @Override
    protected void onBind() {
        super.onBind();

        addRegisteredHandler(StorySummaryLoadedEvent.TYPE, this);
    }
}
