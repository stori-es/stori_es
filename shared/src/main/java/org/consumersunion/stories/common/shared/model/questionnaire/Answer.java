package org.consumersunion.stories.common.shared.model.questionnaire;

import java.io.Serializable;
import java.util.List;

import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;

/**
 * An immutable record of an answer to {@link Question}. Answers are associated
 * to the {@link Question} which they answered and are logically grouped by
 * {@link Questionnaire}s.
 *
 * @author Zane Rockenbaugh
 */
public class Answer implements Serializable {
    /**
     * @see #getLabel()
     */
    private String label;

    /**
     * @see #getDisplayValue
     */
    private String displayValue;

    /**
     * @see #getReportvalue() make this a list
     */
    private List<String> reportValues;

    private Integer answerSet;

    public Answer() {
        super();
    }

    /**
     * Standard constructor. As an immutable object, there is no need for
     * separate constructors for new and existing Answers.
     */
    public Answer(
            String label,
            String displayValue,
            List<String> reportValues) {
        super();

        this.label = label;
        this.displayValue = displayValue;
        setReportValues(reportValues);
    }

    public Answer(
            String label,
            String displayValue,
            List<String> reportValues,
            Integer answerSet) {
        this(label, displayValue, reportValues);

        this.answerSet = answerSet;
    }

    /**
     * Returns the index of the associated {@link Question} within the
     * {@link Questionnaire}. Together with {@link #getLabel()}, this resolves
     * to a specific {@link Question}.
     */
    public String getLabel() {
        return label;
    }

    /**
     * The value to display to an end user. For text / free form answers, this
     * should match {@link #getReportValue()}. For constrained questions (lists,
     * check boxes, and radio buttons), this may be set independently by the
     * author of the {@link Questionnaire}.
     */
    public String getDisplayValue() {
        return displayValue;
    }

    /**
     * The value to display in a reporting context. For text / free form
     * answers, this should match {@link #getDisplayValue()}. For constrained
     * questions (lists, check boxes, and radio buttons), this may be set
     * independently by the author of the {@link Questionnaire}.
     */
    public List<String> getReportValues() {
        return reportValues;
    }

    public String getFirstReportValue() {
        if (reportValues == null || reportValues.size() == 0) {
            return null;
        } else {
            return reportValues.get(0);
        }
    }

    public Integer getAnswerSet() {
        return answerSet;
    }

    public void setAnswerSet(final Integer answerSet) {
        this.answerSet = answerSet;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    public void setReportValues(List<String> reportValues) {
        this.reportValues = FluentIterable.from(reportValues).filter(Predicates.notNull()).toList();
    }
}
