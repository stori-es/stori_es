package org.consumersunion.stories.server.index.mappings;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@JsonAutoDetect(getterVisibility = NONE, setterVisibility = NONE, fieldVisibility = ANY)
class StoriesMapping {
    private Map<String, Object> properties = new LinkedHashMap<String, Object>();

    StoriesMapping() {
        properties.put("tags", new CopyToMapping("text", "tag"));
        properties.put("tag", new Mapping("text"));
        properties.put("title", new KeywordMapping("text"));
        properties.put("authorCity", new KeywordMapping("text"));
        properties.put("authorState", new KeywordMapping("text"));
        properties.put("authorSurname", new KeywordMapping("text"));
        properties.put("authorLocation", new Mapping("geo_point"));
        properties.put("created", new Mapping("date"));
        properties.put("modified", new Mapping("date"));
    }
}
