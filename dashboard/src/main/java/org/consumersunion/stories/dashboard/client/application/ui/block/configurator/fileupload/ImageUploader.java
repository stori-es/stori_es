package org.consumersunion.stories.dashboard.client.application.ui.block.configurator.fileupload;

import org.consumersunion.stories.common.client.i18n.FormI18nMessages;
import org.consumersunion.stories.common.client.resource.CommonResources;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import gwtupload.client.FileList;
import gwtupload.client.IFileInput;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;
import gwtupload.client.dnd.DropZoneButtonFileInput;

import static com.google.gwt.query.client.GQuery.$;

public class ImageUploader extends Composite implements LeafValueEditor<String>, IUploader.OnFinishUploaderHandler,
        IUploader.OnCancelUploaderHandler, IUploader.OnStatusChangedHandler, CustomUploadStatus.FileTooLargeHandler,
        IUploader.OnStartUploaderHandler, CustomUploadStatus.FileTypeErrorHandler, ChangeHandler {
    interface Binder extends UiBinder<HTMLPanel, ImageUploader> {
    }

    private static final Binder BINDER = GWT.create(Binder.class);
    private static final CommonResources COMMON_RESOURCES = GWT.create(CommonResources.class);
    private static final FormI18nMessages MESSAGES = GWT.create(FormI18nMessages.class);
    private static final long MAX_FILE_SIZE = 4194304L;

    private final CustomUploadStatus uploadStatus;
    private final ImageDropZone dropZone;

    private SingleUploader uploader;
    private String url;

    @UiField
    HTMLPanel root;

    public ImageUploader() {
        uploadStatus = new CustomUploadStatus(this, this);
        dropZone = new ImageDropZone();
        url = "";

        Button button = new Button(MESSAGES.chooseImageToUpload());

        button.addStyleName(COMMON_RESOURCES.generalStyleCss().uploadBtn());

        initWidget(BINDER.createAndBindUi(this));
    }

    private void initUploader() {
        uploader = new SingleUploader(
                IFileInput.FileInputType.CUSTOM.withZone(dropZone).with(dropZone),
                uploadStatus,
                null);
        uploader.setAutoSubmit(false);
        uploader.setValidExtensions(".jpg, .gif, .png");

        // handlers
        uploader.addOnFinishUploadHandler(this);
        uploader.addOnCancelUploadHandler(this);
        uploader.addOnStatusChangedHandler(this);
        uploader.addOnStartUploadHandler(this);
        uploader.getFileInput().addChangeHandler(this);

        root.add(uploader);

        toggleDropZone(Strings.isNullOrEmpty(url));

        resetErrors();
    }

    @Override
    protected void onAttach() {
        super.onAttach();

        initUploader();
    }

    @Override
    protected void onDetach() {
        super.onDetach();

        root.clear();
    }

    @Override
    public String getValue() {
        return url;
    }

    @Override
    public void setValue(String value) {
        url = value;
        uploadStatus.setText(value);
        toggleDropZone(Strings.isNullOrEmpty(url));
    }

    @Override
    public void onFinish(IUploader uploader) {
        url = Strings.nullToEmpty(uploader.getServerMessage().getMessage());
    }

    @Override
    public void onCancel(IUploader uploader) {
        setValue("");
    }

    @Override
    public void onStatusChanged(IUploader uploader) {
        if (Status.ERROR.equals(uploader.getStatus())) {
            toggleDropZone(true);
        } else if (Status.SUCCESS.equals(uploader.getStatus())) {
            toggleDropZone(false);
        }
    }

    @Override
    public void onChange(ChangeEvent event) {
        DropZoneButtonFileInput fileInput = (DropZoneButtonFileInput) uploader.getFileInput();
        FileList files = fileInput.getFiles();
        boolean hasSelectedFile = hasFile();
        boolean hasDropFile = fileInput.hasFiles();
        if (hasSelectedFile && isTooLarge() || !hasSelectedFile && hasDropFile && isTooLarge(files.item(0))) {
            onFileTooLarge();
        } else {
            uploader.submit();
        }
    }

    @Override
    public void onFileTooLarge() {
        displayErrorMessage(MESSAGES.maximumFileSize());
    }

    @Override
    public void onFileTypeError(String message) {
        displayErrorMessage();
    }

    @Override
    public void onStart(IUploader uploader) {
        dropZone.toggleErrorStyle(false);
    }

    public void resetErrors() {
        displayStandardMessage(MESSAGES.maximumFileSize());
    }

    public void displayErrorMessage() {
        displayErrorMessage(MESSAGES.pleaseChooseImage());
    }

    private void toggleDropZone(boolean dropZoneVisible) {
        dropZone.setVisible(dropZoneVisible);
        uploadStatus.setVisible(!dropZoneVisible);
    }

    private void displayErrorMessage(String errorMessage) {
        clearFiles();
        dropZone.displayInstruction(errorMessage, true);
    }

    private void displayStandardMessage(String standardMessage) {
        dropZone.displayInstruction(standardMessage, false);
    }

    private void clearFiles() {
        $(getFileInputElement()).val("");
    }

    private boolean hasFile() {
        return hasFile(getFileInputElement());
    }

    private Element getFileInputElement() {
        return $("input[type=\"file\"]", uploader.getFileInput().asWidget()).get(0);
    }

    private native boolean hasFile(Element element) /*-{
        return element.files.length > 0;
    }-*/;

    private boolean isTooLarge(Object file) {
        return isTooLarge(file, MAX_FILE_SIZE);
    }

    private boolean isTooLarge() {
        Object file = getFirstFile(getFileInputElement());

        return isTooLarge(file, MAX_FILE_SIZE);
    }

    private native Object getFirstFile(Element element) /*-{
        return element.files[0];
    }-*/;

    private native boolean isTooLarge(Object element, Long maxSize) /*-{
        return element.size > maxSize;
    }-*/;
}
