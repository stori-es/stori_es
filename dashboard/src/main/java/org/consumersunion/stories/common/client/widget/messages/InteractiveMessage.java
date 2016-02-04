package org.consumersunion.stories.common.client.widget.messages;

import org.consumersunion.stories.common.client.widget.MessageStyle;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class InteractiveMessage extends SimpleMessage implements InteractiveMessageContent {
    private final InteractiveMessageContent messageContent;

    @Inject
    InteractiveMessage(
            Binder uiBinder,
            MessagesContainer messagesContainer,
            @Assisted InteractiveMessageContent content) {
        super(uiBinder, MessageStyle.INTERACTIVE, content, messagesContainer);

        this.messageContent = content;
        setContainer(this);
    }

    @Override
    public IsWidget asMinimized() {
        return messageContent.asMinimized();
    }

    public void replaceContent(Widget newMessage) {
        content.setWidget(newMessage);
    }

    @Override
    public void setContainer(InteractiveMessage interactiveMessage) {
        messageContent.setContainer(interactiveMessage);
    }
}
