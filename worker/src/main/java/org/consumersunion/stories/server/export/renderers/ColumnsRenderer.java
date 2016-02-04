package org.consumersunion.stories.server.export.renderers;

import java.util.List;

public interface ColumnsRenderer<T> {
    List<String> render(T data);

    List<String> getColumns();
}
