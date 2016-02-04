package org.consumersunion.stories.dashboard.client.application.story;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class StoryView extends ViewImpl implements StoryPresenter.MyView {
    interface Binder extends UiBinder<Widget, StoryView> {
    }

    @UiField
    SimplePanel storyCard;
    @UiField
    SimplePanel navBar;
    @UiField
    SimplePanel newContent;
    @UiField
    SimplePanel documents;

    @Inject
    StoryView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == StoryPresenter.SLOT_STORY) {
            storyCard.setWidget(content);
        } else if (slot == StoryPresenter.SLOT_NAV_BAR) {
            navBar.setWidget(content);
        } else if (slot == StoryPresenter.SLOT_NEW_CONTENT) {
            newContent.setWidget(content);
        } else if (slot == StoryPresenter.SLOT_DOCUMENTS) {
            documents.setWidget(content);
        }
    }
}
