package org.consumersunion.stories.dashboard.client.application.ui;

import javax.inject.Inject;

import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

public class ListItem<T> extends Label {
    @Inject
    private static Resources resources;

    private final SelectionDropdownBox<T> selectionDropdownBox;
    private final T value;
    private final String display;
    private final boolean visible;
    private final ClickHandler handler;
    private final String style;
    private final boolean enabled;

    public ListItem(
            SelectionDropdownBox<T> selectionDropdownBox,
            T value, String display,
            boolean visible,
            ClickHandler handler) {
        this(selectionDropdownBox, value, display, visible, handler, true, null);
    }

    public ListItem(
            SelectionDropdownBox<T> selectionDropdownBox,
            T value,
            String display,
            boolean visible,
            ClickHandler handler,
            boolean enabled,
            String style) {
        this.selectionDropdownBox = selectionDropdownBox;
        this.value = value;
        this.display = display;
        this.handler = handler;
        this.visible = visible;
        this.enabled = enabled;
        this.style = style;
        this.setText(display);
        this.setStyleName(resources.generalStyleCss().dropDownMenuItem());

        if (visible && enabled) {
            this.addClickHandler(createClickHandler());
        }
        if (style != null) {
            getElement().addClassName(style);
        }
        if (!enabled) {
            getElement().addClassName("unselected-option");
        }

        if (value != null && !valueIsDashes(value)) {
            getElement().addClassName(resources.generalStyleCss().actAsLink());
        }
    }

    public ListItem(
            SelectionDropdownBox<T> selectionDropdownBox,
            T value,
            String display,
            boolean visible,
            ClickHandler handler,
            String style) {
        this(selectionDropdownBox, value, display, visible, handler, true, style);
    }

    private boolean valueIsDashes(T value) {
        return value instanceof String && ((String) value).matches("^-{3,}$");
    }

    private ClickHandler createClickHandler() {
        return new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                selectionDropdownBox.setSelection(value);
                selectionDropdownBox.hide();
                handler.onClick(event);
            }
        };
    }

    public T getValue() {
        return this.value;
    }

    public String getDisplay() {
        return this.display;
    }

    public String getStyle() {
        return style;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean getVisible() {
        return visible;
    }

    @Override
    public boolean equals(Object o) {
        if ((o == null) || (!(o instanceof ListItem))) {
            return false;
        }

        ListItem oItem = (ListItem) o;

        return display.equals(oItem.display) && value == oItem.value;
    }
}
