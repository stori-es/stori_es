package org.consumersunion.stories.dashboard.client.application.ui;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.service.datatransferobject.DropDownItem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;

public class ClickableDropDown<T extends DropDownItem> extends SelectionDropdownBox<T> {
    public interface DropDownHandler<T> {
        void onLoadSpecificItem(T item);
    }

    private final StoryTellerDashboardI18nLabels labels;

    private DropDownHandler<T> dropDownHandler;

    @Inject
    ClickableDropDown(StoryTellerDashboardI18nLabels labels) {
        super("", true);

        this.labels = labels;
    }

    @SuppressWarnings("unchecked")
    public void loadOptions(List<T> listData, boolean seeAllEnabled) {
        items = new ArrayList<ListItem<T>>();

        for (T item : listData) {
            items.add(new ListItem<T>(this, item, item.getLabel(), item.isVisible(), null));
        }

        if (seeAllEnabled) {
            items.add(new ListItem<T>(this, null, labels.seeAll(), true, null));
        }

        setListItems(items, new ClickHandler() {
            public void onClick(ClickEvent event) {
                ListItem<T> item = (ListItem<T>) event.getSource();
                setTitle(item.getDisplay());

                if (!(item.getValue() instanceof RoleMenuItem)) {
                    dropDownHandler.onLoadSpecificItem(item.getValue());
                }
            }
        });
    }

    public void setDropDownHandler(DropDownHandler<T> dropDownHandler) {
        this.dropDownHandler = dropDownHandler;
    }

    @Override
    protected void onTitleClicked(ClickEvent event) {
        showDropdown();
    }
}
