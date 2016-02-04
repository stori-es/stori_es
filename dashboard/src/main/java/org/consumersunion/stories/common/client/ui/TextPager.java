package org.consumersunion.stories.common.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.AbstractPager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;

public class TextPager extends AbstractPager {
    interface Binder extends UiBinder<Widget, TextPager> {
    }

    public interface Handler {
        void onGoToFirstPage();

        void onGoToPreviousPage();

        void onGoToNextPage();

        void onGoToLastPage();

        void onPageChanged();
    }

    @UiField
    Button firstPage;
    @UiField
    Button prevPage;
    @UiField
    Button nextPage;
    @UiField
    Button lastPage;
    @UiField
    Label label;

    private Handler handler;

    @Inject
    public TextPager(final Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        setDisplay(null);

        handler = new DefaultTextPagerHandler() {
        };
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public boolean hasNextPage() {
        return getPage() < (getPageCount() - 1);
    }

    @Override
    public boolean hasNextPages(int pages) {
        return super.hasNextPages(pages);
    }

    @Override
    public boolean hasPage(int index) {
        return super.hasPage(index);
    }

    @Override
    public boolean hasPreviousPage() {
        return super.hasPreviousPage();
    }

    @Override
    public boolean hasPreviousPages(int pages) {
        return super.hasPreviousPages(pages);
    }

    @Override
    public void lastPageStart() {
        super.lastPageStart();
    }

    @Override
    public void setDisplay(HasRows display) {
        // Enable or disable all buttons.
        boolean disableButtons = (display == null);
        setNextPageButtonsDisabled(disableButtons);
        setPrevPageButtonsDisabled(disableButtons);
        super.setDisplay(display);
    }

    @Override
    public void setPage(int index) {
        super.setPage(index);
    }

    @Override
    public void setPageSize(int pageSize) {
        super.setPageSize(pageSize);
    }

    @Override
    public void setPageStart(int index) {
        if (this.getDisplay() != null) {
            Range range = this.getDisplay().getVisibleRange();
            int pageSize = range.getLength();

            index = Math.max(0, index);
            if (index != range.getStart()) {
                this.getDisplay().setVisibleRange(index, pageSize);
            }
        }
    }

    @Override
    public int getPageCount() {
        return super.getPageCount();
    }

    @Override
    public int getPage() {
        return super.getPage();
    }

    public void startLoading() {
        getDisplay().setRowCount(0, true);
        label.setText("");
    }

    @Override
    public void firstPage() {
        super.firstPage();
    }

    @UiHandler("firstPage")
    void onFirstClicked(ClickEvent event) {
        firstPage();
        handler.onGoToFirstPage();
    }

    @UiHandler("prevPage")
    void onPrevClicked(ClickEvent event) {
        previousPage();
        handler.onGoToPreviousPage();
    }

    @UiHandler("nextPage")
    void onNextClicked(ClickEvent event) {
        nextPage();
        handler.onGoToNextPage();
    }

    @UiHandler("lastPage")
    void onLastClicked(ClickEvent event) {
        lastPage();
        handler.onGoToLastPage();
    }

    protected String createText() {
        HasRows display = getDisplay();
        Range range = display.getVisibleRange();

        int pageStart = range.getStart();
        int pageSize = range.getLength();
        int dataSize = display.getRowCount();
        int morePage = (dataSize % pageSize) >= 1 ? 1 : 0;

        int currentPage = pageStart == 0 ? 1 : 1 + (pageStart / pageSize);
        int numberPages = dataSize <= pageSize ? 1 : (dataSize / pageSize) + morePage;

        if (dataSize == 0) {
            return "Page 0 of 0";
        } else {
            return "Page " + currentPage + " of " + numberPages;
        }
    }

    @Override
    protected void onRangeOrRowCountChanged() {
        HasRows display = getDisplay();
        label.setText(createText());

        // Update the prev and first buttons.
        setPrevPageButtonsDisabled(!hasPreviousPage());

        // Update the next and last buttons.
        if (isRangeLimited() || !display.isRowCountExact()) {
            setNextPageButtonsDisabled(!hasNextPage());
        }
    }

    boolean isNextButtonDisabled() {
        return !nextPage.isEnabled();
    }

    boolean isPreviousButtonDisabled() {
        return !prevPage.isEnabled();
    }

    private void setNextPageButtonsDisabled(boolean disabled) {
        nextPage.setEnabled(!disabled);
        if (lastPage != null) {
            lastPage.setEnabled(!disabled);
        }
    }

    private void setPrevPageButtonsDisabled(boolean disabled) {
        firstPage.setEnabled(!disabled);
        prevPage.setEnabled(!disabled);
    }
}
