package org.consumersunion.stories.common.client.widget.messages;

import com.google.gwt.user.client.ui.IsWidget;

public interface InteractiveMessageContent extends IsWidget {
    IsWidget asMinimized();

    void setContainer(InteractiveMessage interactiveMessage);
}
