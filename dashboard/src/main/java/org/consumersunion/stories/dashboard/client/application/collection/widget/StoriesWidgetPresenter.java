package org.consumersunion.stories.dashboard.client.application.collection.widget;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.event.MapBoundariesChangeEvent;
import org.consumersunion.stories.common.client.event.SearchEvent;
import org.consumersunion.stories.common.client.model.StorySortFieldDropDownItem;
import org.consumersunion.stories.common.client.service.RpcStoryServiceAsync;
import org.consumersunion.stories.common.client.service.datatransferobject.Coordinates;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.ui.stories.ListStoriesPresenter;
import org.consumersunion.stories.common.client.ui.stories.StoriesListContainer;
import org.consumersunion.stories.common.client.ui.stories.StoriesListHandler;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.shared.AuthConstants;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.SortDropDownItem;
import org.consumersunion.stories.common.shared.model.StorySortField;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.common.shared.service.datatransferobject.StoryPosition;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;
import org.consumersunion.stories.dashboard.client.application.collection.CollectionObserver;
import org.consumersunion.stories.dashboard.client.application.stories.widget.StoryCard;
import org.consumersunion.stories.dashboard.client.application.ui.addto.AddToMenuWidget;
import org.consumersunion.stories.dashboard.client.application.ui.addto.AddToWidgetFactory;
import org.consumersunion.stories.dashboard.client.application.util.location.DefaultLocationProvider;
import org.consumersunion.stories.dashboard.client.application.widget.StoriesMapPresenter;
import org.consumersunion.stories.dashboard.client.application.widget.addto.AddToWidgetPresenter;
import org.consumersunion.stories.dashboard.client.application.widget.addto.AddToWidgetPresenterFactory;
import org.consumersunion.stories.dashboard.client.application.widget.search.SearchPresenter;
import org.consumersunion.stories.dashboard.client.application.widget.search.SearchPresenterFactory;
import org.consumersunion.stories.dashboard.client.application.widget.search.StorySearchProvider;
import org.consumersunion.stories.dashboard.client.event.DisplayEvent;
import org.consumersunion.stories.dashboard.client.event.SavedCollectionEvent;
import org.consumersunion.stories.dashboard.client.event.SearchResultEvent;
import org.consumersunion.stories.dashboard.client.event.StoriesSelectionEvent;
import org.consumersunion.stories.dashboard.client.util.GeoUtils;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.services.Geocoder;
import com.google.gwt.maps.client.services.GeocoderRequest;
import com.google.gwt.maps.client.services.GeocoderRequestHandler;
import com.google.gwt.maps.client.services.GeocoderResult;
import com.google.gwt.maps.client.services.GeocoderStatus;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class StoriesWidgetPresenter extends PresenterWidget<StoriesWidgetPresenter.MyView>
        implements StoriesListHandler, MapBoundariesChangeEvent.MapBoundariesChangeHandler,
        StoriesWidgetUiHandlers, StoriesSelectionEvent.StoriesSelectionHandler,
        CollectionObserver.CollectionHandler<Collection>, SearchEvent.SearchHandler {
    interface MyView extends View, HasUiHandlers<StoriesWidgetUiHandlers> {
        void setAddToWidget(AddToMenuWidget addToWidget);

        void updateForCheckboxes(boolean withCheckboxes);

        String getStoriesCountText(Integer count);

        String getUpdateCountText(boolean selecting, int numberOfStories);
    }

    static final Object SLOT_SEARCH = new Object();
    static final Object SLOT_ADD_TO_PANEL = new Object();
    static final Object SLOT_MAP = new Object();
    static final Object SLOT_STORIES = new Object();

    private final SearchPresenter searchPresenter;
    private final RpcStoryServiceAsync storyService;
    private final CollectionObserver collectionObserver;
    private final ListStoriesPresenter listStoriesPresenter;
    private final StoriesMapPresenter storiesMapPresenter;
    private final DefaultLocationProvider defaultLocationProvider;

    private String searchToken;
    private Integer collectionId;
    private LatLng northEast;
    private LatLng southWest;

    @Inject
    StoriesWidgetPresenter(
            EventBus eventBus,
            MyView view,
            StorySearchProvider storySearchProvider,
            SearchPresenterFactory searchPresenterFactory,
            RpcStoryServiceAsync storyService,
            CollectionObserver collectionObserver,
            @StoryCard ListStoriesPresenter listStoriesPresenter,
            StoriesMapPresenter storiesMapPresenter,
            AddToWidgetPresenterFactory addToWidgetPresenterFactory,
            AddToWidgetFactory addToWidgetFactory,
            DefaultLocationProvider defaultLocationProvider) {
        super(eventBus, view);

        this.storyService = storyService;
        this.collectionObserver = collectionObserver;
        this.listStoriesPresenter = listStoriesPresenter;
        this.storiesMapPresenter = storiesMapPresenter;
        this.defaultLocationProvider = defaultLocationProvider;
        this.searchPresenter = searchPresenterFactory.create(storySearchProvider);

        getView().setUiHandlers(this);

        AddToWidgetPresenter addToWidgetPresenter = addToWidgetPresenterFactory.create(listStoriesPresenter);
        AddToMenuWidget addToWidget = addToWidgetFactory.createAddToWidget(addToWidgetPresenter);
        setInSlot(SLOT_ADD_TO_PANEL, addToWidgetPresenter);
        getView().setAddToWidget(addToWidget);
    }

    public void prepareFromRequest(PlaceRequest request) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                collectionObserver.register(StoriesWidgetPresenter.this);
            }
        });

        collectionId = Integer.valueOf(request.getParameter(ParameterTokens.id, "-1"));
        searchToken = request.getParameter(ParameterTokens.search, "");

        String sortFieldString = request.getParameter(ParameterTokens.sort, "");

        searchPresenter.init(searchToken, getSortField(sortFieldString));

        if (GeoUtils.isLocationToken(searchToken)) {
            processProximitySearch();
        }

        switchToList();
    }

    @Override
    public void onSearch(SearchEvent event) {
        searchToken = event.getSearchToken();
        event.getSortDropDownItem();

        loadAllStoriesPosition(collectionId);
    }

    @Override
    public void onDisplay(DisplayEvent<Collection> event) {
        listStoriesPresenter.initPresenter(this);

        searchPresenter.onSortChanged();

        loadAllStoriesPosition(event.get().getId());
    }

    @Override
    public void onCollectionSaved(SavedCollectionEvent event) {
    }

    @Override
    public void onStoriesSelected(StoriesSelectionEvent event) {
        getView().updateForCheckboxes(event.isWithCheckboxes());

        String text = getView().getUpdateCountText(event.isSelecting(), event.getNumberOfStories());
        SearchResultEvent.fire(this, text);
    }

    @Override
    public StoriesListContainer getStoriesListContainer() {
        return listStoriesPresenter;
    }

    @Override
    public StorySearchParameters getStorySearchParameters(int start, int length) {
        StorySearchParameters storySearchParameters;

        Coordinates northEastCoordinates = northEast == null ? null : new Coordinates(northEast.getLatitude(),
                northEast.getLongitude());
        Coordinates southWestCoordinates = southWest == null ? null : new Coordinates(southWest.getLatitude(),
                southWest.getLongitude());
        if (collectionObserver.getCollection().isQuestionnaire()) {
            storySearchParameters = new StorySearchParameters(start, length, null, collectionId, searchToken,
                    northEastCoordinates, southWestCoordinates, AuthConstants.ACCESS_MODE_EXPLICIT,
                    searchPresenter.<StorySortField>getSortField());
        } else {
            storySearchParameters = new StorySearchParameters(start, length, collectionId, null, searchToken,
                    northEastCoordinates, southWestCoordinates, AuthConstants.ACCESS_MODE_EXPLICIT,
                    searchPresenter.<StorySortField>getSortField());
        }

        storySearchParameters.setSearchToken(searchToken);

        return storySearchParameters;
    }

    @Override
    public void onRowCountChange(RowCountChangeEvent event) {
        SearchResultEvent.fire(this, getView().getStoriesCountText(event.getNewRowCount()));
    }

    @Override
    public void onBoundariesChanged(MapBoundariesChangeEvent event) {
        northEast = event.getNorthEast();
        southWest = event.getSouthWest();
    }

    @Override
    public void switchToList() {
        northEast = null;
        southWest = null;

        clearSlot(SLOT_MAP);
        MapBoundariesChangeEvent.fire(this, northEast, southWest);
    }

    @Override
    public void switchToMap() {
        northEast = defaultLocationProvider.northEast();
        southWest = defaultLocationProvider.southWest();

        setInSlot(SLOT_MAP, storiesMapPresenter);
        MapBoundariesChangeEvent.fire(this, northEast, southWest);
    }

    @Override
    public void onHide() {
        northEast = null;
        southWest = null;

        collectionObserver.unregister(this);
    }

    @Override
    protected void onBind() {
        super.onBind();

        addVisibleHandler(SearchEvent.TYPE, this);
        addVisibleHandler(MapBoundariesChangeEvent.TYPE, this);

        setInSlot(SLOT_SEARCH, searchPresenter);
        setInSlot(SLOT_STORIES, listStoriesPresenter);
    }

    private SortDropDownItem getSortField(String sortFieldString) {
        try {
            return StorySortFieldDropDownItem.parse(sortFieldString);
        } catch (IllegalArgumentException e) {
            return StorySortFieldDropDownItem.defaultSortField();
        }
    }

    private void processProximitySearch() {
        GeocoderRequest request = GeocoderRequest.newInstance();
        request.setAddress(GeoUtils.extractNear(searchToken));

        Geocoder.newInstance().geocode(request, new GeocoderRequestHandler() {
            @Override
            public void onCallback(JsArray<GeocoderResult> result, GeocoderStatus status) {
                if (status == GeocoderStatus.OK) {
                    LatLng location = result.get(0).getGeometry().getLocation();
                    String distance = GeoUtils.extractWithin(searchToken);

                    StringBuilder builder = new StringBuilder();
                    builder.append("near:");
                    builder.append(location.getLatitude());
                    builder.append(",");
                    builder.append(location.getLongitude());
                    builder.append(" within:");
                    builder.append(distance);

                    searchToken = builder.toString();
                }
            }
        });
    }

    private void loadAllStoriesPosition(Integer collectionId) {
        storyService.getStoriesPositionByCollection(collectionId, searchToken, AuthConstants.ACCESS_MODE_EXPLICIT,
                new ResponseHandler<DataResponse<StoryPosition>>() {
                    @Override
                    public void handleSuccess(DataResponse<StoryPosition> result) {
                        storiesMapPresenter.setStoriesPositions(result.getData());
                    }
                });
    }
}
