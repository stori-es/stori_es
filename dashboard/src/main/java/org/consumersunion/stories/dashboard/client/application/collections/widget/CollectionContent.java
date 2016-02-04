package org.consumersunion.stories.dashboard.client.application.collections.widget;

import org.consumersunion.stories.common.shared.model.Locale;

public class CollectionContent {
    private final String title;
    private final String summary;
    private final Locale locale;

    public CollectionContent(String title, String summary, Locale locale) {
        this.title = title;
        this.summary = summary;
        this.locale = locale;
    }

    CollectionContent() {
        this(null, null, null);
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public Locale getLocale() {
        return locale;
    }

    public boolean isValid() {
        return title != null;
    }
}
