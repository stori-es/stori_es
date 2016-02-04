package org.consumersunion.stories.common.shared.dto;

import java.util.Date;
import java.util.Map;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.rpc.IsSerializable;

public class StoriesCountByDate implements IsSerializable {
    private Map<Date, Integer> countByDate;

    public StoriesCountByDate() {
        countByDate = Maps.newTreeMap();
    }

    public void add(Date date, int count) {
        countByDate.put(date, MoreObjects.firstNonNull(countByDate.get(date), 0) + count);
    }

    public Map<Date, Integer> getCountByDate() {
        return countByDate;
    }

    public void setCountByDate(Map<Date, Integer> countByDate) {
        this.countByDate = countByDate;
    }
}
