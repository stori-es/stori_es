package org.consumersunion.stories.server.index.mappings;

import java.util.AbstractMap.SimpleEntry;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@JsonAutoDetect(getterVisibility = NONE, setterVisibility = NONE, fieldVisibility = ANY)
public class KeywordMapping {
    private final String type;
    private final Map<String, Object> fields = new LinkedHashMap<String, Object>();

    public KeywordMapping(String type) {
        this.type = type;
        fields.put("keyword", new SimpleEntry<String, String>("type", "keyword"));
    }
}
