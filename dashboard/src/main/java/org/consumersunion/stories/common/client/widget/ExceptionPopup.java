package org.consumersunion.stories.common.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ExceptionPopup extends PopupPanel {
    interface Binder extends UiBinder<Widget, ExceptionPopup> {
    }

    @UiField
    Label message;

    @Inject
    public ExceptionPopup(final Binder uiBinder) {
        setWidget(uiBinder.createAndBindUi(this));

        setVisible(false);
        setGlassEnabled(true);
        setModal(true);
        setAutoHideEnabled(false);
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    @UiHandler("reload")
    void onReloadClicked(ClickEvent event) {
        Window.Location.reload();
    }
}
