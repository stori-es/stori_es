package org.consumersunion.stories.common.shared.model.metrics;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ItemCountByDate implements IsSerializable {
    private int itemCount;
    private Date startDate;

    public ItemCountByDate() {

    }

    public ItemCountByDate(Date startDate, int itemCount) {
        this.startDate = startDate;
        this.itemCount = itemCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
