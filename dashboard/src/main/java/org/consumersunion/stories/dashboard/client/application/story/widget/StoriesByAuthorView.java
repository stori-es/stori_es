package org.consumersunion.stories.dashboard.client.application.story.widget;

import java.util.List;

import org.consumersunion.stories.common.client.ui.TextPager;
import org.consumersunion.stories.common.shared.i18n.CommonI18nLabels;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.dashboard.client.application.story.ui.StoryByAuthorCell;

import com.google.common.base.Strings;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public class StoriesByAuthorView extends ViewWithUiHandlers<StoriesByAuthorUiHandlers>
        implements StoriesByAuthorPresenter.MyView {
    interface Binder extends UiBinder<Widget, StoriesByAuthorView> {
    }

    @UiField(provided = true)
    final CellList<StorySummary> storiesList;
    @UiField(provided = true)
    final TextPager pager;

    @UiField
    Label authorLabel;

    private final CommonI18nLabels labels;
    private final AsyncDataProvider<StorySummary> dataProvider;
    private final SingleSelectionModel<StorySummary> selectionModel;

    @Inject
    StoriesByAuthorView(
            Binder uiBinder,
            CommonI18nLabels labels,
            TextPager textPager,
            StoryByAuthorCell storyByAuthorCell) {
        this.labels = labels;
        this.pager = textPager;
        this.dataProvider = setupDataProvider();
        this.storiesList = new CellList<StorySummary>(storyByAuthorCell);
        this.selectionModel = new SingleSelectionModel<StorySummary>();

        initWidget(uiBinder.createAndBindUi(this));
        dataProvider.addDataDisplay(storiesList);
        storiesList.setSelectionModel(selectionModel);

        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent selectionChangeEvent) {
                getUiHandlers().storyDetails(selectionModel.getSelectedObject());
            }
        });

        pager.setDisplay(storiesList);
        pager.setPageSize(StoriesByAuthorPresenter.PAGE_SIZE);

        $("button", textPager).first().hide();
        $("button", textPager).last().hide();
    }

    @Override
    public void setData(List<StorySummary> data, int start, int totalCount) {
        dataProvider.updateRowData(start, data);
        dataProvider.updateRowCount(totalCount, true);
    }

    @Override
    public void displayAuthorName(Profile profile) {
        authorLabel.setText(labels.storiesBy() + " " + getUserName(profile.getGivenName(), profile.getSurname()));
    }

    public String getUserName(String givenName, String surName) {
        String fullName;
        if (!Strings.isNullOrEmpty(givenName)) {
            fullName = givenName + " ";
        } else {
            fullName = "? ";
        }

        if (!Strings.isNullOrEmpty(surName)) {
            fullName = fullName + surName;
        } else {
            fullName = fullName + "?";
        }

        if (fullName.contains("? ?")) {
            fullName = "anonymous";
        }

        return fullName;
    }

    private AsyncDataProvider<StorySummary> setupDataProvider() {
        ProvidesKey<StorySummary> providesKey = new ProvidesKey<StorySummary>() {
            @Override
            public Object getKey(StorySummary item) {
                return item.getStory();
            }
        };

        return new AsyncDataProvider<StorySummary>(providesKey) {
            @Override
            protected void onRangeChanged(HasData<StorySummary> display) {
                Range range = display.getVisibleRange();

                if (getUiHandlers() != null) {
                    getUiHandlers().loadStories(range.getStart(), range.getLength());
                }
            }
        };
    }
}
