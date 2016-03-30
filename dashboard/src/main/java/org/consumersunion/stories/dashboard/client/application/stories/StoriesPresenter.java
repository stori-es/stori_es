package org.consumersunion.stories.dashboard.client.application.stories;

import org.consumersunion.stories.common.client.event.SortChangedEvent;
import org.consumersunion.stories.common.client.model.StorySortFieldDropDownItem;
import org.consumersunion.stories.common.client.place.ClientPlaceManager;
import org.consumersunion.stories.common.client.ui.stories.ListStoriesPresenter;
import org.consumersunion.stories.common.client.ui.stories.StoriesListHandler;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.StorySortField;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;
import org.consumersunion.stories.dashboard.client.application.AbstractStoriesPresenter;
import org.consumersunion.stories.dashboard.client.application.StoriesDashboardPresenter;
import org.consumersunion.stories.dashboard.client.application.stories.widget.StoryCard;
import org.consumersunion.stories.dashboard.client.application.ui.addto.AddToMenuWidget;
import org.consumersunion.stories.dashboard.client.application.ui.addto.AddToWidgetFactory;
import org.consumersunion.stories.dashboard.client.application.widget.addto.AddToWidgetPresenter;
import org.consumersunion.stories.dashboard.client.application.widget.addto.AddToWidgetPresenterFactory;
import org.consumersunion.stories.dashboard.client.event.StoriesSelectionEvent;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;

import com.google.common.base.Strings;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_EXPLICIT;

/**
 * Presents a list of {@link Story}s. Reached by selecting the top level 'View Stories' menu item.
 */
public class StoriesPresenter extends AbstractStoriesPresenter<StoriesPresenter.MyView, StoriesPresenter.MyProxy>
        implements StoriesUiHandlers, StoriesSelectionEvent.StoriesSelectionHandler, StoriesListHandler {
    interface MyView extends View, HasUiHandlers<StoriesUiHandlers> {
        void setSearchToken(String token);

        void setAddToWidget(AddToMenuWidget addToWidget);

        void updateCount(boolean selecting, int numberOfStories);

        StorySortFieldDropDownItem getSort();

        void setStoriesCount(int count);

        void updateForCheckboxes(boolean withCheckboxes);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.stories)
    interface MyProxy extends ProxyPlace<StoriesPresenter> {
    }

    static final Object SLOT_ADD_TO_PANEL = new Object();
    static final Object SLOT_STORIES = new Object();

    private final Provider<Scheduler> schedulerProvider;
    private final ListStoriesPresenter listStoriesPresenter;
    private final ClientPlaceManager placeManager;

    private String searchToken = "";

    @Inject
    StoriesPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            Provider<Scheduler> schedulerProvider,
            AddToWidgetFactory addToWidgetFactory,
            AddToWidgetPresenterFactory addToWidgetPresenterFactory,
            @StoryCard ListStoriesPresenter listStoriesPresenter,
            ClientPlaceManager placeManager) {
        super(eventBus, view, proxy, StoriesDashboardPresenter.SLOT_MAIN_CONTENT);

        this.schedulerProvider = schedulerProvider;
        this.listStoriesPresenter = listStoriesPresenter;
        this.placeManager = placeManager;

        AddToWidgetPresenter addToWidgetPresenter = addToWidgetPresenterFactory.create(listStoriesPresenter);
        AddToMenuWidget addToWidget = addToWidgetFactory.createAddToWidget(addToWidgetPresenter);
        setInSlot(SLOT_ADD_TO_PANEL, addToWidgetPresenter);
        getView().setAddToWidget(addToWidget);

        getView().setUiHandlers(this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        if (isVisible()) {
            revealInParent();
        }
    }

    @Override
    public void filterStories(String searchToken) {
        PlaceRequest searchPlace = new PlaceRequest.Builder().nameToken(NameTokens.stories).build();

        if (!Strings.isNullOrEmpty(searchToken)) {
            searchPlace = new PlaceRequest.Builder().nameToken(NameTokens.stories)
                    .with("search", searchToken).build();
        }

        placeManager.revealPlace(searchPlace);
    }

    @Override
    public void onStoriesSelected(StoriesSelectionEvent event) {
        getView().updateCount(event.isSelecting(), event.getNumberOfStories());
        getView().updateForCheckboxes(event.isWithCheckboxes());
    }

    @Override
    public StorySearchParameters getStorySearchParameters(int start, int length) {
        SortField sort = getView().getSort().getSortField();
        return new StorySearchParameters(start, length, sort, true, searchToken, ACCESS_MODE_EXPLICIT);
    }

    @Override
    public void onRowCountChange(RowCountChangeEvent event) {
        getView().setStoriesCount(event.getNewRowCount());
    }

    @Override
    public void onSortChanged(StorySortField item) {
        SortChangedEvent.fire(this, item);
    }

    @Override
    public HasRows getRowHandler() {
        return listStoriesPresenter.getRowHandler();
    }

    @Override
    protected void onBind() {
        super.onBind();

        addVisibleHandler(StoriesSelectionEvent.TYPE, this);

        setInSlot(SLOT_STORIES, listStoriesPresenter);

        listStoriesPresenter.initPresenter(this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        handleSearch();
    }

    @Override
    protected void onReset(PlaceRequest placeRequest) {
        if (NameTokens.stories.equals(placeRequest.getNameToken())) {
            handleSearch();
        }
    }

    private void handleSearch() {
        String newSearchToken = placeManager.getCurrentPlaceRequest().getParameter(ParameterTokens.search, "");
        searchToken = newSearchToken;
        getView().setSearchToken(searchToken);
    }

    @Override
    protected void onHide() {
        super.onHide();

        listStoriesPresenter.clear();
    }
}
