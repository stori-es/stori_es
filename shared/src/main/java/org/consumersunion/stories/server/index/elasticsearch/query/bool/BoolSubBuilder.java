package org.consumersunion.stories.server.index.elasticsearch.query.bool;

import org.consumersunion.stories.server.index.elasticsearch.query.Ids;

interface BoolSubBuilder<B extends FluentBoolBuilder> extends FluentBoolBuilder {
    B withIds(Ids ids);

    B withQueryString(String queryString);

    B addTerm(String key, Object value);

    B withGeoBoundingBox(String field, String topRight, String bottomLeft);

    B withGeoDistance(String field, String distance, String location);
}
