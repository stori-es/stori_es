package org.consumersunion.stories.dashboard.client.application.ui.block.configurator.fileupload;

import org.consumersunion.stories.common.client.i18n.FormI18nMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;

public class ImageDropZone extends FocusPanel {
    interface Binder extends UiBinder<HTMLPanel, ImageDropZone> {
    }

    interface Style extends CssResource {
        String root();

        String uploadBtn();

        String dragPrompt();

        String instructions();

        String error();
    }

    private static final Binder BINDER = GWT.create(Binder.class);
    private static final FormI18nMessages MESSAGES = GWT.create(FormI18nMessages.class);

    @UiField
    Button btn;
    @UiField
    HTMLPanel root;
    @UiField
    Style style;
    @UiField
    ParagraphElement instructions;

    public ImageDropZone() {
        setWidget(BINDER.createAndBindUi(this));
        instructions.setInnerText(MESSAGES.maximumFileSize());
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return btn.addClickHandler(handler);
    }

    public void toggleErrorStyle(boolean showErrorStyle) {
        GQuery.$(root).toggleClass(style.error(), showErrorStyle);
    }

    public void displayInstruction(String instruction, boolean isError) {
        toggleErrorStyle(isError);
        instructions.setInnerText(instruction);
    }
}
