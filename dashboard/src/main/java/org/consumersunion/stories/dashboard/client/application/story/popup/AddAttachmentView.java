package org.consumersunion.stories.dashboard.client.application.story.popup;

import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;

public class AddAttachmentView extends PopupViewWithUiHandlers<AddAttachmentUiHandlers>
        implements AddAttachmentPresenter.MyView {
    interface Binder extends UiBinder<PopupPanel, AddAttachmentView> {
    }

    final String LINK_REGEX = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    @UiField
    TextBox url;
    @UiField
    TextBox text;

    @Inject
    AddAttachmentView(
            Binder uiBinder,
            EventBus eventBus) {
        super(eventBus);

        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void clearForm() {
        url.setText("");
        text.setText("");
    }

    @UiHandler("link")
    void onLinkClicked(ClickEvent event) {
        if (!Strings.isNullOrEmpty(url.getText()) && !Strings.isNullOrEmpty(text.getText())) {
            String validatedURL = validateURL();
            getUiHandlers().addAttachment(text.getText(), validatedURL);
        }
    }

    private String validateURL() {
        RegExp linkExp = RegExp.compile(LINK_REGEX);
        if (linkExp.test(url.getText().trim())) {
            return url.getText();
        } else {
            return "http://" + url.getText();
        }
    }
}
