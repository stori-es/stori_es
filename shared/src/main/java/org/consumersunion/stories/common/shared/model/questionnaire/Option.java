package org.consumersunion.stories.common.shared.model.questionnaire;

import java.io.Serializable;

public class Option implements Serializable {
    /**
     * @see #getDisplayValue()
     */
    private String displayValue;
    /**
     * @see #getReportValue()
     */
    private String reportValue;

    public Option() {
    }

    public Option(
            String displayValue,
            String reportValue) {
        this.displayValue = displayValue;
        this.reportValue = reportValue;
    }

    /**
     * The option displayed to users.
     */
    public String getDisplayValue() {
        return displayValue;
    }

    /**
     * @see #getDisplayValue()
     */
    public void setDisplayValue(String userOption) {
        this.displayValue = userOption;
    }

    /**
     * The report value.
     */
    public String getReportValue() {
        return reportValue;
    }

    /**
     * @see #getReportValue()
     */
    public void setReportValue(String reportValue) {
        this.reportValue = reportValue;
    }
}
