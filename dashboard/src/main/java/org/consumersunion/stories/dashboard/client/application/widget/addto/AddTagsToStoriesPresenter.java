package org.consumersunion.stories.dashboard.client.application.widget.addto;

import java.util.Set;

import org.consumersunion.stories.common.client.i18n.CommonI18nMessages;
import org.consumersunion.stories.common.client.service.RpcEntityServiceAsync;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.ui.stories.StoriesListContainer;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;
import org.consumersunion.stories.dashboard.client.application.ui.addto.AddToMenuHandler;
import org.consumersunion.stories.dashboard.client.application.util.TagsService;
import org.consumersunion.stories.dashboard.client.util.AbstractAsyncCallback;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Widget used to add tags to stories
 */
class AddTagsToStoriesPresenter extends AddToPresenter<AddToUiHandlers, AddTagsToStoriesPresenter.MyView> {
    interface MyView extends AddToView<AddToUiHandlers> {
        void populateSuggestionsList(Set<String> tags);

        Set<String> getTags();
    }

    private final RpcEntityServiceAsync entityService;
    private final TagsService tagsService;

    @Inject
    AddTagsToStoriesPresenter(
            EventBus eventBus,
            MyView view,
            StorySummarySelectionHelper storySummarySelectionHelper,
            RpcEntityServiceAsync entityService,
            TagsService tagsService,
            CommonI18nMessages messages,
            @Assisted AddToMenuHandler addToMenuHandler,
            @Assisted StoriesListContainer storiesListContainer) {
        super(eventBus, view, storySummarySelectionHelper, addToMenuHandler, storiesListContainer, messages);

        this.entityService = entityService;
        this.tagsService = tagsService;
    }

    @Override
    public void onGoClicked(Set<Integer> storyIds) {
        entityService.addTags(storyIds, getView().getTags(),
                createCallback(messages.tagsAddedSuccessfully(),
                        new AbstractAsyncCallback<ActionResponse>() {
                            @Override
                            public void onSuccess(ActionResponse result) {
                                updateTagsSuggestions();
                            }
                        }));
    }

    @Override
    public void onGoClicked(StorySearchParameters storySearchParameters) {
        // TODO : Implement
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        updateTagsSuggestions();
    }

    private void updateTagsSuggestions() {
        tagsService.loadTagsForSuggestionList(new TagsService.TagsCallback() {
            @Override
            public void onTagsReceived(Set<String> tags) {
                getView().populateSuggestionsList(tags);
            }
        });
    }
}
