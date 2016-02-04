package org.consumersunion.stories.common.shared.dto;

import java.util.Map;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.rpc.IsSerializable;

public class StoriesCountByState implements IsSerializable {
    private Map<String, Integer> countByState;

    public StoriesCountByState() {
        countByState = Maps.newHashMap();
    }

    public void add(String state, int count) {
        countByState.put(state, MoreObjects.firstNonNull(countByState.get(state), 0) + count);
    }

    public Map<String, Integer> getCountByState() {
        return countByState;
    }

    public void setCountByState(Map<String, Integer> countByState) {
        this.countByState = countByState;
    }
}
