package org.consumersunion.stories.server.index.elasticsearch.query.bool;

import org.consumersunion.stories.server.index.elasticsearch.query.Ids;

public interface MustBuilder<I extends MustBuilder<I>> extends BoolSubBuilder<I> {
    @Override
    I withIds(Ids ids);

    @Override
    I withQueryString(String queryString);

    @Override
    I addTerm(String key, Object value);

    @Override
    I withGeoBoundingBox(String field, String topRight, String bottomLeft);
}
