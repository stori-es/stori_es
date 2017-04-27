package org.consumersunion.stories.server.index.elasticsearch.search;

import org.consumersunion.stories.server.index.elasticsearch.SortOrder;
import org.consumersunion.stories.server.index.elasticsearch.query.Query;

public class SearchBuilder {
    public static SearchBuilder newBuilder() {
        return new SearchBuilder();
    }

    private final Search search;

    private SearchBuilder() {
        search = new Search();
    }

    public SearchBuilder withQuery(Query query) {
        search.setQuery(query);
        return this;
    }

    public SearchBuilder withFrom(long from) {
        search.setFrom(from);
        return this;
    }

    public SearchBuilder withSize(long size) {
        search.setSize(size);
        return this;
    }

    public SearchBuilder withSort(String key, SortOrder sortOrder) {
        search.addSort(key, sortOrder);
        return this;
    }

    public SearchBuilder withoutFields() {
        search.withoutFields();
        return this;
    }

    public static Search ofQuery(Query query) {
        Search search = new Search();
        search.setQuery(query);
        return search;
    }

    public Search build() {
        return search;
    }
}
