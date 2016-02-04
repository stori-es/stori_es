package org.consumersunion.stories.dashboard.client.application.widget;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class FooterView extends Composite {
    interface Binder extends UiBinder<Widget, FooterView> {
    }

    @Inject
    FooterView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
}
