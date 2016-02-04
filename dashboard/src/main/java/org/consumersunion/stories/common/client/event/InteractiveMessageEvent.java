package org.consumersunion.stories.common.client.event;

import org.consumersunion.stories.common.client.widget.messages.InteractiveMessageContent;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class InteractiveMessageEvent extends GwtEvent<InteractiveMessageEvent.InteractiveMessageHandler> {
    public interface InteractiveMessageHandler extends EventHandler {
        void onInteractiveMessage(InteractiveMessageEvent event);
    }

    public static final Type<InteractiveMessageHandler> TYPE = new Type<InteractiveMessageHandler>();

    private final InteractiveMessageContent content;

    private InteractiveMessageEvent(InteractiveMessageContent content) {
        this.content = content;
    }

    public static void fire(HasHandlers source, InteractiveMessageContent content) {
        source.fireEvent(new InteractiveMessageEvent(content));
    }

    public InteractiveMessageContent getContent() {
        return content;
    }

    @Override
    public Type<InteractiveMessageHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(InteractiveMessageHandler handler) {
        handler.onInteractiveMessage(this);
    }
}
