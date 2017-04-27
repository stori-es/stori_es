package org.consumersunion.stories.server.index.elasticsearch.query;

public interface Query {
    Ids getIds();

    void setIds(Ids ids);

    void setTerm(String key, Object value);

    void setMatch(String key, Object value);

    void matchAll();

    void queryString(String queryString);

    void addField(String field);
}
