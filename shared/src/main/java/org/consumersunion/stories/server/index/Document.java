package org.consumersunion.stories.server.index;

import java.util.Set;

public interface Document {
    int getId();

    void setId(String id);

    String getType();

    Set<Integer> getReadAuths();
}
