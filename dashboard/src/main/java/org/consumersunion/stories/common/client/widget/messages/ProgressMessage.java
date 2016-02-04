package org.consumersunion.stories.common.client.widget.messages;

import javax.inject.Provider;

import org.consumersunion.stories.common.client.resource.CommonResources;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import static com.google.gwt.query.client.GQuery.$;

public class ProgressMessage implements InteractiveMessageContent {
    @UiField
    public DivElement progress;
    @UiField
    public CommonResources resources;

    protected InteractiveMessage container;

    private final UiBinder binder;
    private final Provider<? extends HasProgress> minimizedMessagesProvider;
    private final MessageFactory messageFactory;

    private Widget widget;
    private HasProgress minimizedMessage;
    private InteractiveMessage minimizedMessageWidget;
    private boolean done;

    protected <T extends UiBinder<U, O>, U extends Widget, O extends ProgressMessage, M extends HasProgress>
    ProgressMessage(
            T binder,
            Provider<M> minimizedMessagesProvider,
            MessageFactory messageFactory) {
        this.binder = binder;
        this.minimizedMessagesProvider = minimizedMessagesProvider;
        this.messageFactory = messageFactory;
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public IsWidget asMinimized() {
        if (minimizedMessage == null) {
            minimizedMessage = minimizedMessagesProvider.get();
            minimizedMessageWidget = messageFactory.createInteractiveMessage(minimizedMessage);
            if (done) {
                scheduleRemoveMinimized();
            }
        }

        return minimizedMessageWidget;
    }

    @Override
    public void setContainer(InteractiveMessage interactiveMessage) {
        container = interactiveMessage;
    }

    public void updateProgress(int count, int total) {
        $(progress).width("calc(" + (count * 100) / total + "% + " + (80 * count) / total + "px)");

        if (minimizedMessage != null) {
            minimizedMessage.updateProgress(count, total);
        }
    }

    protected void remove() {
        $(asWidget()).closest(".message-interactive")
                .find(".icon-remove")
                .click();
    }

    protected void replaceWith(Widget newMessage) {
        String styles = widget.getStyleName();
        newMessage.addStyleName(styles);
        container.replaceContent(newMessage);
    }

    protected void setToDone() {
        done = true;
        $(progress).addClass(resources.generalStyleCss().progressDone());
        scheduleRemoveMinimized();
    }

    protected final <O extends ProgressMessage> void createAndBindUi() {
        widget = (Widget) binder.createAndBindUi(this);
    }

    private void scheduleRemoveMinimized() {
        if (minimizedMessage != null) {
            minimizedMessage.setToDone();
            $(minimizedMessageWidget).delay(10000, new Function() {
                @Override
                public void f() {
                    $(this).remove();
                }
            });
        }
    }
}
