package org.consumersunion.stories.dashboard.client.application.ui.block.configurator.fileupload;

import org.consumersunion.stories.common.client.i18n.FormI18nMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import gwtupload.client.BaseUploadStatus;

public class CustomUploadStatus extends BaseUploadStatus {
    public interface FileTooLargeHandler {
        void onFileTooLarge();
    }

    public interface FileTypeErrorHandler {
        void onFileTypeError(String message);
    }

    private static final FormI18nMessages MESSAGES = GWT.create(FormI18nMessages.class);
    private static final UploadStatusConstants UPLOAD_CONSTANTS = GWT.create(UploadStatusConstants.class);

    private final FileTooLargeHandler fileTooLargeHandler;
    private final FileTypeErrorHandler fileTypeErrorHandler;
    private final HTMLPanel widget;

    public CustomUploadStatus(
            FileTooLargeHandler fileTooLargeHandler,
            FileTypeErrorHandler fileTypeErrorHandler) {
        this.fileTooLargeHandler = fileTooLargeHandler;
        this.fileTypeErrorHandler = fileTypeErrorHandler;
        setProgressWidget(new CustomProgress());
        widget = new HTMLPanel("");
        widget.add(new Label(MESSAGES.file()));
        widget.add(panel);
        panel.addStyleName("container");
    }

    @Override
    public void setError(String msg) {
        setStatus(Status.ERROR);
        if (msg.startsWith("The request was rejected because its size:")) {
            fileTooLargeHandler.onFileTooLarge();
        } else {
            fileTypeErrorHandler.onFileTypeError(msg);
        }
    }

    public void setText(String text) {
        if (text != null) {
            fileNameLabel.setText(text.substring(text.lastIndexOf('/') + 1));
        }

        toggleLongClass(text == null || text.isEmpty());
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    protected void addElementsToPanel() {
        // Intentionally left blank
    }

    @Override
    protected void setProgressWidget(Widget progress) {
        panel.add(fileNameLabel);
        super.setProgressWidget(progress);
        cancelLabel.addStyleName("icon-remove");
        panel.add(cancelLabel);
    }

    @Override
    protected void updateStatusPanel(boolean showProgress, String statusMessage) {
        showProgress = !UPLOAD_CONSTANTS.uploadStatusSuccess().equals(statusMessage);
        toggleLongClass(showProgress);

        super.updateStatusPanel(showProgress, statusMessage);
    }

    @Override
    protected Panel getPanel() {
        return new HTMLPanel("");
    }

    private void toggleLongClass(boolean remove) {
        GQuery.$(fileNameLabel).toggleClass("long", !remove);
    }
}
