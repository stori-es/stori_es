package org.consumersunion.stories.server.index.elasticsearch.query.bool;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.consumersunion.stories.server.index.elasticsearch.query.BoundingBox;
import org.consumersunion.stories.server.index.elasticsearch.query.GeoDistance;
import org.consumersunion.stories.server.index.elasticsearch.query.Ids;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;

public class BoolBuilder implements FluentBoolBuilder {
    private final Bool bool;

    private MustBuilderImpl mustBuilder;
    private MustNotBuilderImpl mustNotBuilder;
    private FilterBuilderImpl filterBuilder;
    private ShouldBuilderImpl shouldBuilder;

    public static BoolBuilder newBuilder() {
        return new BoolBuilder();
    }

    public BoolBuilder() {
        bool = new Bool();
    }

    @Override
    public MustBuilder must() {
        if (mustBuilder == null) {
            mustBuilder = new MustBuilderImpl();
        }
        return mustBuilder;
    }

    @Override
    public FilterBuilder filter() {
        if (filterBuilder == null) {
            filterBuilder = new FilterBuilderImpl();
        }
        return filterBuilder;
    }

    @Override
    public MustNotBuilder mustNot() {
        if (mustNotBuilder == null) {
            mustNotBuilder = new MustNotBuilderImpl();
        }
        return mustNotBuilder;
    }

    @Override
    public ShouldBuilder should() {
        if (shouldBuilder == null) {
            shouldBuilder = new ShouldBuilderImpl();
        }
        return shouldBuilder;
    }

    @Override
    public BoolBuilder and() {
        return this;
    }

    @Override
    public Bool build() {
        bool.must = assignBuilder(mustBuilder);
        bool.mustNot = assignBuilder(mustNotBuilder);
        bool.filter = assignBuilder(filterBuilder);
        bool.should = assignBuilder(shouldBuilder);
        return bool;
    }

    private Object assignBuilder(InnerBuilder<?> innerBuilder) {
        if (innerBuilder == null) {
            return null;
        }

        List<? extends Object> values = FluentIterable.from(innerBuilder.data)
                .filter(Predicates.notNull())
                .toList();

        if (values.isEmpty()) {
            return null;
        } else if (values.size() == 1) {
            return values.get(0);
        } else {
            return values;
        }
    }

    private abstract class InnerBuilder<B extends InnerBuilder<B>> implements BoolSubBuilder<B> {
        protected List<Map.Entry<String, Object>> data;

        @Override
        public B addTerm(String key, Object value) {
            ensureData();
            SimpleEntry<String, Object> entry = new SimpleEntry<String, Object>(key, value);

            removeTerm(key);
            data.add(new SimpleEntry<String, Object>("term", entry));

            return getBuilder();
        }

        private boolean removeTerm(final String key) {
            return Iterables.removeIf(data, new Predicate<Map.Entry<String, Object>>() {
                @Override
                public boolean apply(Map.Entry<String, Object> datum) {
                    return "term".equals(datum.getKey())
                            && datum.getValue() instanceof SimpleEntry
                            && key.equals(((SimpleEntry) datum.getValue()).getKey());
                }
            });
        }

        @Override
        public B withGeoBoundingBox(String field, String topRight, String bottomLeft) {
            ensureData();
            SimpleEntry<String, Object> entry = new SimpleEntry<String, Object>(field,
                    new BoundingBox(topRight, bottomLeft));
            data.add(new SimpleEntry<String, Object>("geo_bounding_box", entry));

            return getBuilder();
        }

        @Override
        public B withGeoDistance(String field, String distance, String location) {
            ensureData();
            GeoDistance geoDistance = new GeoDistance(field, distance, location);
            data.add(new SimpleEntry<String, Object>("geo_distance", geoDistance));

            return getBuilder();
        }

        @Override
        public B withIds(Ids ids) {
            ensureData();
            data.add(new SimpleEntry<String, Object>("ids", ids));
            return getBuilder();
        }

        @Override
        public B withQueryString(String queryString) {
            ensureData();
            SimpleEntry<String, Object> entry = new SimpleEntry<String, Object>("query", queryString);
            data.add(new SimpleEntry<String, Object>("query_string", entry));

            return getBuilder();
        }

        private void ensureData() {
            if (data == null) {
                data = new ArrayList<Map.Entry<String, Object>>();
            }
        }

        @Override
        public MustBuilder must() {
            return BoolBuilder.this.must();
        }

        @Override
        public FilterBuilder filter() {
            return BoolBuilder.this.filter();
        }

        @Override
        public MustNotBuilder mustNot() {
            return BoolBuilder.this.mustNot();
        }

        @Override
        public ShouldBuilder should() {
            return BoolBuilder.this.should();
        }

        @Override
        public BoolBuilder and() {
            return BoolBuilder.this.and();
        }

        @Override
        public Bool build() {
            return BoolBuilder.this.build();
        }

        protected abstract B getBuilder();
    }

    private class MustBuilderImpl extends InnerBuilder<MustBuilderImpl> implements MustBuilder<MustBuilderImpl> {
        @Override
        protected MustBuilderImpl getBuilder() {
            return this;
        }
    }

    private class MustNotBuilderImpl extends InnerBuilder<MustNotBuilderImpl>
            implements MustNotBuilder<MustNotBuilderImpl> {
        @Override
        protected MustNotBuilderImpl getBuilder() {
            return this;
        }
    }

    private class FilterBuilderImpl extends InnerBuilder<FilterBuilderImpl>
            implements FilterBuilder<FilterBuilderImpl> {
        @Override
        protected FilterBuilderImpl getBuilder() {
            return this;
        }
    }

    private class ShouldBuilderImpl extends InnerBuilder<ShouldBuilderImpl>
            implements ShouldBuilder<ShouldBuilderImpl> {
        @Override
        protected ShouldBuilderImpl getBuilder() {
            return this;
        }
    }
}
