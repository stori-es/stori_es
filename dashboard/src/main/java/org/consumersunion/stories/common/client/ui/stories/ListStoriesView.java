package org.consumersunion.stories.common.client.ui.stories;

import org.consumersunion.stories.common.client.ui.DefaultTextPagerHandler;
import org.consumersunion.stories.common.client.ui.EnhancedPager;
import org.consumersunion.stories.common.client.ui.LoadingIndicator;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasRows;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class ListStoriesView extends ViewWithUiHandlers<ListStoriesUiHandlers>
        implements ListStoriesPresenter.MyView, EnhancedPager.Handler {
    interface Binder extends UiBinder<Widget, ListStoriesView> {
    }

    @UiField(provided = true)
    final EnhancedPager pager;
    @UiField(provided = true)
    final LoadingIndicator loadingIndicator;

    @UiField
    HTMLPanel storiesList;

    @Inject
    ListStoriesView(Binder uiBinder,
            LoadingIndicator loadingIndicator,
            EnhancedPager enhancedPager) {
        this.loadingIndicator = loadingIndicator;
        pager = enhancedPager;

        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void initPager(int defaultPageSize) {
        pager.getPager().setPageSize(defaultPageSize);
        reloadData();
    }

    @Override
    public void clear() {
        storiesList.clear();
    }

    @Override
    public void setupPager(HasRows rowHandler) {
        pager.getPager().setHandler(new DefaultTextPagerHandler() {
            @Override
            public void onPageChanged() {
                onGoToPage(pager.getPager().getPage() + 1);
            }
        });
        pager.setHandler(this);

        pager.getPager().setDisplay(rowHandler);
    }

    @Override
    public IsWidget getStoriesListContainer() {
        return storiesList;
    }

    @Override
    public void reloadData() {
        pager.resetPager();
        pager.getPager().firstPage();
        fetchData();
    }

    @Override
    public void hideLoading() {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                loadingIndicator.setVisible(false);
                storiesList.setVisible(true);
            }
        });
    }

    @Override
    public void showLoading() {
        loadingIndicator.setVisible(true);
        storiesList.setVisible(false);
    }

    @Override
    public void goToPage(int page, boolean force) {
        int effectivePage = page - 1;
        if (force || effectivePage != pager.getPager().getPage()) {
            onGoToPage(page);
        }
    }

    @Override
    public int getCurrentPage() {
        return pager.getPager().getPage();
    }

    @Override
    public void onGoToPage(Integer pageNumber) {
        pager.getPager().setPage(pageNumber - 1);
        fetchData();
    }

    @Override
    public void addToSlot(Object slot, IsWidget content) {
        if (slot == ListStoriesPresenter.SLOT_STORIES) {
            storiesList.add(content);
        }
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == ListStoriesPresenter.SLOT_STORIES) {
            storiesList.clear();
            if (content != null) {
                storiesList.add(content);
            }
        }
    }

    private void fetchData() {
        getUiHandlers().loadStories(pager.getPager().getPage());
    }
}
