package org.consumersunion.stories.common.client.util;

import javax.inject.Inject;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.inject.assistedinject.Assisted;

public class CustomHasRows implements HasRows {
    private final HandlerManager handlerManager;

    private int count;
    private boolean isExact;
    private Range visibleRange;

    @Inject
    CustomHasRows(@Assisted HasHandlers hasHandlers) {
        handlerManager = new HandlerManager(hasHandlers);

        isExact = true;
        visibleRange = new Range(0, 1);
    }

    @Override
    public HandlerRegistration addRangeChangeHandler(RangeChangeEvent.Handler handler) {
        return handlerManager.addHandler(RangeChangeEvent.getType(), handler);
    }

    @Override
    public HandlerRegistration addRowCountChangeHandler(RowCountChangeEvent.Handler handler) {
        return handlerManager.addHandler(RowCountChangeEvent.getType(), handler);
    }

    @Override
    public int getRowCount() {
        return count;
    }

    @Override
    public Range getVisibleRange() {
        return visibleRange;
    }

    @Override
    public boolean isRowCountExact() {
        return isExact;
    }

    @Override
    public void setRowCount(int count) {
        setRowCount(count, true);
    }

    @Override
    public void setRowCount(int count, boolean isExact) {
        this.count = count;
        this.isExact = isExact;
        RowCountChangeEvent.fire(this, count, isExact);
    }

    @Override
    public void setVisibleRange(int start, int length) {
        setVisibleRange(new Range(start, length));
    }

    @Override
    public void setVisibleRange(Range range) {
        visibleRange = range;
        RangeChangeEvent.fire(this, visibleRange);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }
}
