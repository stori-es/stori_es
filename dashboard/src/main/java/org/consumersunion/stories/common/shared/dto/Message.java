package org.consumersunion.stories.common.shared.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    private String target;
    private String summary;
    @JsonProperty("message_code")
    private String messageCode;
    @JsonProperty("detail_url")
    private String detailUrl;

    @JsonCreator
    public Message(@JsonProperty("summary") String summary, @JsonProperty("message_code") String messageCode) {
        this.summary = summary;
        this.messageCode = messageCode;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }
}
