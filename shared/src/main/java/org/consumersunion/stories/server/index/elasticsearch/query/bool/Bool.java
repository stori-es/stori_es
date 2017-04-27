package org.consumersunion.stories.server.index.elasticsearch.query.bool;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Bool {
    public Object must;
    @JsonProperty("must_not")
    public Object mustNot;
    public Object filter;
    public Object should;
}
