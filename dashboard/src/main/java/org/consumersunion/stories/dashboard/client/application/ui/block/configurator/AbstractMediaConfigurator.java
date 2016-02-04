package org.consumersunion.stories.dashboard.client.application.ui.block.configurator;

import org.consumersunion.stories.common.shared.model.document.MediaBlock;

import com.google.common.base.Strings;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base configurator for {@link MediaBlock} elements.
 */
public abstract class AbstractMediaConfigurator extends AbstractConfigurator<MediaBlock> {
    interface Binder extends UiBinder<Widget, AbstractMediaConfigurator> {
    }

    interface Driver extends SimpleBeanEditorDriver<MediaBlock, AbstractMediaConfigurator> {
    }

    @UiField
    TextBox url;
    @UiField
    @Ignore
    Label urlError;
    @UiField
    TextBox title;
    @UiField
    TextArea description;

    protected AbstractMediaConfigurator(
            Binder uiBinder,
            Driver driver,
            MediaBlock media) {
        super(driver, media);

        initWidget(uiBinder.createAndBindUi(this));

        driver.initialize(this);
        setErrorLabels(urlError);

        init();
    }

    @Override
    protected void onDone() {
        MediaBlock media = driver.flush();

        if (!driver.hasErrors() && !url.getText().trim().isEmpty()) {
            verifyMediaValidity(media);
        } else {
            urlError.setText(messages.requiredField());
        }
    }

    @Override
    public boolean isNew() {
        return Strings.isNullOrEmpty(getEditedValue().getUrl());
    }

    /**
     * Verify the validity of the given media. Must call {@link #onSuccess(MediaBlock)} if the media is valid.
     */
    protected abstract void verifyMediaValidity(MediaBlock media);

    protected void onSuccess(MediaBlock media) {
        doneCallback.onSuccess(media);
    }

    protected void onFailure(String error) {
        doneCallback.onFailure(error);
    }
}
