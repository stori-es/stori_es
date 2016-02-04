package org.consumersunion.stories.common.client.event;

import org.consumersunion.stories.common.client.widget.MessageStyle;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class AlertMessageEvent extends GwtEvent<AlertMessageEvent.AlertMessageHandler> {
    public interface AlertMessageHandler extends EventHandler {
        void onAlertMessage(AlertMessageEvent event);
    }

    public static final Type<AlertMessageHandler> TYPE = new Type<AlertMessageHandler>();

    private final MessageStyle messageStyle;
    private final String content;

    private AlertMessageEvent(MessageStyle messageStyle, String content) {
        this.messageStyle = messageStyle;
        this.content = content;
    }

    public static void fire(HasHandlers source, MessageStyle messageStyle, String content) {
        source.fireEvent(new AlertMessageEvent(messageStyle, content));
    }

    public MessageStyle getMessageStyle() {
        return messageStyle;
    }

    public String getContent() {
        return content;
    }

    @Override
    public Type<AlertMessageHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AlertMessageHandler handler) {
        handler.onAlertMessage(this);
    }
}
