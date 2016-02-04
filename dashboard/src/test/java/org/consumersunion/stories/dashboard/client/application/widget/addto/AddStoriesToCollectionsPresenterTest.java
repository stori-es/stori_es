package org.consumersunion.stories.dashboard.client.application.widget.addto;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.ui.stories.StoriesListContainer;
import org.consumersunion.stories.common.client.util.MessageDispatcher;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.dashboard.client.AsyncMockStubber;
import org.consumersunion.stories.dashboard.client.BaseTestModule;
import org.consumersunion.stories.dashboard.client.TestBase;
import org.consumersunion.stories.dashboard.client.application.ui.addto.AddToMenuHandler;
import org.jukito.JukitoRunner;
import org.jukito.MockProvider;
import org.jukito.TestSingleton;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.assistedinject.Assisted;

import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class AddStoriesToCollectionsPresenterTest extends TestBase {
    public static class Module extends BaseTestModule {
        @Override
        protected void configureTest() {
            super.configureTest();

            bind(AddToMenuHandler.class)
                    .annotatedWith(Assisted.class)
                    .toProvider(new MockProvider<AddToMenuHandler>(AddToMenuHandler.class));

            bind(StoriesListContainer.class)
                    .annotatedWith(Assisted.class)
                    .toProvider(new MockProvider<StoriesListContainer>(StoriesListContainer.class))
                    .in(TestSingleton.class);
        }
    }

    @Inject
    Provider<AddStoriesToCollectionsPresenter> provider;

    @Test
    public void addStoriesToCollection(
            RpcCollectionServiceAsync collectionServiceAsync,
            @Assisted StoriesListContainer storiesListContainer) {
        // given
        AddStoriesToCollectionsPresenter presenter = spy(provider.get());
        List<StorySummary> mock = getStoriesListMock(5);
        Mockito.when(storiesListContainer.getStoriesList()).thenReturn(mock);

        Set<Integer> collectionIds = Sets.newHashSet(1, 2, 3, 4, 5);
        for (Integer collectionId : collectionIds) {
            presenter.onListItemAdded(new CollectionSummary(new Collection(collectionId, 1)));
        }

        // when
        presenter.onGoClicked();

        // then
        verify(collectionServiceAsync)
                .linkStoriesToCollections(
                        Matchers.eq(collectionIds), anySet(), (AsyncCallback<ActionResponse>) Matchers.any());
    }

    @Test
    public void addStoriesToCollection_showsSuccessMessage(
            RpcCollectionServiceAsync collectionServiceAsync,
            @Assisted StoriesListContainer storiesListContainer,
            MessageDispatcher messageDispatcher) {
        // given
        AddStoriesToCollectionsPresenter presenter = spy(provider.get());
        List<StorySummary> mock = getStoriesListMock(5);
        Mockito.when(storiesListContainer.getStoriesList()).thenReturn(mock);

        Set<Integer> collectionIds = Sets.newHashSet(1, 2);
        for (Integer collectionId : collectionIds) {
            presenter.onListItemAdded(new CollectionSummary(new Collection(collectionId, 1)));
        }

        DatumResponse datumResponse = Mockito.mock(DatumResponse.class);
        Mockito.when(datumResponse.isLoggedIn()).thenReturn(true);
        AsyncMockStubber.callSuccessWith(datumResponse)
                .when(collectionServiceAsync)
                .linkStoriesToCollections(
                        Matchers.eq(collectionIds), anySet(), (AsyncCallback<ActionResponse>) Matchers.any());

        // when
        presenter.onGoClicked();

        // then
        verify(messageDispatcher).displayMessage(Matchers.<MessageStyle>any(), Matchers.anyString());
    }

    @Test
    public void addStoriesToCollection_noStories_noUpdate(
            RpcCollectionServiceAsync collectionServiceAsync,
            @Assisted StoriesListContainer storiesListContainer) {
        // given
        AddStoriesToCollectionsPresenter presenter = spy(provider.get());
        Mockito.when(storiesListContainer.getStoriesList()).thenReturn(
                Mockito.mock(List.class, Mockito.RETURNS_DEEP_STUBS));

        // when
        presenter.onGoClicked();

        // then
        Mockito.verifyZeroInteractions(collectionServiceAsync);
    }

    @Test
    public void presenterSetAsUiHandlers(AddStoriesToCollectionsPresenter.MyView myView) {
        // when
        AddStoriesToCollectionsPresenter presenter = provider.get();

        // then
        verify(myView).setUiHandlers(presenter);
    }

    private List<StorySummary> getStoriesListMock(int numberOfStories) {
        List<StorySummary> stories = Lists.newArrayList();
        for (int i = 0; i < numberOfStories; i++) {
            stories.add(Mockito.mock(StorySummary.class, Mockito.RETURNS_DEEP_STUBS));
        }

        return stories;
    }
}
