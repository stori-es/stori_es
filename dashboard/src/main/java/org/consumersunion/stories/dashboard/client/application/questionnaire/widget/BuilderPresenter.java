package org.consumersunion.stories.dashboard.client.application.questionnaire.widget;

import org.consumersunion.stories.dashboard.client.application.collection.CollectionObserver;
import org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab.ContentTabPresenter;
import org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab.OnSubmitTabPresenter;
import org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab.PublicationTabPresenter;
import org.consumersunion.stories.dashboard.client.application.ui.BuilderTab;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

/**
 * Presenter managing the editing of a Questionnaire/Collection through child presenters
 * {@link org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab.ContentTabPresenter}
 * {@link org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab.PublicationTabPresenter}
 * {@link org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab.OnSubmitTabPresenter}
 */
public class BuilderPresenter extends PresenterWidget<BuilderPresenter.MyView> implements BuilderUiHandlers {
    interface MyView extends View, HasUiHandlers<BuilderUiHandlers> {
        void setTabs(BuilderTab... tabs);
    }

    static final Object SLOT_TAB_CONTENT = new Object();

    private final CollectionObserver collectionObserver;
    private final ContentTabPresenter contentTabPresenter;
    private final OnSubmitTabPresenter onSubmitTabPresenter;
    private final PublicationTabPresenter publicationTabPresenter;

    @Inject
    BuilderPresenter(
            EventBus eventBus,
            MyView view,
            CollectionObserver collectionObserver,
            ContentTabPresenter contentTabPresenter,
            OnSubmitTabPresenter onSubmitTabPresenter,
            PublicationTabPresenter publicationTabPresenter) {
        super(eventBus, view);

        this.collectionObserver = collectionObserver;
        this.contentTabPresenter = contentTabPresenter;
        this.onSubmitTabPresenter = onSubmitTabPresenter;
        this.publicationTabPresenter = publicationTabPresenter;

        getView().setUiHandlers(this);
    }

    public void initPresenter(boolean questionnaire) {
        if (questionnaire) {
            getView().setTabs(BuilderTab.QUESTIONS, BuilderTab.ON_SUBMIT, BuilderTab.PUBLICATION);
            collectionObserver.register(contentTabPresenter, onSubmitTabPresenter, publicationTabPresenter);
        } else {
            getView().setTabs(BuilderTab.CONTENT, BuilderTab.PUBLICATION);
            collectionObserver.register(contentTabPresenter, publicationTabPresenter);
        }

        onTabChanged(BuilderTab.CONTENT);
    }

    @Override
    public void onTabChanged(BuilderTab tab) {
        PresenterWidget tabContent = null;
        switch (tab) {
            case CONTENT:
            case QUESTIONS:
                tabContent = contentTabPresenter;
                break;
            case ON_SUBMIT:
                tabContent = onSubmitTabPresenter;
                break;
            case PUBLICATION:
                tabContent = publicationTabPresenter;
                break;
        }

        setInSlot(SLOT_TAB_CONTENT, tabContent);
    }

    @Override
    protected void onBind() {
        super.onBind();

        setInSlot(SLOT_TAB_CONTENT, contentTabPresenter);
    }
}
