package org.consumersunion.stories.server.index.elasticsearch.query;

import java.util.Collection;

import org.consumersunion.stories.server.index.elasticsearch.query.bool.Bool;

import com.google.common.base.Functions;

import static com.google.common.collect.Collections2.transform;

public class QueryBuilder {
    private final Query query;

    public static QueryBuilder newBuilder() {
        return new QueryBuilder();
    }

    private QueryBuilder() {
        query = new QueryImpl();
    }

    public QueryBuilder withTerm(String key, Object value) {
        query.setTerm(key, value);
        return this;
    }

    public QueryBuilder withMatch(String key, Object value) {
        query.setMatch(key, value);
        return this;
    }

    public QueryBuilder matchAll() {
        query.matchAll();
        return this;
    }

    private QueryBuilder withQueryString(String queryString) {
        query.queryString(queryString);
        return this;
    }

    public QueryBuilder withIds(Collection<Integer> ids) {
        query.setIds(Ids.fromInts(ids));
        return this;
    }

    public QueryBuilder withIds(String type, String... ids) {
        query.setIds(new Ids(type, ids));
        return this;
    }

    public QueryBuilder withIds(String... ids) {
        query.setIds(new Ids(ids));
        return this;
    }

    public QueryBuilder withIds(int input) {
        query.setIds(Ids.fromInt(input));
        return this;
    }

    public QueryBuilder withFields(String field) {
        query.addField(field);
        return this;
    }

    public Query build() {
        return query;
    }

    public static QueryBuilder queryString(String queryString) {
        return QueryBuilder.newBuilder().withQueryString(queryString);
    }

    public static Query ofIds(String type, String... ids) {
        return newBuilder().withIds(type, ids).build();
    }

    public static Query ofIds(String... ids) {
        return newBuilder().withIds(ids).build();
    }

    public static Query ofIds(Collection<Integer> ids) {
        return newBuilder().withIds(ids).build();
    }

    public static Query ofBool(Bool bool) {
        QueryImpl query = new QueryImpl();
        query.setBool(bool);
        return query;
    }

    public static Query newMatchAll() {
        QueryImpl query = new QueryImpl();
        query.matchAll();
        return query;
    }
}
