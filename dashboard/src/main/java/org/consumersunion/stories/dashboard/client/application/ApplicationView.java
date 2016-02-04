package org.consumersunion.stories.dashboard.client.application;

import org.consumersunion.stories.common.client.widget.ExceptionPopup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class ApplicationView extends ViewWithUiHandlers<ApplicationUiHandlers> implements ApplicationPresenter.MyView {
    interface Binder extends UiBinder<Widget, ApplicationView> {
    }

    @UiField
    SimplePanel main;
    @UiField
    SimplePanel messagePanel;

    private final ExceptionPopup exceptionPopup;

    @Inject
    ApplicationView(
            Binder uiBinder,
            ExceptionPopup exceptionPopup) {
        this.exceptionPopup = exceptionPopup;

        initWidget(uiBinder.createAndBindUi(this));

        main.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().renewSession();
            }
        }, ClickEvent.getType());
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == ApplicationPresenter.SLOT_MAIN_CONTENT) {
            main.setWidget(content);
        } else if (slot == ApplicationPresenter.TYPE_SetMessagesContent) {
            messagePanel.setWidget(content);
        }
    }

    @Override
    public void showErrorPopup(String message) {
        exceptionPopup.setMessage(message);
        if (!exceptionPopup.isVisible()) {
            exceptionPopup.center();
        }
    }
}
