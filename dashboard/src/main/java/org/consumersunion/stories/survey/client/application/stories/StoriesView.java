package org.consumersunion.stories.survey.client.application.stories;

import java.util.List;

import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.survey.client.application.stories.ui.StoryCell;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class StoriesView extends ViewImpl implements StoriesPresenter.MyView {
    interface Binder extends UiBinder<Widget, StoriesView> {
    }

    @UiField(provided = true)
    final CellList<StorySummary> storyList;

    private final ListDataProvider<StorySummary> dataProvider;

    @Inject
    StoriesView(
            Binder uiBinder,
            StoryCell storyCell,
            ListDataProvider<StorySummary> dataProvider) {
        storyList = new CellList<StorySummary>(storyCell);
        this.dataProvider = dataProvider;

        initWidget(uiBinder.createAndBindUi(this));
        dataProvider.addDataDisplay(storyList);
    }

    @Override
    public void setData(List<StorySummary> data) {
        dataProvider.getList().clear();
        dataProvider.getList().addAll(data);
        dataProvider.refresh();
    }
}
