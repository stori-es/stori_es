package org.consumersunion.stories.dashboard.client.application.widget;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class DocumentsListView extends ViewImpl implements DocumentsListPresenter.MyView {
    interface Binder extends UiBinder<Widget, DocumentsListView> {
    }

    @UiField
    HTMLPanel documentsContainer;

    @Inject
    DocumentsListView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setVisible(boolean visible) {
        documentsContainer.setVisible(visible);
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == DocumentsListPresenter.SLOT_DOCUMENTS) {
            if (content == null) {
                documentsContainer.clear();
            } else {
                documentsContainer.add(content);
            }
        }
    }

    @Override
    public void addToSlot(Object slot, IsWidget content) {
        if (slot == DocumentsListPresenter.SLOT_DOCUMENTS) {
            documentsContainer.add(content);
        }
    }
}
