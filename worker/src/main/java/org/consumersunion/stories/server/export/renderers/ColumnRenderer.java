package org.consumersunion.stories.server.export.renderers;

public interface ColumnRenderer<T> {
    String render(T data);
}
