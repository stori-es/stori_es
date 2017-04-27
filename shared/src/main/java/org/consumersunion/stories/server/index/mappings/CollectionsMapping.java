package org.consumersunion.stories.server.index.mappings;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@JsonAutoDetect(getterVisibility = NONE, setterVisibility = NONE, fieldVisibility = ANY)
class CollectionsMapping {
    private Map<String, Object> properties = new LinkedHashMap<String, Object>();

    CollectionsMapping() {
        properties.put("tags", new CopyToMapping("text", "tag"));
        properties.put("tag", new Mapping("text"));
        properties.put("title", new KeywordMapping("text"));
    }
}
