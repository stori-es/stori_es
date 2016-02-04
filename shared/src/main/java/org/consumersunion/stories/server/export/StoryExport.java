package org.consumersunion.stories.server.export;

import java.util.List;

public class StoryExport<T> {
    private final List<T> values;
    private final int total;

    public StoryExport(
            List<T> values,
            int total) {
        this.values = values;
        this.total = total;
    }

    public List<T> getValues() {
        return values;
    }

    public int getTotal() {
        return total;
    }
}
