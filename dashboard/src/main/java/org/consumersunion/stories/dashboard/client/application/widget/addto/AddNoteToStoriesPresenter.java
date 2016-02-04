package org.consumersunion.stories.dashboard.client.application.widget.addto;

import java.util.Set;

import org.consumersunion.stories.common.client.i18n.CommonI18nMessages;
import org.consumersunion.stories.common.client.service.RpcDocumentServiceAsync;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.ui.stories.StoriesListContainer;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;
import org.consumersunion.stories.dashboard.client.application.ui.addto.AddToMenuHandler;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Widget used to add tags to stories
 */
class AddNoteToStoriesPresenter extends AddToPresenter<AddToUiHandlers, AddNoteToStoriesPresenter.MyView> {
    interface MyView extends AddToView<AddToUiHandlers> {
        String getNote();

        void onDisplay();
    }

    private final RpcDocumentServiceAsync documentService;

    @Inject
    AddNoteToStoriesPresenter(
            EventBus eventBus,
            MyView view,
            StorySummarySelectionHelper storySummarySelectionHelper,
            RpcDocumentServiceAsync documentService,
            CommonI18nMessages messages,
            @Assisted AddToMenuHandler addToMenuHandler,
            @Assisted StoriesListContainer storiesListContainer) {
        super(eventBus, view, storySummarySelectionHelper, addToMenuHandler, storiesListContainer, messages);

        this.documentService = documentService;
    }

    @Override
    public void onGoClicked(Set<Integer> storyIds) {
        documentService.createNotes(getView().getNote(), storyIds,
                this.<ActionResponse>createCallback(messages.noteAddedSuccessfully()));
    }

    @Override
    public void onGoClicked(StorySearchParameters storySearchParameters) {
        // TODO : Implement
    }

    @Override
    public void onDisplay() {
        super.onDisplay();

        getView().onDisplay();
    }
}
