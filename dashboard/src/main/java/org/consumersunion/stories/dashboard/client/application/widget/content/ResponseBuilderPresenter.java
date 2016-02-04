package org.consumersunion.stories.dashboard.client.application.widget.content;

import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.dashboard.client.application.questionnaire.widget.AnswerSetPresenter;
import org.consumersunion.stories.dashboard.client.application.ui.BuilderTab;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class ResponseBuilderPresenter extends PresenterWidget<ResponseBuilderPresenter.MyView>
        implements ResponseBuilderUiHandlers {
    interface MyView extends View, HasUiHandlers<ResponseBuilderUiHandlers> {
        void setTabs(BuilderTab... tabs);
    }

    static final Object SLOT_TAB_CONTENT = new Object();

    private final AnswerSetPresenter answerSetPresenter;

    @Inject
    ResponseBuilderPresenter(
            EventBus eventBus,
            MyView view,
            AnswerSetPresenter answerSetPresenter) {
        super(eventBus, view);

        this.answerSetPresenter = answerSetPresenter;

        getView().setUiHandlers(this);
    }

    public void initPresenter(AnswerSet answerSet) {
        answerSetPresenter.initPresenter(answerSet);

        getView().setTabs(BuilderTab.QUESTIONS);

        onTabChanged(BuilderTab.CONTENT);
    }

    @Override
    public void onTabChanged(BuilderTab tab) {
        PresenterWidget tabContent = null;
        switch (tab) {
            case CONTENT:
            case QUESTIONS:
                tabContent = answerSetPresenter;
                break;
        }

        setInSlot(SLOT_TAB_CONTENT, tabContent);
    }

    @Override
    protected void onBind() {
        super.onBind();

        setInSlot(SLOT_TAB_CONTENT, answerSetPresenter);
    }
}
