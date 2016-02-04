package org.consumersunion.stories.common.client.ui;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class LoadingIndicator extends Composite {
    interface Binder extends UiBinder<Widget, LoadingIndicator> {
    }

    @Inject
    public LoadingIndicator(final Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
}