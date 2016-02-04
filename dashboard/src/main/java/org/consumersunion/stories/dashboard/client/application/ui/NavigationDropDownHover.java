package org.consumersunion.stories.dashboard.client.application.ui;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.service.datatransferobject.DropDownItem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

public class NavigationDropDownHover<T extends DropDownItem> extends SelectionDropdownBox<T> {
    public interface DropDownHandler<T> {
        void onLoadAll();

        void onLoadSpecificItem(T item);
    }

    private final StoryTellerDashboardI18nLabels labels;

    private DropDownHandler<T> dropDownHandler;
    private ListItem<T> seeAllItem;

    @Inject
    NavigationDropDownHover(StoryTellerDashboardI18nLabels labels) {
        super("", false, true, null);

        this.labels = labels;
    }

    public void loadOptions(List<T> listData, boolean seeAllEnabled) {
        items = new ArrayList<ListItem<T>>();
        for (T item : listData) {
            items.add(new ListItem<T>(this, item, item.getLabel(), item.isVisible(), null));
        }

        if (listData.isEmpty()) {
            seeAllItem = new ListItem<T>(this, null, labels.none(), false, null, "cu-italic-style");
        } else if (seeAllEnabled) {
            seeAllItem = new ListItem<T>(this, null, labels.seeAll(), true, null);
            items.add(seeAllItem);
        }

        setListItems(items, new ClickHandler() {
            public void onClick(ClickEvent event) {
                ListItem item = (ListItem) event.getSource();
                processSelectedItem(item);
            }
        });
    }

    public void setDropDownHandler(DropDownHandler<T> dropDownHandler) {
        this.dropDownHandler = dropDownHandler;
    }

    @Override
    protected void onTitleClicked(ClickEvent event) {
        dropDownHandler.onLoadAll();
    }

    private void processSelectedItem(ListItem<T> item) {
        if (!item.getDisplay().equals(labels.none())) {
            if (item.equals(seeAllItem)) {
                dropDownHandler.onLoadAll();
            } else {
                dropDownHandler.onLoadSpecificItem(item.getValue());
            }
        }
    }
}
