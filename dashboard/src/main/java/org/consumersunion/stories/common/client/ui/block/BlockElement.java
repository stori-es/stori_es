package org.consumersunion.stories.common.client.ui.block;

import com.google.gwt.user.client.ui.IsWidget;

public interface BlockElement<T> extends IsWidget {
    void display(T value);
}
