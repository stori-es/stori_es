package org.consumersunion.stories.common.client.widget.messages;

import org.consumersunion.stories.common.client.i18n.CommonI18nMessages;

import com.google.inject.Inject;

public class ExportMinimizedMessage extends InteractiveMinimizedMessage {
    @Inject
    ExportMinimizedMessage(
            Binder binder,
            CommonI18nMessages messages) {
        super(binder, messages);
    }

    @Override
    protected String getDoneText() {
        return messages.exportingDone();
    }

    @Override
    protected String getProgressText(int percent) {
        return messages.exportingPercent(percent);
    }
}
