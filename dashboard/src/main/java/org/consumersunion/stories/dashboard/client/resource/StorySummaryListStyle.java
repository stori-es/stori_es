package org.consumersunion.stories.dashboard.client.resource;

import com.google.gwt.user.cellview.client.CellList;

public interface StorySummaryListStyle extends CellList.Resources {

    interface ListStyle extends CellList.Style {
    }

    @Source({CellList.Style.DEFAULT_CSS, "org/consumersunion/stories/client/resource/css/storySummaryListStyle.css"})
    ListStyle cellListStyle();
}
