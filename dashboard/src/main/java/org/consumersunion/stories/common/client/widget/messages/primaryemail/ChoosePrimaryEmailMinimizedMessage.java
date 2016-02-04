package org.consumersunion.stories.common.client.widget.messages.primaryemail;

import org.consumersunion.stories.common.client.widget.messages.HasProgress;
import org.consumersunion.stories.common.client.widget.messages.InteractiveMessage;
import org.consumersunion.stories.common.client.widget.messages.InteractiveMessageContent;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ChoosePrimaryEmailMinimizedMessage implements InteractiveMessageContent, HasProgress {
    interface Binder extends UiBinder<Widget, ChoosePrimaryEmailMinimizedMessage> {
    }

    private final Widget widget;

    @Inject
    ChoosePrimaryEmailMinimizedMessage(
            Binder binder) {
        widget = binder.createAndBindUi(this);

        updateProgress(0, 0);
    }

    @Override
    public void updateProgress(int count, int total) {
    }

    @Override
    public void setToDone() {
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public IsWidget asMinimized() {
        return this;
    }

    @Override
    public void setContainer(InteractiveMessage interactiveMessage) {
    }
}
