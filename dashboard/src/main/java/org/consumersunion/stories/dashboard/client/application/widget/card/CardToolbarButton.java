package org.consumersunion.stories.dashboard.client.application.widget.card;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import static com.google.gwt.query.client.GQuery.$;

public class CardToolbarButton implements IsWidget {
    interface Binder extends UiBinder<Widget, CardToolbarButton> {
    }

    private static final Binder binder = GWT.create(Binder.class);

    @UiField
    HTMLPanel main;
    @UiField
    SpanElement icon;

    private final Widget widget;
    private final String iconName;
    private final String confirmText;
    private final ToolbarButtonActionHandler actionHandler;

    public CardToolbarButton(
            String icon,
            String tooltip,
            String confirmText,
            final ToolbarButtonActionHandler actionHandler) {
        iconName = icon;
        this.confirmText = confirmText;
        this.actionHandler = actionHandler;

        widget = binder.createAndBindUi(this);

        this.icon.addClassName(icon);

        if (!Strings.isNullOrEmpty(tooltip)) {
            $(main).attr("data-tooltip", tooltip);
        }

        main.sinkEvents(Event.ONCLICK);
        main.addHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                actionHandler.onButtonClicked();
            }
        }, ClickEvent.getType());
    }

    public String getConfirmText() {
        return confirmText;
    }

    public String getIcon() {
        return iconName;
    }

    public void executeAction() {
        actionHandler.executeAction();
    }

    public void setStyleName(String stylename) {
        widget.addStyleName(stylename);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }
}
