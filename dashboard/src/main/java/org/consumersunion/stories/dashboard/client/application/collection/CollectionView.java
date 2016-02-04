package org.consumersunion.stories.dashboard.client.application.collection;

import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class CollectionView extends ViewImpl implements CollectionPresenter.MyView {
    interface Binder extends UiBinder<Widget, CollectionView> {
    }

    @UiField
    Resources resource;
    @UiField
    SimplePanel mainCard;
    @UiField
    SimplePanel navBar;
    @UiField
    SimplePanel newContent;
    @UiField
    SimplePanel navContent;

    @Inject
    CollectionView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == CollectionPresenter.SLOT_CARD) {
            mainCard.setWidget(content);
        } else if (slot == CollectionPresenter.SLOT_NAV_BAR) {
            navBar.setWidget(content);
        } else if (slot == CollectionPresenter.SLOT_NEW_CONTENT) {
            newContent.setWidget(content);
        } else if (slot == CollectionPresenter.SLOT_NAV_CONTENT) {
            navContent.setWidget(content);
            navContent.setVisible(content != null);
        }
    }
}

