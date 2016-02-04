package org.consumersunion.stories.dashboard.client.application.ui;

import java.util.List;

import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;

/**
 * Selecting dropdown box component
 *
 * @author Machin
 */
public class SelectionDropdownBox<T> extends HorizontalPanel {
    @Inject
    private static Resources resources;

    protected final Label title;
    protected final FlowPanel listPanel;
    protected final OptionsPopup popup;

    protected List<ListItem<T>> items;

    private T selectedValue;

    public SelectionDropdownBox(
            String label,
            boolean withTriangle) {
        this(label, withTriangle, true, null);
    }

    public SelectionDropdownBox(
            String label,
            boolean withTriangle,
            boolean mouseOver,
            ClickHandler handler) {
        listPanel = new FlowPanel();
        popup = new OptionsPopup();

        setVerticalAlignment(ALIGN_MIDDLE);
        popup.setWidget(listPanel);
        title = new Label(label);

        if (handler != null) {
            title.addClickHandler(handler);
        } else {
            setTitleDefaultClickHandler();
        }

        title.addStyleName(resources.generalStyleCss().actAsLink());
        this.add(title);

        if (withTriangle) {
            title.setStyleName(resources.generalStyleCss().dropDown());

            InlineLabel caret = new InlineLabel("");
            caret.setStyleName(resources.generalStyleCss().caret());

            FlowPanel triangle = new FlowPanel();
            triangle.setStyleName(resources.generalStyleCss().dropDownToggle());
            triangle.add(caret);
            triangle.addDomHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    showDropdown();
                }
            }, ClickEvent.getType());
            this.add(triangle);
        } else {
            title.setStyleName(resources.generalStyleCss().menuBarItem());
            if (mouseOver) {
                title.addMouseOverHandler(new MouseOverHandler() {
                    @Override
                    public void onMouseOver(final MouseOverEvent event) {
                        showDropdown();
                    }
                });
            }
        }
    }

    @Override
    public void setTitle(String titleText) {
        title.setText(titleText);
    }

    public void setListItems(List<ListItem<T>> itemsToDisplay, ClickHandler handler) {
        listPanel.clear();
        for (ListItem<T> listItem : itemsToDisplay) {
            addListItem(new ListItem<T>(this, listItem.getValue(), listItem.getDisplay(), listItem.getVisible(),
                    handler, listItem.isEnabled(), listItem.getStyle()));
        }

        if (!itemsToDisplay.isEmpty()) {
            selectedValue = itemsToDisplay.get(0).getValue();
        }
    }

    public void addListItem(ListItem item) {
        if (item.getVisible()) {
            listPanel.add(item);
        }
    }

    public void setSelection(T item) {
        if (item != null) {
            for (int i = 0; i < listPanel.getWidgetCount(); i++) {
                ListItem listItem = (ListItem) listPanel.getWidget(i);
                if (item.equals(listItem.getValue())) {
                    selectedValue = item;
                    setTitle(listItem.getDisplay());
                }
            }
        }

        selectedValue = item;
    }

    public T getSelection() {
        return selectedValue;
    }

    public void hide() {
        popup.hide();
    }

    public void setTitleDefaultClickHandler() {
        title.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                onTitleClicked(event);
            }
        });
    }

    protected void onTitleClicked(ClickEvent event) {
        showDropdown();
    }

    protected void showDropdown() {
        if (hasOptions()) {
            int height = Window.getClientHeight();

            popup.setPopupPosition(this.getAbsoluteLeft(), this.getAbsoluteTop() + this.getOffsetHeight());
            popup.show();

            if (height < this.getElement().getOffsetTop() + this.getOffsetHeight() + listPanel.getOffsetHeight()) {
                int popupsize = listPanel.getOffsetHeight();

                popup.hide();

                int newTop = this.getAbsoluteTop() - (this.getOffsetHeight() + popupsize);
                popup.setPopupPosition(this.getAbsoluteLeft(), newTop < 0 ? 0 : newTop);
                popup.show();
            }
        }
    }

    protected boolean hasOptions() {
        return listPanel != null && listPanel.getWidgetCount() > 0;
    }
}
