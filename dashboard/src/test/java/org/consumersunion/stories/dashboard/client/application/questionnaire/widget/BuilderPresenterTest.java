package org.consumersunion.stories.dashboard.client.application.questionnaire.widget;

import javax.inject.Inject;

import org.consumersunion.stories.dashboard.client.BaseTestModule;
import org.consumersunion.stories.dashboard.client.application.collection.CollectionObserver;
import org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab.ContentTabPresenter;
import org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab.OnSubmitTabPresenter;
import org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab.PublicationTabPresenter;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.consumersunion.stories.dashboard.client.application.ui.BuilderTab.CONTENT;
import static org.consumersunion.stories.dashboard.client.application.ui.BuilderTab.ON_SUBMIT;
import static org.consumersunion.stories.dashboard.client.application.ui.BuilderTab.PUBLICATION;
import static org.consumersunion.stories.dashboard.client.application.ui.BuilderTab.QUESTIONS;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class BuilderPresenterTest {
    public static class Module extends BaseTestModule {
        @Override
        protected void configureTest() {
            super.configureTest();

            forceMock(CollectionObserver.class);
            forceMock(ContentTabPresenter.class);
            forceMock(OnSubmitTabPresenter.class);
            forceMock(PublicationTabPresenter.class);
        }
    }

    @Inject
    private BuilderPresenter presenter;
    @Inject
    private BuilderPresenter.MyView view;
    @Inject
    private CollectionObserver collectionObserver;
    @Inject
    private ContentTabPresenter contentTabPresenter;
    @Inject
    private OnSubmitTabPresenter onSubmitTabPresenter;
    @Inject
    private PublicationTabPresenter publicationTabPresenter;

    @Test
    public void initPresenter_questionnaire_setsTabs() throws Exception {
        presenter.initPresenter(true);

        verify(view).setTabs(same(QUESTIONS), same(ON_SUBMIT), same(PUBLICATION));
        verify(collectionObserver)
                .register(same(contentTabPresenter), same(onSubmitTabPresenter), same(publicationTabPresenter));
    }

    @Test
    public void initPresenter_collection_setsTabs() throws Exception {
        presenter.initPresenter(false);

        verify(view).setTabs(same(CONTENT), same(PUBLICATION));
        verify(collectionObserver).register(same(contentTabPresenter), same(publicationTabPresenter));
    }
}
