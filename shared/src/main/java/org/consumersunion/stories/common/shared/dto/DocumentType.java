package org.consumersunion.stories.common.shared.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DocumentType {
    ATTACHMENT("AttachmentDocument"),
    BODY("Body"),
    CONTENT("ContentDocument"),
    DEFAULT_PERMISSIONS("Permissions"),
    FORM("FormDocument"),
    NOTE("NoteDocument"),
    RESPONSE("ResponseDocument"),
    STYLE("Style");

    private String name;
    
    private DocumentType(String name) {
        this.name = name;
    }
    
    @JsonValue
    @Override
    public String toString() {
        return name;
    }
}
