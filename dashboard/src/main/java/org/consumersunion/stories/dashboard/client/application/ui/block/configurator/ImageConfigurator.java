package org.consumersunion.stories.dashboard.client.application.ui.block.configurator;

import org.consumersunion.stories.common.shared.model.document.ImageBlock;
import org.consumersunion.stories.dashboard.client.application.ui.block.configurator.fileupload.ImageUploader;

import com.google.common.base.Strings;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class ImageConfigurator extends AbstractConfigurator<ImageBlock> implements BlockConfigurator<ImageBlock> {
    interface Binder extends UiBinder<Widget, ImageConfigurator> {
    }

    interface Driver extends SimpleBeanEditorDriver<ImageBlock, ImageConfigurator> {
    }

    @UiField
    ImageUploader url;
    @UiField
    TextBox altText;
    @UiField
    TextArea caption;

    @Inject
    ImageConfigurator(
            Binder uiBinder,
            Driver driver,
            @Assisted ImageBlock image) {
        super(driver, image);

        initWidget(uiBinder.createAndBindUi(this));
        driver.initialize(this);

        init();
    }

    @Override
    public boolean validate() {
        if (!driver.hasErrors() && !Strings.isNullOrEmpty(url.getValue())) {
            resetErrors();
            return true;
        } else {
            url.displayErrorMessage();
            return false;
        }
    }

    @Override
    public boolean isNew() {
        return Strings.isNullOrEmpty(getEditedValue().getUrl());
    }

    @Override
    protected void onDone() {
        ImageBlock image = driver.flush();
        if (validate()) {
            resetErrors();
            doneCallback.onSuccess(image);
            setEditedValue(image);
        }
    }

    @Override
    protected void resetErrors() {
        super.resetErrors();

        url.resetErrors();
    }
}
