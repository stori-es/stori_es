package org.consumersunion.stories.dashboard.client.resource;

import com.google.gwt.user.cellview.client.CellList;

public interface QuestionnaireListStyle extends CellList.Resources {

    interface ListStyle extends CellList.Style {
    }

    @Source({CellList.Style.DEFAULT_CSS, "css/questionnaireListStyle.css"})
    ListStyle cellListStyle();
}
