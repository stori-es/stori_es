package org.consumersunion.stories.server.index.mappings;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@JsonAutoDetect(getterVisibility = NONE, setterVisibility = NONE, fieldVisibility = ANY)
class CopyToMapping {
    private String type;
    @JsonProperty("copy_to")
    private String copyTo;

    CopyToMapping(String type, String copyTo) {
        this.type = type;
        this.copyTo = copyTo;
    }
}
