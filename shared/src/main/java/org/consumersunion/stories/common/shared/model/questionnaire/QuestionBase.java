package org.consumersunion.stories.common.shared.model.questionnaire;

import java.io.Serializable;

import org.consumersunion.stories.common.shared.model.document.Block;

/**
 * Encapsulates data regarding a question. A question consists of:
 * <p/>
 * * internationalized text * a 'form type' (text field, select list, etc.) * a
 * name (for reporting) * is required indicator * constraints *
 * internationalized help text
 * <p/>
 * Questions only exist within the context of a {@link Questionnaire}.
 * <p/>
 * A question may be either 'custom' or 'standard'. Refer to the 'Standard and
 * Custom Questions' section of the Developer Reference for more background.
 */
public abstract class QuestionBase extends Block implements Serializable {
    /**
     * @see #getDatatype();
     */
    private String dataType;

    /**
     * @see #getName()
     */
    private String label;

    /**
     * @see #isRequired()
     */
    private boolean required;

    private boolean multiselect;

    /**
     * @see #getMinlength()
     */
    private Integer minLength;

    /**
     * @see #getMaxLength()
     */
    private Integer maxLength;

    private String startDate;

    /**
     * The data type is a type constraint for text inputs.
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * @see #getDataType()
     */
    public void setDataType(final String dataType) {
        this.dataType = dataType;
    }

    /**
     * The question label is used for reports.
     */
    public String getLabel() {
        return label;
    }

    /**
     * @see #getLabel()
     */
    public void setLabel(final String label) {
        this.label = label;
    }

    /**
     * Indicates whether the question is required or not.
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * @see #getRequired()
     */
    public void setRequired(final boolean required) {
        this.required = required;
    }

    public boolean isMultiselect() {
        return multiselect;
    }

    public void setMultiselect(final boolean multiselect) {
        this.multiselect = multiselect;
    }

    /**
     * The minimum length, effective for text input only. A null value indicates
     * no constraint.
     */
    public Integer getMinLength() {
        return minLength;
    }

    /**
     * @see #getMinLength()
     */
    public void setMinLength(final Integer minLength) {
        this.minLength = minLength;
    }

    /**
     * The maximum length, effective for text input only. A null value indicates
     * no constraint.
     */
    public Integer getMaxLength() {
        return maxLength;
    }

    /**
     * @see #getMaxLength()
     */
    public void setMaxLength(final Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
