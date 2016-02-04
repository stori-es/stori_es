package org.consumersunion.stories.dashboard.client.application.ui.block.configurator;

import org.consumersunion.stories.common.client.ui.form.RichTextToolbar;
import org.consumersunion.stories.common.client.ui.form.controls.RichTextEditor;
import org.consumersunion.stories.common.shared.model.document.TextImageBlock;
import org.consumersunion.stories.dashboard.client.application.ui.block.configurator.subeditor.TextImageEditor;

import com.google.common.base.Strings;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class TextImageConfigurator extends AbstractConfigurator<TextImageBlock> implements
        BlockConfigurator<TextImageBlock> {
    interface Binder extends UiBinder<Widget, TextImageConfigurator> {
    }

    interface Driver extends SimpleBeanEditorDriver<TextImageBlock, TextImageConfigurator> {
    }

    @UiField(provided = true)
    final RichTextToolbar toolbar;

    @UiField
    RichTextEditor text;
    @UiField
    @Ignore
    Label textError;
    @UiField
    @Ignore
    CheckBox imageCheckbox;
    @UiField
    TextImageEditor image;

    @Inject
    TextImageConfigurator(
            Binder binder,
            Driver driver,
            RichTextToolbar toolbar,
            @Assisted TextImageBlock block) {
        super(driver, block);

        this.toolbar = toolbar;

        initWidget(binder.createAndBindUi(this));
        toolbar.initialize(this.text);
        driver.initialize(this);

        boolean hasImage = block.containsImage();
        image.setVisible(hasImage);
        imageCheckbox.setValue(hasImage);

        setErrorLabels(textError);
        init();
    }

    @Override
    public boolean validate() {
        TextImageBlock content = driver.flush();

        if (!driver.hasErrors() && !Strings.isNullOrEmpty(content.getText())) {
            resetErrors();
            if (validateImage(content)) {
                return true;
            }
        } else {
            textError.setText(messages.requiredField());
            validateImage(content);
        }

        return false;
    }

    @Override
    public boolean isNew() {
        return Strings.isNullOrEmpty(getEditedValue().getText());
    }

    @Override
    protected void onDone() {
        if (validate()) {
            TextImageBlock content = driver.flush();
            if (!imageCheckbox.getValue() || !image.isVisible()) {
                content.setImage(null);
            }

            TextImageBlock oldContent = getEditedValue();
            content.setFormType(oldContent.getFormType());
            content.setStandardMeaning(oldContent.getStandardMeaning());
            doneCallback.onSuccess(content);

            setEditedValue(content);
        }
    }

    @UiHandler("imageCheckbox")
    void onImageCheckbox(ValueChangeEvent<Boolean> event) {
        image.setVisible(event.getValue());
    }

    private boolean validateImage(TextImageBlock content) {
        boolean valid = !image.isVisible() || !Strings.isNullOrEmpty(content.getImage().getUrl());

        if (!valid) {
            image.displayErrorMessage();
        } else {
            image.resetErrors();
        }

        return valid;
    }
}
