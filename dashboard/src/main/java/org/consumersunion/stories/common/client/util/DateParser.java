package org.consumersunion.stories.common.client.util;

import java.util.Date;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;

import com.google.inject.Inject;

public class DateParser {

    final Long DAY_IN_MILLIS = 86400000l;

    final StoryTellerDashboardI18nLabels labels;

    @Inject
    public DateParser(final StoryTellerDashboardI18nLabels labels) {
        this.labels = labels;
    }

    public String parseDate(Date date) {
        Long now = System.currentTimeMillis();

        if (date == null) {
            return labels.dayAgo(0l);
        }

        Long time = date.getTime();
        Long difference = now - time;
        Long day = difference / DAY_IN_MILLIS;

        if (day == 0) {
            return labels.today();
        }
        return day >= 1 ? labels.daysAgo(day) : labels.dayAgo(day);
    }
}
