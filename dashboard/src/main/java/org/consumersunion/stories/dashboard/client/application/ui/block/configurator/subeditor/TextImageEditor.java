package org.consumersunion.stories.dashboard.client.application.ui.block.configurator.subeditor;

import org.consumersunion.stories.common.client.i18n.SizeAndPositionLabels;
import org.consumersunion.stories.common.client.widget.RadioButtonGroup;
import org.consumersunion.stories.common.shared.model.document.TextImage;
import org.consumersunion.stories.dashboard.client.application.ui.block.configurator.fileupload.ImageUploader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

import static org.consumersunion.stories.common.shared.model.document.TextImage.Position;
import static org.consumersunion.stories.common.shared.model.document.TextImage.Size;

public class TextImageEditor extends Composite implements LeafValueEditor<TextImage> {
    interface TextImageEditorUiBinder extends UiBinder<HTMLPanel, TextImageEditor> {
    }

    private static final TextImageEditorUiBinder BINDER = GWT.create(TextImageEditorUiBinder.class);
    private static final SizeAndPositionLabels LABELS = GWT.create(SizeAndPositionLabels.class);

    @UiField(provided = true)
    final RadioButtonGroup<Size> size;
    @UiField(provided = true)
    final RadioButtonGroup<Position> position;

    @UiField
    ImageUploader url;
    @UiField
    TextArea caption;
    @UiField
    TextBox altText;

    public TextImageEditor() {
        size = new RadioButtonGroup<Size>(this.<Size>createRenderer(), "size");
        size.add(Size.values());
        size.setValue(Size.DEFAULT);
        position = new RadioButtonGroup<Position>(this.<Position>createRenderer(), "position");
        position.add(Position.values());
        position.setValue(Position.DEFAULT);

        initWidget(BINDER.createAndBindUi(this));
    }

    @Override
    public void setValue(TextImage textImage) {
        if (textImage == null) {
            return;
        }
        url.setValue(textImage.getUrl());
        caption.setValue(textImage.getCaption());
        altText.setValue(textImage.getAltText());
        size.setValue(textImage.getSize());
        position.setValue(textImage.getPosition());
    }

    @Override
    public TextImage getValue() {
        TextImage textImage = new TextImage();

        textImage.setUrl(url.getValue());
        textImage.setCaption(caption.getValue());
        textImage.setAltText(altText.getValue());
        textImage.setSize(size.getValue());
        textImage.setPosition(position.getValue());

        return textImage;
    }

    public void displayErrorMessage() {
        url.displayErrorMessage();
    }

    public void resetErrors() {
        url.resetErrors();
    }

    private <T extends Enum<T>> Renderer<T> createRenderer() {
        return new AbstractRenderer<T>() {
            @Override
            public String render(T object) {
                return LABELS.getString(object.name());
            }
        };
    }
}
