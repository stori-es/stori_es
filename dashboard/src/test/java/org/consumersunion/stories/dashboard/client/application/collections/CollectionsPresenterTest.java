package org.consumersunion.stories.dashboard.client.application.collections;

import java.util.Map;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.event.SearchEvent;
import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.client.service.response.CollectionDataPagedResponse;
import org.consumersunion.stories.common.client.util.CustomHasRows;
import org.consumersunion.stories.common.client.util.CustomHasRowsFactory;
import org.consumersunion.stories.common.shared.dto.RetrievePagedCollectionsParams;
import org.consumersunion.stories.common.shared.model.CollectionSortDropDownItem;
import org.consumersunion.stories.common.shared.model.CollectionSortField;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.dashboard.client.AsyncMockStubber;
import org.consumersunion.stories.dashboard.client.BaseTestModule;
import org.consumersunion.stories.dashboard.client.PlaceRequestMatcher;
import org.consumersunion.stories.dashboard.client.TestBase;
import org.consumersunion.stories.dashboard.client.application.widget.search.SearchPresenter;
import org.consumersunion.stories.dashboard.client.application.widget.search.SearchPresenterFactory;
import org.consumersunion.stories.dashboard.client.event.SearchResultEvent;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;
import org.jukito.JukitoRunner;
import org.jukito.TestSingleton;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.google.common.collect.Maps;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JukitoRunner.class)
public class CollectionsPresenterTest extends TestBase {
    public static class Module extends BaseTestModule {
        @Override
        protected void configureTest() {
            super.configureTest();

            forceMock(SearchPresenter.class);

            bindMock(CustomHasRows.class).in(TestSingleton.class);

            install(new FactoryModuleBuilder().build(SearchPresenterFactory.class));
        }

        @Provides
        CustomHasRowsFactory getCustomHasRows(CustomHasRows customHasRows) {
            CustomHasRowsFactory factory = mock(CustomHasRowsFactory.class);

            given(factory.create(Matchers.<HasHandlers>any())).willReturn(customHasRows);

            return factory;
        }
    }

    private static final CollectionSortDropDownItem SORT_FIELD =
            new CollectionSortDropDownItem(CollectionSortField.MODIFIED_OLD);
    private static final String SOME_SEARCH_TOKEN = "someSearch";

    @Inject
    CollectionsPresenter collectionsPresenter;

    @Test
    public void searchEvent_revealsPlace(PlaceManager placeManager) {
        // given
        SearchEvent searchEvent = new SearchEvent(SOME_SEARCH_TOKEN, SORT_FIELD) {
        };
        PlaceRequest placeRequest = new PlaceRequest.Builder().nameToken(NameTokens.collections).build();
        when(placeManager.getCurrentPlaceRequest()).thenReturn(placeRequest);

        // when
        collectionsPresenter.onSearch(searchEvent);

        // then
        Map<String, String> params = Maps.newHashMap();
        params.put(ParameterTokens.search, searchEvent.getSearchToken());
        params.put(ParameterTokens.sort, searchEvent.getSortDropDownItem().getSortField().toString());
        verify(placeManager).revealPlace(argThat(new PlaceRequestMatcher(NameTokens.collections, params)));
    }

    @Test
    public void prepareFromRequest_updatesSearchResults(
            CustomHasRows customHasRows,
            EventBus eventBus,
            RpcCollectionServiceAsync collectionService) {
        // given
        PlaceRequest placeRequest = new PlaceRequest.Builder()
                .nameToken(NameTokens.collections)
                .with(ParameterTokens.search, SOME_SEARCH_TOKEN)
                .with(ParameterTokens.sort, SORT_FIELD.toString())
                .build();

        CollectionDataPagedResponse response = mock(CollectionDataPagedResponse.class);
        when(response.isLoggedIn()).thenReturn(true);

        AsyncMockStubber.callSuccessWith(response)
                .when(collectionService)
                .getCollections(Matchers.any(RetrievePagedCollectionsParams.class), Matchers.any(AsyncCallback.class));

        // when
        collectionsPresenter.prepareFromRequest(placeRequest);

        // then
        verify(customHasRows, Mockito.times(2)).setRowCount(Matchers.anyInt());
        verify(customHasRows).setVisibleRange(Matchers.anyInt(), Matchers.anyInt());
        verify(eventBus).fireEventFromSource(
                Matchers.any(SearchResultEvent.class), Matchers.eq(collectionsPresenter));
    }
}
