package org.consumersunion.stories.common.shared.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Metadata {
    private String self;
    @JsonProperty("http_code")
    private int httpCode;
    private ResponseStatus status;
    private List<Message> messages = new ArrayList<Message>();
    private Pagination pagination;

    public Metadata() {
    }

    @JsonCreator
    public Metadata(
            @JsonProperty("self") String self,
            @JsonProperty("http_code") int httpCode,
            @JsonProperty("status") ResponseStatus status) {
        this.self = self;
        this.httpCode = httpCode;
        this.status = status;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }
}
