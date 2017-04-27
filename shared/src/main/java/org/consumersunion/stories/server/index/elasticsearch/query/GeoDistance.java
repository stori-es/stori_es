package org.consumersunion.stories.server.index.elasticsearch.query;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

public class GeoDistance {
    private final Map<String, String> properties = new LinkedHashMap<String, String>();

    public GeoDistance(String field, String distance, String location) {
        properties.put("distance", distance);
        properties.put(field, location);
    }

    @JsonAnyGetter
    public Map<String, String> dynamicField() {
        return properties;
    }
}
