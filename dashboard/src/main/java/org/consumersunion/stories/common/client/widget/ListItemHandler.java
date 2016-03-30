package org.consumersunion.stories.common.client.widget;

public interface ListItemHandler<T> {
    void onListItemClicked(T item);

    void onListItemRemoved(T item);
}
