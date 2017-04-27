package org.consumersunion.stories.server.index.mappings;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@JsonAutoDetect(getterVisibility = NONE, setterVisibility = NONE, fieldVisibility = ANY)
class ProfilesMapping {
}
