package org.consumersunion.stories.common.client.ui.block.content;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class CollectionContentView extends ViewImpl implements CollectionContentPresenter.MyView {
    interface Binder extends UiBinder<Widget, CollectionContentView> {
    }

    @UiField
    SimplePanel collectionContent;
    @UiField
    Label noStories;

    @Inject
    CollectionContentView(Binder uiBinder,
            StoryTellerDashboardI18nLabels labels) {
        initWidget(uiBinder.createAndBindUi(this));

        noStories.setText(labels.noStoriesInCollection());
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == CollectionContentPresenter.SLOT_STORIES) {
            collectionContent.setWidget(content);
        }
    }

    @Override
    public void setNoStories(boolean noStories) {
        this.noStories.setVisible(noStories);
    }
}
