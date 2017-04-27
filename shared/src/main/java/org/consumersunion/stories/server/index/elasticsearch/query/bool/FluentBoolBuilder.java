package org.consumersunion.stories.server.index.elasticsearch.query.bool;

public interface FluentBoolBuilder {
    MustBuilder must();

    FilterBuilder filter();

    MustNotBuilder mustNot();

    ShouldBuilder should();

    BoolBuilder and();

    Bool build();
}
