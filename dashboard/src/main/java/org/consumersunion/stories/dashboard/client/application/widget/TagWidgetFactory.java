package org.consumersunion.stories.dashboard.client.application.widget;

interface TagWidgetFactory {
    TagWidget create(String tag, TagWidget.TagHandler handler);
}
