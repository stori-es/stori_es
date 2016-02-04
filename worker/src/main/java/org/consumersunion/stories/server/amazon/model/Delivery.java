package org.consumersunion.stories.server.amazon.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Delivery {
    private Date timestamp;
    private int processingTimeMillis;
    private List<String> recipients;
    private String smtpResponse;
    @JsonProperty("reportingMTA")
    private String reportingMta;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getProcessingTimeMillis() {
        return processingTimeMillis;
    }

    public void setProcessingTimeMillis(int processingTimeMillis) {
        this.processingTimeMillis = processingTimeMillis;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public String getSmtpResponse() {
        return smtpResponse;
    }

    public void setSmtpResponse(String smtpResponse) {
        this.smtpResponse = smtpResponse;
    }

    public String getReportingMta() {
        return reportingMta;
    }

    public void setReportingMta(String reportingMta) {
        this.reportingMta = reportingMta;
    }
}
