package org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.dashboard.client.BaseTestModule;
import org.consumersunion.stories.dashboard.client.application.collection.CollectionObserver;
import org.consumersunion.stories.dashboard.client.event.DisplayEvent;
import org.consumersunion.stories.dashboard.client.event.SavedCollectionEvent;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.google.gwt.user.client.rpc.AsyncCallback;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class PublicationTabPresenterTest {
    public static class Module extends BaseTestModule {
        @Override
        protected void configureTest() {
            super.configureTest();

            forceMock(CollectionObserver.class);
            forceMock(PermalinkValidator.class);
        }
    }

    private static final int ORGANIZATION_ID = 2;
    private static final String PERMALINK = "permalink";
    private static final String NEW_PERMALINK = "newlink";
    private static final String PERMALINK_URL = "permalinkurl";
    private static final String OLD_LINK = "oldlink";

    @Inject
    private PublicationTabPresenter presenter;
    @Inject
    private PublicationTabPresenter.MyView view;
    @Inject
    private CollectionObserver collectionObserver;
    @Inject
    private RpcCollectionServiceAsync collectionService;
    @Inject
    private PermalinkUtil permalinkUtil;
    @Inject
    private PermalinkValidator permalinkValidator;

    @Test
    public void onDisplay_refreshView() throws Exception {
        DisplayEvent event = mock(DisplayEvent.class);
        Collection collection = new Collection();
        collection.setOwner(ORGANIZATION_ID);
        collection.setPublished(true);
        collection.setPermalink(PERMALINK);
        given(event.get()).willReturn(collection);
        given(collectionObserver.getCollection()).willReturn(collection);

        String permalinkUrl = "permalinkurl";
        given(permalinkUtil.getPermalink(same(PERMALINK), anyString())).willReturn(permalinkUrl);

        presenter.onDisplay(event);

        verify(view).refreshView(same(collection));
        verify(view).setPermalinkUrl(same(permalinkUrl));
    }

    @Test
    public void onCollectionSaved_updatesPermalink() throws Exception {
        SavedCollectionEvent event = mock(SavedCollectionEvent.class);
        Collection collection = new Collection();
        collection.setOwner(ORGANIZATION_ID);
        collection.setPublished(true);
        collection.setPermalink(PERMALINK);
        given(event.get()).willReturn(collection);
        given(collectionObserver.getCollection()).willReturn(collection);

        given(permalinkUtil.getPermalink(same(PERMALINK), anyString())).willReturn(PERMALINK_URL);

        presenter.onCollectionSaved(event);

        verify(view).setPermalinkUrl(same(PERMALINK_URL));
        verify(view).updatePermalink(same(collection));
    }

    @Test
    public void toggleState_togglesTrueToFalse() throws Exception {
        boolean published = true;
        Collection collection = new Collection();
        collection.setPublished(published);
        collection = spy(collection);
        given(collectionObserver.getCollection()).willReturn(collection);

        presenter.toggleState();

        InOrder inOrder = Mockito.inOrder(collection, collectionObserver);
        inOrder.verify(collection).setPublished(false);
        inOrder.verify(collectionObserver).save();
    }

    @Test
    public void toggleState_togglesFalseToTrue() throws Exception {
        boolean published = false;
        Collection collection = new Collection();
        collection.setPublished(published);
        collection = spy(collection);
        given(collectionObserver.getCollection()).willReturn(collection);

        presenter.toggleState();

        InOrder inOrder = Mockito.inOrder(collection, collectionObserver);
        inOrder.verify(collection).setPublished(true);
        inOrder.verify(collectionObserver).save();
    }

    @Test
    public void updatePermalink_newPermalink_savesCollection() throws Exception {
        Collection collection = new Collection();
        collection.setPermalink("/collections/oldlink");
        collection = spy(collection);
        given(collectionObserver.getCollection()).willReturn(collection);

        presenter.updatePermalink(NEW_PERMALINK);

        InOrder inOrder = Mockito.inOrder(collection, collectionObserver);
        inOrder.verify(collection).setPermalink("/collections/" + NEW_PERMALINK);
        inOrder.verify(collectionObserver).save();
    }

    @Test
    public void updatePermalink_samePermalink_doesNothing() throws Exception {
        Collection collection = new Collection();
        collection.setPermalink("/collections/" + OLD_LINK);
        collection = spy(collection);
        given(collectionObserver.getCollection()).willReturn(collection);
        String newPermalink = OLD_LINK;

        presenter.updatePermalink(newPermalink);

        verify(collection, never()).setPermalink(anyString());
        verify(collectionObserver, never()).save();
    }

    @Test
    public void checkIfLinkExists_samePermalink_setsAvailable() throws Exception {
        Collection collection = new Collection();
        collection.setPermalink("/collections/" + OLD_LINK);
        collection = spy(collection);
        given(collectionObserver.getCollection()).willReturn(collection);
        String newPermalink = OLD_LINK;

        presenter.checkIfLinkExists(newPermalink);

        verify(view).setLinkDisponibility(true);
    }

    @Test
    public void checkIfLinkExists_differentPermalinkAndValid_checksAvailability() throws Exception {
        Collection collection = new Collection();
        collection.setPermalink("/collections/" + OLD_LINK);
        collection = spy(collection);
        given(collectionObserver.getCollection()).willReturn(collection);
        given(permalinkValidator.isValidLink(same(NEW_PERMALINK))).willReturn(true);

        presenter.checkIfLinkExists(NEW_PERMALINK);

        verify(collectionService).checkUnusedLink(same(NEW_PERMALINK), any(AsyncCallback.class));
    }

    @Test
    public void checkIfLinkExists_differentPermalinkAndInvalid() throws Exception {
        Collection collection = new Collection();
        collection.setPermalink("/collections/" + OLD_LINK);
        collection = spy(collection);
        given(collectionObserver.getCollection()).willReturn(collection);
        given(permalinkValidator.isValidLink(same(NEW_PERMALINK))).willReturn(false);

        presenter.checkIfLinkExists(NEW_PERMALINK);

        verify(view).setLinkDisponibility(false);
    }
}
