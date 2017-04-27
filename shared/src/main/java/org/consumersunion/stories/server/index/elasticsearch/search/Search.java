package org.consumersunion.stories.server.index.elasticsearch.search;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.consumersunion.stories.server.index.elasticsearch.SortOrder;
import org.consumersunion.stories.server.index.elasticsearch.query.Query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class Search {
    private Long from;
    private Long size;
    private Query query;
    private Map<String, String> sort;
    @JsonInclude(NON_NULL)
    @JsonProperty("stored_fields")
    private List<String> storedFields;

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public void addSort(String key, SortOrder sortOrder) {
        if (sort == null) {
            sort = new LinkedHashMap<String, String>();
        }

        sort.put(key, sortOrder.getValue());
    }

    public Map<String, String> getSort() {
        return sort;
    }

    public void setSort(Map<String, String> sort) {
        this.sort = sort;
    }

    public List<String> getStoredFields() {
        return storedFields;
    }

    public void setStoredFields(List<String> storedFields) {
        this.storedFields = storedFields;
    }

    public void withoutFields() {
        storedFields = new ArrayList<String>();
    }
}
