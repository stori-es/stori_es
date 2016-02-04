package org.consumersunion.stories.common.client.ui.block;

import com.google.gwt.user.client.ui.IsWidget;

public interface ContentElement<T> extends IsWidget {
    void display(T content);
}
