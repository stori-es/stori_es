package org.consumersunion.stories.dashboard.client.resource;

import com.google.gwt.user.cellview.client.CellList;

public interface ToggleButtonStyle extends CellList.Resources {

    interface ListStyle extends CellList.Style {
    }

    @Source({CellList.Style.DEFAULT_CSS, "css/toggleButtonStyle.css"})
    ListStyle cellListStyle();
}
