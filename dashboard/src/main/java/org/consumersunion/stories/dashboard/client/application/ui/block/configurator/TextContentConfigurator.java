package org.consumersunion.stories.dashboard.client.application.ui.block.configurator;

import org.consumersunion.stories.common.client.ui.form.RichTextToolbar;
import org.consumersunion.stories.common.client.ui.form.controls.RichTextEditor;
import org.consumersunion.stories.common.shared.model.document.Content;

import com.google.common.base.Strings;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class TextContentConfigurator extends AbstractConfigurator<Content> implements BlockConfigurator<Content> {
    interface Binder extends UiBinder<Widget, TextContentConfigurator> {
    }

    interface Driver extends SimpleBeanEditorDriver<Content, TextContentConfigurator> {
    }

    @UiField(provided = true)
    final RichTextToolbar toolbar;

    @UiField
    RichTextEditor content;
    @UiField
    @Ignore
    Label textError;
    @UiField
    @Path("content")
    TextArea contentText;

    @Inject
    TextContentConfigurator(
            Binder uiBinder,
            Driver driver,
            RichTextToolbar toolbar,
            @Assisted Content content) {
        super(driver, content);

        this.toolbar = toolbar;

        initWidget(uiBinder.createAndBindUi(this));

        if (Content.TextType.HTML.equals(content.getTextType())) {
            toolbar.initialize(this.content);
            contentText.removeFromParent();
        } else {
            toolbar.removeFromParent();
            this.content.removeFromParent();
        }

        driver.initialize(this);

        setErrorLabels(textError);
        init();
    }

    @Override
    public boolean validate() {
        Content content = driver.flush();

        if (!driver.hasErrors() && !Strings.isNullOrEmpty(content.getContent())) {
            resetErrors();
            return true;
        } else {
            textError.setText(messages.requiredField());
            return false;
        }
    }

    @Override
    public boolean isNew() {
        return Strings.isNullOrEmpty(getEditedValue().getContent());
    }

    @Override
    protected void onDone() {
        if (validate()) {
            Content content = driver.flush();
            Content oldContent = getEditedValue();
            content.setFormType(oldContent.getFormType());
            content.setStandardMeaning(oldContent.getStandardMeaning());

            setContentText(content);

            doneCallback.onSuccess(content);

            setEditedValue(content);
        }
    }

    private void setContentText(Content content) {
        if (this.content.isAttached()) {
            content.setContent(this.content.getValue());
        } else {
            content.setContent(contentText.getValue());
        }
    }
}
