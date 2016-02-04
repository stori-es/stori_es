package org.consumersunion.stories.dashboard.client.application.ui;

import java.util.List;

import org.consumersunion.stories.common.client.util.WidgetIds;

import com.google.common.collect.Lists;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class MapToggler implements IsWidget, ClickHandler, AttachEvent.Handler {
    interface Binder extends UiBinder<Widget, MapToggler> {
    }

    public interface MapTogglerHandler {
        void onSwitchToMap();

        void onSwitchToList();
    }

    private static final String MAP_ICON = "icon-map-marker";
    private static final String LIST_ICON = "icon-th-list icon-rotate-180";

    @UiField
    Label mapButton;
    @UiField
    InlineLabel mapLabel;
    @UiField
    HTMLPanel main;

    private final Widget widget;
    private final List<MapTogglerHandler> handlers = Lists.newArrayList();

    private boolean visible;

    @Inject
    MapToggler(Binder binder) {
        widget = binder.createAndBindUi(this);

        mapButton.addStyleName(MAP_ICON);

        mapButton.getElement().setId(WidgetIds.MAP_ICON_BUTTON);
        mapLabel.getElement().setId(WidgetIds.MAP_LABEL);

        main.sinkEvents(Event.ONCLICK);
        main.addHandler(this, ClickEvent.getType());
        main.addAttachHandler(this);
    }

    public void setVisible(boolean visible) {
        widget.setVisible(visible);
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            setToList();
            visible = false;
        }
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (visible) {
            switchToList();
        } else {
            switchToMap();
        }

        visible = !visible;
    }

    public HandlerRegistration addHandler(final MapTogglerHandler handler) {
        handlers.add(handler);

        return new HandlerRegistration() {
            @Override
            public void removeHandler() {
                handlers.remove(handler);
            }
        };
    }

    // For UiBinder
    public void setAddStyleNames(String styleName) {
        widget.addStyleName(styleName);
    }

    private void switchToList() {
        setToList();

        for (MapTogglerHandler handler : handlers) {
            handler.onSwitchToList();
        }
    }

    private void setToList() {
        mapButton.removeStyleName(LIST_ICON);
        mapButton.addStyleName(MAP_ICON);
        mapLabel.setText("Map");
    }

    private void switchToMap() {
        mapButton.removeStyleName(MAP_ICON);
        mapButton.addStyleName(LIST_ICON);
        mapLabel.setText("List");

        for (MapTogglerHandler handler : handlers) {
            handler.onSwitchToMap();
        }
    }
}
