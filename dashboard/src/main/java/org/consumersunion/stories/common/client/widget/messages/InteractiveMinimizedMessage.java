package org.consumersunion.stories.common.client.widget.messages;

import org.consumersunion.stories.common.client.i18n.CommonI18nMessages;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public abstract class InteractiveMinimizedMessage implements InteractiveMessageContent, HasProgress {
    interface Binder extends UiBinder<Widget, InteractiveMinimizedMessage> {
    }

    @UiField
    HTML main;

    protected final CommonI18nMessages messages;

    private final Widget widget;

    @Inject
    InteractiveMinimizedMessage(
            Binder binder,
            CommonI18nMessages messages) {
        this.messages = messages;
        widget = binder.createAndBindUi(this);

        updateProgress(0);
    }

    @Override
    public void updateProgress(int count, int total) {
        updateProgress((count * 100) / total);
    }

    @Override
    public void setToDone() {
        main.setText(getDoneText());
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

    protected abstract String getDoneText();

    protected abstract String getProgressText(int percent);

    private void updateProgress(int percent) {
        main.setText(getProgressText(percent));
    }
}
