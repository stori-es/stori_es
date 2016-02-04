package org.consumersunion.stories.dashboard.client.application.collections;

import org.consumersunion.stories.common.client.event.SearchEvent;
import org.consumersunion.stories.common.client.event.SearchEvent.SearchHandler;
import org.consumersunion.stories.common.client.i18n.CommonI18nMessages;
import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.client.service.response.CollectionDataPagedResponse;
import org.consumersunion.stories.common.client.util.CurrentUser;
import org.consumersunion.stories.common.client.util.CustomHasRows;
import org.consumersunion.stories.common.client.util.CustomHasRowsFactory;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.common.shared.AuthConstants;
import org.consumersunion.stories.common.shared.dto.RetrievePagedCollectionsParams;
import org.consumersunion.stories.common.shared.model.CollectionSortDropDownItem;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.common.shared.service.QuestionnaireMask;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.dashboard.client.application.StoriesDashboardPresenter;
import org.consumersunion.stories.dashboard.client.application.collections.widget.CollectionItemPresenter;
import org.consumersunion.stories.dashboard.client.application.collections.widget.CollectionItemPresenterFactory;
import org.consumersunion.stories.dashboard.client.application.collections.widget.NewCollectionPresenter;
import org.consumersunion.stories.dashboard.client.application.collections.widget.NewCollectionPresenterFactory;
import org.consumersunion.stories.dashboard.client.application.widget.search.CollectionSearchProvider;
import org.consumersunion.stories.dashboard.client.application.widget.search.SearchPresenter;
import org.consumersunion.stories.dashboard.client.application.widget.search.SearchPresenterFactory;
import org.consumersunion.stories.dashboard.client.event.CollectionDeletedEvent;
import org.consumersunion.stories.dashboard.client.event.ReloadCollectionsEvent;
import org.consumersunion.stories.dashboard.client.event.ReloadCollectionsEvent.ReloadCollectionsHandler;
import org.consumersunion.stories.dashboard.client.event.SearchResultEvent;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;

import com.google.common.base.Strings;
import com.google.gwt.view.client.HasRows;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import static org.consumersunion.stories.dashboard.client.event.CollectionDeletedEvent.CollectionDeletedHandler;

public class CollectionsPresenter extends Presenter<CollectionsPresenter.MyView, CollectionsPresenter.MyProxy>
        implements CollectionsUiHandlers, CollectionDeletedHandler, SearchHandler, ReloadCollectionsHandler {
    interface MyView extends View, HasUiHandlers<CollectionsUiHandlers> {
        void clearCollections();

        void setupPager();

        void hideLoading();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.collections)
    interface MyProxy extends ProxyPlace<CollectionsPresenter> {
    }

    static final Object SLOT_SEARCH = new Object();
    static final Object SLOT_COLLECTIONS = new Object();
    static final Object SLOT_NEW = new Object();

    private static final int PAGE_LENGTH = 8;

    private final CurrentUser currentUser;
    private final RpcCollectionServiceAsync collectionService;
    private final CollectionItemPresenterFactory collectionItemPresenterFactory;
    private final PlaceManager placeManager;
    private final CommonI18nMessages commonI18nMessages;
    private final SearchPresenter searchPresenter;
    private final NewCollectionPresenterFactory newCollectionPresenterFactory;
    private final CustomHasRows rowHandler;

    private Integer currentPage;
    private String searchToken;
    private CollectionSortDropDownItem sortField;

    @Inject
    CollectionsPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            CurrentUser currentUser,
            RpcCollectionServiceAsync collectionService,
            CustomHasRowsFactory customHasRowsFactory,
            CollectionItemPresenterFactory collectionItemPresenterFactory,
            PlaceManager placeManager,
            SearchPresenterFactory searchPresenterFactory,
            NewCollectionPresenterFactory newCollectionPresenterFactory,
            CollectionSearchProvider collectionSearchProvider,
            CommonI18nMessages commonI18nMessages) {
        super(eventBus, view, proxy, StoriesDashboardPresenter.SLOT_MAIN_CONTENT);

        this.currentUser = currentUser;
        this.collectionService = collectionService;
        this.collectionItemPresenterFactory = collectionItemPresenterFactory;
        this.placeManager = placeManager;
        this.commonI18nMessages = commonI18nMessages;
        this.searchPresenter = searchPresenterFactory.create(collectionSearchProvider);
        this.newCollectionPresenterFactory = newCollectionPresenterFactory;
        this.rowHandler = customHasRowsFactory.create(this);

        getView().setUiHandlers(this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest placeRequest) {
        searchToken = placeRequest.getParameter(ParameterTokens.search, "");

        String sortFieldString = placeRequest.getParameter(ParameterTokens.sort, "");
        setSortField(sortFieldString);

        searchPresenter.init(searchToken, sortField);

        currentPage = Integer.valueOf(placeRequest.getParameter(ParameterTokens.page, "1"));
        if (currentPage > 0) {
            loadCollections();
        }

        if (isVisible()) {
            revealInParent();
        }
    }

    @Override
    public void onReloadCollections(ReloadCollectionsEvent event) {
        if (event.getSource() instanceof CollectionItemPresenter) {
            loadCollections();
        }
    }

    @Override
    public void onSearch(SearchEvent event) {
        if (NameTokens.collections.equals(placeManager.getCurrentPlaceRequest().getNameToken())) {
            PlaceRequest.Builder placeRequestBuilder = new PlaceRequest.Builder()
                    .nameToken(NameTokens.collections)
                    .with(ParameterTokens.sort, event.getSortDropDownItem().getSortField().toString());

            if (!Strings.isNullOrEmpty(event.getSearchToken())) {
                placeRequestBuilder.with(ParameterTokens.search, event.getSearchToken());
            }

            placeManager.revealPlace(placeRequestBuilder.build());
        }
    }

    @Override
    public void addNewCollection() {
        NewCollectionPresenter presenter = newCollectionPresenterFactory.create(ContentKind.COLLECTION);
        setInSlot(SLOT_NEW, presenter);
    }

    @Override
    public void addNewQuestionnaire() {
        NewCollectionPresenter presenter = newCollectionPresenterFactory.create(ContentKind.QUESTIONNAIRE);
        setInSlot(SLOT_NEW, presenter);
    }

    @Override
    public void goToPage(Integer pageNumber) {
        PlaceRequest place = new PlaceRequest.Builder(placeManager.getCurrentPlaceRequest())
                .with(ParameterTokens.page, pageNumber.toString())
                .build();
        placeManager.revealPlace(place);
    }

    @Override
    public void onCollectionDeleted(CollectionDeletedEvent event) {
        loadCollections();
    }

    @Override
    public HasRows getRowHandler() {
        return rowHandler;
    }

    @Override
    protected void onBind() {
        super.onBind();

        addVisibleHandler(ReloadCollectionsEvent.TYPE, this);
        addVisibleHandler(CollectionDeletedEvent.TYPE, this);
        addVisibleHandler(SearchEvent.TYPE, this);

        getView().setupPager();

        setInSlot(SLOT_SEARCH, searchPresenter);
    }

    private void loadCollections() {
        getView().clearCollections();
        rowHandler.setRowCount(0);
        collectionService.getCollections(new RetrievePagedCollectionsParams.Builder()
                        .withStart((currentPage - 1) * PAGE_LENGTH)
                        .withLength(PAGE_LENGTH)
                        .withSortField(sortField.getSortField())
                        .withAscending(true)
                        .withQuestionnaireMask(QuestionnaireMask.QUESTIONNAIRE_MASK_ALL)
                        .withPermissionMask(AuthConstants.ROLE_READER)
                        .withAccessMode(AuthConstants.ACCESS_MODE_EXPLICIT)
                        .withStoryCount(true)
                        .withLinkedCollections(true)
                        .withEffectiveId(currentUser.getCurrentProfileId())
                        .build(searchToken),
                new ResponseHandler<CollectionDataPagedResponse>() {
                    @Override
                    public void handleSuccess(CollectionDataPagedResponse result) {
                        onCollectionsLoaded(result);
                    }
                }
        );
    }

    private void onCollectionsLoaded(CollectionDataPagedResponse result) {
        rowHandler.setRowCount(result.getTotalCount());
        rowHandler.setVisibleRange(result.getStart(), PAGE_LENGTH);

        for (CollectionData collectionData : result.getData()) {
            CollectionItemPresenter collectionPresenter =
                    collectionItemPresenterFactory.create(this, collectionData);
            addToSlot(SLOT_COLLECTIONS, collectionPresenter);
        }

        String resultMessage = commonI18nMessages.showingNbCollectionsQuestionnaires(result.getCollectionsCount(),
                result.getQuestionnairesCount());
        SearchResultEvent.fire(this, resultMessage);

        getView().hideLoading();
    }

    private void setSortField(String sortFieldString) {
        try {
            sortField = CollectionSortDropDownItem.parse(sortFieldString);
        } catch (IllegalArgumentException e) {
            sortField = CollectionSortDropDownItem.defaultSortField();
        }
    }
}
