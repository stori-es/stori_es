package org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab;

import java.util.ArrayList;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.RpcThemeServiceAsync;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.dashboard.client.BaseTestModule;
import org.consumersunion.stories.dashboard.client.application.collection.CollectionObserver;
import org.consumersunion.stories.dashboard.client.event.DisplayEvent;
import org.consumersunion.stories.dashboard.client.event.SavedCollectionEvent;
import org.jukito.JukitoRunner;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.google.gwt.user.client.rpc.AsyncCallback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class ContentTabPresenterTest {
    public static class Module extends BaseTestModule {
        @Override
        protected void configureTest() {
            super.configureTest();

            forceMock(CollectionObserver.class);
        }
    }

    private static final int ORGANIZATION_ID = 2;
    private static final int THEME = 999;

    @Inject
    private ContentTabPresenter presenter;
    @Inject
    private ContentTabPresenter.MyView view;
    @Inject
    private CollectionObserver collectionObserver;
    @Inject
    private RpcThemeServiceAsync themeService;

    @Test
    public void onDisplay_loadsThemesAndSetContent() throws Exception {
        DisplayEvent event = mock(DisplayEvent.class);
        Collection collection = new Collection();
        collection.setOwner(ORGANIZATION_ID);
        collection.setPublished(true);
        given(event.get()).willReturn(collection);

        presenter.onDisplay(event);

        verify(themeService).getThemesForOrganization(eq(ORGANIZATION_ID), any(AsyncCallback.class));
        verify(view).setContent(same(collection), eq(true));
    }

    @Test
    public void onCollectionSaved_refreshView() throws Exception {
        SavedCollectionEvent event = mock(SavedCollectionEvent.class);
        Collection collection = new Collection();
        collection.setOwner(ORGANIZATION_ID);
        collection.setPublished(true);
        given(event.get()).willReturn(collection);

        presenter.onCollectionSaved(event);

        verify(view).refreshView(same(collection), eq(true));
    }

    @Test
    public void save_setsBlocksAndSave() throws Exception {
        Collection collection = spy(new Collection());
        collection.setOwner(ORGANIZATION_ID);
        collection.setPublished(true);
        collection.setTheme(0);
        given(collectionObserver.getCollection()).willReturn(collection);
        ArrayList<Block> blocks = new ArrayList<Block>();

        presenter.save(blocks);

        InOrder inOrder = Mockito.inOrder(collection, collectionObserver);
        inOrder.verify(collection).setBlocks(same(blocks));
        inOrder.verify(collectionObserver).save();
    }

    @Test
    public void updateTheme_savesCollection() throws Exception {
        Collection collection = spy(new Collection());
        collection.setOwner(ORGANIZATION_ID);
        collection.setPublished(true);
        collection.setTheme(0);
        given(collectionObserver.getCollection()).willReturn(collection);

        presenter.updateTheme(THEME);

        InOrder inOrder = Mockito.inOrder(collection, collectionObserver);
        inOrder.verify(collection).setTheme(THEME);
        inOrder.verify(collectionObserver).save();
        assertThat(collection.getTheme()).isEqualTo(THEME);
    }

    @After
    public void tearDown() {
        verify(view).setUiHandlers(presenter);
    }
}
