package org.consumersunion.stories.server.amazon.model;

import java.util.Date;
import java.util.List;

public class Complaint {
    private List<ComplainedRecipient> complainedRecipients;
    private Date timestamp;
    private String feedbackId;
    private String userAgent;
    private String complaintFeedbackType;
    private Date arrivalDate;

    public List<ComplainedRecipient> getComplainedRecipients() {
        return complainedRecipients;
    }

    public void setComplainedRecipients(List<ComplainedRecipient> complainedRecipients) {
        this.complainedRecipients = complainedRecipients;
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

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getComplaintFeedbackType() {
        return complaintFeedbackType;
    }

    public void setComplaintFeedbackType(String complaintFeedbackType) {
        this.complaintFeedbackType = complaintFeedbackType;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }
}
