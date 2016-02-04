package org.consumersunion.stories.dashboard.client.application.collections.widget;

import javax.inject.Inject;

import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class DeleteCollectionWidget implements IsWidget {
    interface Binder extends UiBinder<Widget, DeleteCollectionWidget> {
    }

    @UiField
    Resources resource;
    @UiField
    HTMLPanel main;

    private final Widget widget;

    @Inject
    DeleteCollectionWidget(Binder uiBinder) {
        widget = uiBinder.createAndBindUi(this);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }
}
