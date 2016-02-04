package org.consumersunion.stories.dashboard.shared;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.service.datatransferobject.DropDownItem;
import org.consumersunion.stories.common.shared.service.datatransferobject.DropDownType;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MenuItem implements DropDownItem {
    public enum Type implements IsSerializable {
        CREATE_ACCOUNT(0, "Create Account"),
        UPDATE_ACCOUNT(1, "Update Account"),
        CREATE_ORGANIZATION(2, "Create Organization"),
        UPDATE_ORGANIZATION(3, "Update Organization");

        private String label;
        private int index;

        Type(int index, String label) {
            this.index = index;
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public int getIndex() {
            return index;
        }

        public static List<Type> getActiveMenus() {
            List<Type> menus = new ArrayList<Type>();
            menus.add(Type.CREATE_ACCOUNT);
            menus.add(Type.UPDATE_ACCOUNT);
            menus.add(Type.CREATE_ORGANIZATION);
            menus.add(Type.UPDATE_ORGANIZATION);

            return menus;
        }
    }

    private Type itemType;
    private boolean visible;

    public MenuItem() {
    }

    public MenuItem(Type itemType) {
        this.itemType = itemType;
        this.visible = true;
    }

    public Type getItemType() {
        return itemType;
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
        return getItemType().getLabel();
    }
}
