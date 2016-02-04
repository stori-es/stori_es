package org.consumersunion.stories.dashboard.client.application.ui;

public interface EditorView<T> {
    void init();

    void edit();

    T get();
}
