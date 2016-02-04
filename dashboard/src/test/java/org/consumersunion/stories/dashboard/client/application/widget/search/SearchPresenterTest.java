package org.consumersunion.stories.dashboard.client.application.widget.search;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.CollectionSortDropDownItem;
import org.consumersunion.stories.dashboard.client.BaseTestModule;
import org.consumersunion.stories.dashboard.client.SearchEventMatcher;
import org.consumersunion.stories.dashboard.client.TestBase;
import org.consumersunion.stories.dashboard.client.event.SearchResultEvent;
import org.jukito.JukitoRunner;
import org.jukito.MockProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class SearchPresenterTest extends TestBase {
    public static class Module extends BaseTestModule {
        @Override
        protected void configureTest() {
            super.configureTest();

            forceMock(SearchProvider.class);

            bind(SearchProvider.class).annotatedWith(Assisted.class)
                    .toProvider(new MockProvider<SearchProvider>(SearchProvider.class));
        }
    }

    private static final String SOME_SEARCH_RESULT = "someSearchResult";
    private static final String SOME_SEARCH_TEXT = "someSearchText";

    @Inject
    SearchPresenter searchPresenter;

    @Test
    public void searchResultReceived_updatesView(SearchPresenter.MyView view) {
        // given
        SearchResultEvent searchResultEvent = mock(SearchResultEvent.class);
        given(searchResultEvent.getResult()).willReturn(SOME_SEARCH_RESULT);

        // when
        searchPresenter.onSearchResultReceived(searchResultEvent);

        // then
        verify(view).setSearchResult(SOME_SEARCH_RESULT);
    }

    @Test
    public void searchTextChanged_fireSearchEvent(SearchPresenter.MyView view,
            EventBus eventBus) {
        // given
        given(view.getSearchToken()).willReturn(SOME_SEARCH_TEXT);
        given(view.getCurrentSort()).willReturn(CollectionSortDropDownItem.defaultSortField());

        // when
        searchPresenter.onSearchTextChanged();

        // then
        verify(eventBus)
                .fireEventFromSource(
                        argThat(new SearchEventMatcher<CollectionSortDropDownItem>(SOME_SEARCH_TEXT,
                                CollectionSortDropDownItem.defaultSortField())), eq(searchPresenter));
    }
}
