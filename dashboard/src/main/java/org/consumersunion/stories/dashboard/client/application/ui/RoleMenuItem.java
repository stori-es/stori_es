package org.consumersunion.stories.dashboard.client.application.ui;

import org.consumersunion.stories.common.shared.model.SortDropDownItem;
import org.consumersunion.stories.common.shared.model.entity.SortField;
import org.consumersunion.stories.common.shared.service.datatransferobject.DropDownType;

public class RoleMenuItem implements SortDropDownItem {
    private boolean visible;

    public RoleMenuItem() {
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public DropDownType getDropDownType() {
        return DropDownType.MENU_ITEM;
    }

    @Override
    public String getLabel() {
        return "";
    }

    @Override
    public SortField getSortField() {
        return null;
    }
}
