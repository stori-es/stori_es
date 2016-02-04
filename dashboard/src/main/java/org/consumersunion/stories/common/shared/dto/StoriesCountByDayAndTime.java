package org.consumersunion.stories.common.shared.dto;

import java.util.Map;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.rpc.IsSerializable;

public class StoriesCountByDayAndTime implements IsSerializable {
    private Map<Integer, Map<Integer, Integer>> countByDayAndTime;

    public StoriesCountByDayAndTime() {
        countByDayAndTime = Maps.newHashMap();

        for (int i = 1; i <= 7; i++) {
            countByDayAndTime.put(i, Maps.<Integer, Integer>newHashMap());
        }
    }

    /**
     * @param dayOfWeek Between 1 and 7
     * @param hourOfDay Between 1 and 24
     * @param count     count
     */
    public void add(int dayOfWeek, int hourOfDay, int count) {
        Map<Integer, Integer> countByHour = countByDayAndTime.get(dayOfWeek);
        if (!countByHour.containsKey(hourOfDay)) {
            countByHour.put(hourOfDay, 0);
        }

        countByHour.put(hourOfDay, countByHour.get(hourOfDay) + count);
    }

    public int get(int dayOfWeek, int hourOfDay) {
        return MoreObjects.firstNonNull(countByDayAndTime.get(dayOfWeek).get(hourOfDay), 0);
    }

    public Map<Integer, Map<Integer, Integer>> getCountByDayAndTime() {
        return countByDayAndTime;
    }

    public void setCountByDayAndTime(Map<Integer, Map<Integer, Integer>> countByDayAndTime) {
        this.countByDayAndTime = countByDayAndTime;
    }
}
