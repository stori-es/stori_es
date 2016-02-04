package org.consumersunion.stories.dashboard.client.application.ui.block;

import com.google.gwt.user.client.ui.IsWidget;

public interface HasValidation<T> extends IsWidget {
    boolean validate();

    boolean isNew();
}
