package org.consumersunion.stories.server.api.rest.merger;

import java.util.Map;

public abstract class AbstractMerger<E, D> {
    public abstract void merge(E entity, D dto);

    public void mergeAll(Map<E, D> entityMap) {
        for (Map.Entry<E, D> entity : entityMap.entrySet()) {
            merge(entity.getKey(), entity.getValue());
        }
    }
}
