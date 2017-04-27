package org.consumersunion.stories.server.index.elasticsearch.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@JsonAutoDetect(fieldVisibility = ANY)
public class SearchHit {
    private String _index;
    private String _type;
    private String _id;
    private double _score;
    private JsonNode source;

    @JsonSetter("_source")
    void setSource(JsonNode source) {
        this.source = source;
    }

    public JsonNode getSource() {
        return source;
    }

    public String getIndex() {
        return _index;
    }

    public String getType() {
        return _type;
    }

    public String getId() {
        return _id;
    }

    public double getScore() {
        return _score;
    }
}
