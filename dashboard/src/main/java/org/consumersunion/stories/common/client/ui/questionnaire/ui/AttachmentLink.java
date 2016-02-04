package org.consumersunion.stories.common.client.ui.questionnaire.ui;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * A component of {@link AttachmentQUestion}; specifically, the 'link view' used to display existing attachments. See
 * {@link AttachmentInput}.
 */
public class AttachmentLink extends Composite {
    interface Binder extends UiBinder<Widget, AttachmentLink> {
    }

    private static final Binder uiBinder = GWT.create(Binder.class);

    @UiField
    AnchorElement link;
    @UiField
    Button remove;

    public AttachmentLink(String value) {
        initWidget(uiBinder.createAndBindUi(this));

        if (!Strings.isNullOrEmpty(value)) {
            String title = value.split("@")[0];
            String url = value.split("@")[1];
            link.setHref(url);
            link.setInnerHTML(Strings.isNullOrEmpty(title) ? url : title);
        }
    }

    public void setEnabled(Boolean enabled) {
        remove.setVisible(enabled);
    }

    public HandlerRegistration addRemoveHandler(ClickHandler handler) {
        return remove.addClickHandler(handler);
    }
}
