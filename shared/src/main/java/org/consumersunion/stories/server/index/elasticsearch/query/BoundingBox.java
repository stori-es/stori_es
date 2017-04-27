package org.consumersunion.stories.server.index.elasticsearch.query;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@JsonAutoDetect(
        fieldVisibility = ANY,
        getterVisibility = NONE,
        setterVisibility = NONE)
public class BoundingBox {
    @JsonProperty("top_right")
    public String topRight;
    @JsonProperty("bottom_left")
    public String bottomLeft;

    public BoundingBox(String topRight, String bottomLeft) {
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
    }
}
