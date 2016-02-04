package org.consumersunion.stories.server.amazon.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Bounce {
    private BounceType bounceType;
    private BounceSubType bounceSubType;
    private List<BouncedRecipient> bouncedRecipients;
    private Date timestamp;
    private String feedbackId;
    @JsonProperty("reportingMTA")
    private String reportingMta;

    public BounceType getBounceType() {
        return bounceType;
    }

    public void setBounceType(BounceType bounceType) {
        this.bounceType = bounceType;
    }

    public BounceSubType getBounceSubType() {
        return bounceSubType;
    }

    public void setBounceSubType(BounceSubType bounceSubType) {
        this.bounceSubType = bounceSubType;
    }

    public List<BouncedRecipient> getBouncedRecipients() {
        return bouncedRecipients;
    }

    public void setBouncedRecipients(List<BouncedRecipient> bouncedRecipients) {
        this.bouncedRecipients = bouncedRecipients;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getReportingMta() {
        return reportingMta;
    }

    public void setReportingMta(String reportingMta) {
        this.reportingMta = reportingMta;
    }
}
