package org.consumersunion.stories.dashboard.client.application.ui;

import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public abstract class ConfirmationModal extends Modal {
    interface Binder extends UiBinder<Widget, ConfirmationModal> {
    }

    @Inject
    private static Binder binder;
    @Inject
    private static CommonI18nLabels labels;

    @UiField
    Label message;
    @UiField
    Button done;
    @UiField
    Anchor cancel;

    public ConfirmationModal(String title, String message) {
        super(title);

        add(binder.createAndBindUi(this));

        this.message.setText(message);

        done.setText(labels.confirm());
        done.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                handleConfirm();
                hide();
            }
        });

        cancel.setText(labels.cancel());
        cancel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                handleCancel();
                hide();
            }
        });
    }

    protected abstract void handleConfirm();

    protected void handleCancel() {
    }
}
