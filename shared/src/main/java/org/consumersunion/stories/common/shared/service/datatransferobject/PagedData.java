package org.consumersunion.stories.common.shared.service.datatransferobject;

import java.util.ArrayList;
import java.util.List;

public class PagedData<T> {
    private final List<T> data;

    private int totalCount;

    public PagedData() {
        data = new ArrayList<T>();
    }

    public List<T> getData() {
        return data;
    }

    public PagedData<T> setData(List<T> data) {
        this.data.clear();
        this.data.addAll(data);
        return this;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public PagedData<T> setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }
}
