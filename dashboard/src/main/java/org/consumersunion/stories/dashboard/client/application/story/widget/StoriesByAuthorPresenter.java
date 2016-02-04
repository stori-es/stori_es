package org.consumersunion.stories.dashboard.client.application.story.widget;

import java.util.List;

import org.consumersunion.stories.common.client.service.RpcStoryServiceAsync;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.dashboard.client.event.StoryChangedEvent;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_EXPLICIT;

public class StoriesByAuthorPresenter extends PresenterWidget<StoriesByAuthorPresenter.MyView>
        implements StoriesByAuthorUiHandlers, StoryChangedEvent.StoryChangedHandler {
    interface MyView extends View, HasUiHandlers<StoriesByAuthorUiHandlers> {
        void setData(List<StorySummary> data, int start, int totalCount);

        void displayAuthorName(Profile profile);
    }

    public static final int PAGE_SIZE = 5;

    private final RpcStoryServiceAsync storyService;
    private final PlaceManager placeManager;

    private Profile currentProfile;

    @Inject
    StoriesByAuthorPresenter(
            EventBus eventBus,
            MyView view,
            RpcStoryServiceAsync storyService,
            PlaceManager placeManager) {
        super(eventBus, view);

        this.storyService = storyService;
        this.placeManager = placeManager;

        getView().setUiHandlers(this);
    }

    public void initPresenter(Profile currentProfile) {
        this.currentProfile = currentProfile;

        getView().displayAuthorName(currentProfile);
        loadStories(0);
    }

    public void storyDetails(StorySummary story) {
        PlaceRequest place = new PlaceRequest.Builder()
                .nameToken(NameTokens.story)
                .with(ParameterTokens.id, String.valueOf(story.getStory().getId()))
                .build();
        placeManager.revealPlace(place);
    }

    @Override
    public void loadStories(int start, int length) {
        loadStories(start);
    }

    @Override
    public void onStoryChanged(StoryChangedEvent event) {
        loadStories(0);
    }

    @Override
    protected void onBind() {
        addVisibleHandler(StoryChangedEvent.TYPE, this);
    }

    private void loadStories(int start) {
        storyService.getStories(
                StorySearchParameters.byAuthor(start, PAGE_SIZE, currentProfile.getId(), ACCESS_MODE_EXPLICIT),
                new ResponseHandler<PagedDataResponse<StorySummary>>() {
                    @Override
                    public void handleSuccess(PagedDataResponse<StorySummary> result) {
                        getView().setData(result.getData(), result.getStart(), result.getTotalCount());
                    }
                });
    }
}
