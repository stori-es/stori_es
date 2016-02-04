package org.consumersunion.stories.dashboard.client.application.ui;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class TagsTextConverter {
    public String toText(Collection<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return "";
        }

        return Joiner.on(", ").join(tags);
    }

    public List<String> formatText(String text) {
        return Splitter.on(",").trimResults().splitToList(text);
    }
}
