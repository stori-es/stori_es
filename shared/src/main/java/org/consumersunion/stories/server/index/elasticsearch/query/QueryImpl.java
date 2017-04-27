package org.consumersunion.stories.server.index.elasticsearch.query;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.consumersunion.stories.server.index.elasticsearch.query.bool.Bool;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonAutoDetect(getterVisibility = NONE, setterVisibility = NONE, fieldVisibility = ANY)
@JsonInclude(NON_EMPTY)
public class QueryImpl implements Query {
    private Ids ids;
    private Map<String, String> term;
    private Map<String, String> match;
    private List<String> fields;
    @JsonProperty("match_all")
    @JsonInclude(NON_NULL)
    private Object matchAll;
    @JsonProperty("query_string")
    private Object queryString;
    private Bool bool;

    QueryImpl() {
    }

    @Override
    public Ids getIds() {
        return ids;
    }

    @Override
    public void setIds(Ids ids) {
        this.ids = ids;
    }

    public void setBool(Bool bool) {
        this.bool = bool;
    }

    @Override
    public void setTerm(String key, Object value) {
        assert term == null && match == null;

        term = new LinkedHashMap<String, String>();
        term.put(key, value == null ? null : value.toString());
    }

    @Override
    public void setMatch(String key, Object value) {
        assert term == null && match == null;

        match = new LinkedHashMap<String, String>();
        match.put(key, value == null ? null : value.toString());
    }

    @Override
    public void addField(String field) {
        if (fields == null) {
            fields = new ArrayList<String>();
        }
        fields.add(field);
    }

    @Override
    public void matchAll() {
        matchAll = new Object();
    }

    @Override
    public void queryString(String queryString) {
        this.queryString = new QueryString(queryString);
    }
}
