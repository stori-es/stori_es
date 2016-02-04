package org.consumersunion.stories.common.client.ui.stories;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.event.MapBoundariesChangeEvent;
import org.consumersunion.stories.common.client.event.RedrawEvent;
import org.consumersunion.stories.common.client.event.ReloadStoriesEvent;
import org.consumersunion.stories.common.client.event.SearchEvent;
import org.consumersunion.stories.common.client.event.SortChangedEvent;
import org.consumersunion.stories.common.client.event.UpdateMapPinsEvent;
import org.consumersunion.stories.common.client.service.RpcStoryServiceAsync;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.client.util.CustomHasRows;
import org.consumersunion.stories.common.client.util.CustomHasRowsFactory;
import org.consumersunion.stories.common.client.util.PublicResponseHandler;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class ListStoriesPresenter extends PresenterWidget<ListStoriesPresenter.MyView> implements
        ListStoriesUiHandlers, SearchEvent.SearchHandler, SortChangedEvent.SortChangedHandler,
        MapBoundariesChangeEvent.MapBoundariesChangeHandler, StoriesListContainer, RedrawEvent.RedrawHandler,
        ReloadStoriesEvent.ReloadStoriesHandler, StoriesListHandler {
    public interface MyView extends View, HasUiHandlers<ListStoriesUiHandlers> {
        void initPager(int defaultPageSize);

        void reloadData();

        void clear();

        void setupPager(HasRows rowHandler);

        IsWidget getStoriesListContainer();

        void hideLoading();

        void showLoading();

        void goToPage(int page, boolean force);

        int getCurrentPage();
    }

    private static final int PAGE_SIZE = 8;

    static final Object SLOT_STORIES = new Object();

    private final PlaceManager placeManager;
    private final RpcStoryServiceAsync storyService;
    private final boolean trackUrl;
    private final StoryItemFactory storyItemFactory;
    private final CustomHasRows rowHandler;

    private List<StoryCard> storyItemPresenters;
    private int nbStoriesLoaded;
    private int nbStoriesToLoad;
    private StoriesListHandler storiesListHandler;
    private Request lastRequest;
    private int currentPage;
    private String boundNameToken;
    private String currentSearchToken;

    @Inject
    ListStoriesPresenter(
            EventBus eventBus,
            MyView view,
            PlaceManager placeManager,
            RpcStoryServiceAsync storyService,
            CustomHasRowsFactory customHasRowsFactory,
            @Assisted boolean trackUrl,
            @Assisted StoryItemFactory storyItemFactory) {
        super(eventBus, view);

        this.placeManager = placeManager;
        this.storyService = storyService;
        this.trackUrl = trackUrl;
        this.storyItemFactory = storyItemFactory;
        this.rowHandler = customHasRowsFactory.create(this);

        currentPage = 1;

        getView().setUiHandlers(this);
    }

    @Override
    public List<StorySummary> getStoriesList() {
        return FluentIterable.from(storyItemPresenters)
                .transform(new Function<StoryCard, StorySummary>() {
                    @Override
                    public StorySummary apply(StoryCard input) {
                        return input.getStorySummary();
                    }
                }).toList();
    }

    @Override
    public StorySearchParameters getStorySearchParameters(int start, int length) {
        return storiesListHandler.getStorySearchParameters(start, length);
    }

    @Override // Called when a list item is done loading
    public void onRedraw(RedrawEvent event) {
        nbStoriesLoaded++;

        if (nbStoriesLoaded == nbStoriesToLoad) {
            getView().hideLoading();
        }
    }

    @Override
    public void redraw() {
        for (Redrawable storyItemPresenter : storyItemPresenters) {
            storyItemPresenter.redraw();
        }
    }

    @Override
    public void onReloadStories(ReloadStoriesEvent event) {
        loadStories(getView().getCurrentPage());
    }

    public void initPresenter(StoriesListHandler storiesListHandler) {
        boundNameToken = getNameToken();
        this.storiesListHandler = storiesListHandler;
        storyItemPresenters = Lists.newArrayList();

        rowHandler.addRowCountChangeHandler(storiesListHandler);

        getView().initPager(PAGE_SIZE);
    }

    @Override
    public void loadStories(int page) {
        int realPage = page + 1;

        if (realPage != currentPage && trackUrl) {
            PlaceRequest currentPlaceRequest = placeManager.getCurrentPlaceRequest();
            PlaceRequest placeRequest = new PlaceRequest.Builder(currentPlaceRequest)
                    .with(ParameterTokens.page, String.valueOf(realPage))
                    .build();
            placeManager.revealRelativePlace(placeRequest, -1);
        }

        loadStoriesByCollection(page * PAGE_SIZE, PAGE_SIZE);
    }

    @Override
    public HasRows getRowHandler() {
        return rowHandler;
    }

    @Override
    public IsWidget getWidgetContainer() {
        return getView().getStoriesListContainer();
    }

    @Override
    public void onSearch(SearchEvent event) {
        reloadDataDeferred();
    }

    @Override
    public void onBoundariesChanged(MapBoundariesChangeEvent event) {
        reloadDataDeferred();
    }

    @Override
    public void onSortChanged(SortChangedEvent event) {
        reloadDataDeferred();
    }

    public void setPage(int page) {
        getView().goToPage(page, false);
    }

    public void clear() {
        rowHandler.setRowCount(0);
        clearSlot(SLOT_STORIES);
    }

    @Override
    public void onRowCountChange(RowCountChangeEvent event) {
        storiesListHandler.onRowCountChange(event);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        currentPage = Integer.valueOf(placeManager.getCurrentPlaceRequest().getParameter(ParameterTokens.page, "1"));
        getView().goToPage(currentPage, true);
    }

    @Override
    protected void onReset() {
        super.onReset();

        PlaceRequest currentPlaceRequest = placeManager.getCurrentPlaceRequest();
        int page = Integer.valueOf(currentPlaceRequest.getParameter(ParameterTokens.page, "1"));

        String searchToken = currentPlaceRequest.getParameter(ParameterTokens.search, "");
        boolean sameSearchToken = searchToken.equals(currentSearchToken);
        if ((page != currentPage || !sameSearchToken) && getNameToken().equals(boundNameToken)) {
            currentPage = page;
            currentSearchToken = searchToken;
            getView().goToPage(page, !sameSearchToken);
        }
    }

    @Override
    protected void onBind() {
        super.onBind();

        addVisibleHandler(SearchEvent.TYPE, this);
        addVisibleHandler(SortChangedEvent.TYPE, this);
        addVisibleHandler(MapBoundariesChangeEvent.TYPE, this);
        addVisibleHandler(ReloadStoriesEvent.TYPE, this);
        addRegisteredHandler(RedrawEvent.TYPE, this);

        getView().setupPager(rowHandler);
    }

    private void loadStoriesByCollection(final int start, final int length) {
        StorySearchParameters storySearchParameters =
                storiesListHandler.getStorySearchParameters(start, length);

        ResponseHandler<PagedDataResponse<StorySummary>> callback =
                new PublicResponseHandler<PagedDataResponse<StorySummary>>() {
                    @Override
                    public void handleSuccess(PagedDataResponse<StorySummary> result) {
                        onStoriesLoaded(result);
                    }
                };

        rowHandler.setRowCount(0);
        getView().showLoading();

        if (lastRequest != null) {
            lastRequest.cancel();
        }

        lastRequest = storyService.getStories(storySearchParameters, callback);
    }

    private void reloadDataDeferred() {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                getView().reloadData();
            }
        });
    }

    private void onStoriesLoaded(PagedDataResponse<StorySummary> result) {
        clearSlot(SLOT_STORIES);

        nbStoriesLoaded = 0;
        nbStoriesToLoad = result.getData() == null ? 0 : result.getData().size();
        if (nbStoriesToLoad == 0) {
            getView().hideLoading();
        }

        List<Integer> ids = new ArrayList<Integer>();
        List<StorySummary> storySummaries = result.getData();
        storyItemPresenters = Lists.newArrayList();

        if (storySummaries != null) {
            for (StorySummary story : storySummaries) {
                ids.add(story.getStoryId());
                StoryCard storyItemPresenter = storyItemFactory.create(this, story);
                addToSlot(SLOT_STORIES, (PresenterWidget<?>) storyItemPresenter);
                storyItemPresenters.add(storyItemPresenter);
            }
        }

        UpdateMapPinsEvent.fire(this, ids);
        rowHandler.setRowCount(result.getTotalCount());
        rowHandler.setVisibleRange(result.getStart(), PAGE_SIZE);
    }

    private String getNameToken() {
        return placeManager.getCurrentPlaceRequest().getNameToken();
    }
}
