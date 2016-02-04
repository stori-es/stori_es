package org.consumersunion.stories.common.client.widget.messages;

public interface HasProgress extends InteractiveMessageContent {
    void updateProgress(int count, int total);

    void setToDone();
}
