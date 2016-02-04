package org.consumersunion.stories.dashboard.client.application.collections.widget;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.RpcEntityServiceAsync;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.NotificationTrigger;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.dashboard.client.BaseTestModule;
import org.consumersunion.stories.dashboard.client.TestBase;
import org.consumersunion.stories.dashboard.client.application.collection.popup.SourceOrTargetSelectPresenter;
import org.consumersunion.stories.dashboard.client.application.collection.widget.collectionsbystory
        .CollectionsByStoryPresenter;
import org.consumersunion.stories.dashboard.client.application.questionnaire.widget.ListQuestionnairePresenter;
import org.consumersunion.stories.dashboard.client.application.widget.TagsPresenter;
import org.consumersunion.stories.dashboard.client.application.widget.TagsPresenterFactory;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.gwtplatform.mvp.client.PresenterWidget;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JukitoRunner.class)
public class CollectionItemPresenterTest extends TestBase {
    public static class Module extends BaseTestModule {
        @Override
        protected void configureTest() {
            super.configureTest();

            forceMock(CollectionData.class);
            forceMock(CollectionsByStoryPresenter.class);
            forceMock(ListQuestionnairePresenter.class);
            forceMock(SourceOrTargetSelectPresenter.class);
            forceMock(TagsPresenter.class);

            install(new FactoryModuleBuilder().build(CollectionItemPresenterFactory.class));
            install(new FactoryModuleBuilder().build(TagsPresenterFactory.class));
        }
    }

    @Inject
    CollectionItemPresenterFactory itemFactory;

    @Test
    public void enableWatch_callsStartWatching(CollectionData collectionData,
            RpcEntityServiceAsync entityService) {
        Collection collection = mock(Collection.class, RETURNS_DEEP_STUBS);
        when(collection.getId()).thenReturn(1);
        when(collectionData.getCollection()).thenReturn(collection);
        CollectionItemPresenter collectionItemPresenter =
                itemFactory.create(mock(PresenterWidget.class), collectionData);

        collectionItemPresenter.watchCollection(true);

        verify(entityService).startWatching(
                eq(1), eq(NotificationTrigger.STORY_ADDED), any(AsyncCallback.class));
    }

    @Test
    public void removeWatch_callsStopWatching(CollectionData collectionData,
            RpcEntityServiceAsync entityService) {
        Collection collection = mock(Collection.class, RETURNS_DEEP_STUBS);
        when(collection.getId()).thenReturn(1);
        when(collectionData.getCollection()).thenReturn(collection);
        CollectionItemPresenter collectionItemPresenter =
                itemFactory.create(mock(PresenterWidget.class), collectionData);

        collectionItemPresenter.watchCollection(false);

        verify(entityService).stopWatching(
                eq(1), eq(NotificationTrigger.STORY_ADDED), any(AsyncCallback.class));
    }
}
