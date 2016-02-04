package org.consumersunion.stories.dashboard.client.application.stories.widget.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class NavigationBar extends Composite {
    interface Binder extends UiBinder<Widget, NavigationBar> {
    }

    private static final Binder binder = GWT.create(Binder.class);

    NavigationBar() {
        initWidget(binder.createAndBindUi(this));
    }
}
