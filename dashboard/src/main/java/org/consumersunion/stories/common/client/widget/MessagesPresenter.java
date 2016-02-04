package org.consumersunion.stories.common.client.widget;

import org.consumersunion.stories.common.client.event.AlertMessageEvent;
import org.consumersunion.stories.common.client.event.ClearMessagesEvent;
import org.consumersunion.stories.common.client.event.InteractiveMessageEvent;
import org.consumersunion.stories.common.client.widget.messages.InteractiveMessageContent;
import org.consumersunion.stories.common.client.widget.messages.MessagesContainer;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class MessagesPresenter extends PresenterWidget<MessagesPresenter.MyView>
        implements AlertMessageEvent.AlertMessageHandler, ClearMessagesEvent.ClearMessagesHandler,
        InteractiveMessageEvent.InteractiveMessageHandler {
    interface MyView extends View, MessagesContainer {
        void displayMessage(MessageStyle style, String content);

        void displayMessage(InteractiveMessageContent content);

        void clearMessages();
    }

    @Inject
    MessagesPresenter(
            EventBus eventBus,
            MyView view) {
        super(eventBus, view);
    }

    @Override
    public void onAlertMessage(AlertMessageEvent event) {
        getView().displayMessage(event.getMessageStyle(), event.getContent());
    }

    @Override
    public void onInteractiveMessage(InteractiveMessageEvent event) {
        getView().displayMessage(event.getContent());
    }

    @Override
    public void onClearMessages(ClearMessagesEvent event) {
        getView().clearMessages();
    }

    @Override
    protected void onBind() {
        super.onBind();

        addRegisteredHandler(AlertMessageEvent.TYPE, this);
        addRegisteredHandler(InteractiveMessageEvent.TYPE, this);
        addRegisteredHandler(ClearMessagesEvent.TYPE, this);
    }
}
