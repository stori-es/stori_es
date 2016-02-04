package org.consumersunion.stories.common.client.ui.form;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;

public abstract class UiFormControl extends Composite implements HasEnabled {
    private String label;
    private String key;
    private String rowClass;
    private boolean required;
    private Integer minLength;
    private Integer maxLength;
    private Validator validator;

    protected final Panel mainLayout;

    public UiFormControl(final String label, final String key, final boolean required, final String rowClass) {
        mainLayout = createPanel();
        initWidget(mainLayout);
        setLabel(label);
        setKey(key);
        setRequired(required);
        setRowClass(rowClass);
    }

    protected Panel createPanel() {
        return new HorizontalPanel();
    }

    public abstract void validate();

    protected boolean isValue(final String value) {
        return value != null && value.trim().length() > 0 && !value.matches("^-*$");
    }

    protected Panel getPanel() {
        return mainLayout;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(final boolean required) {
        this.required = required;
    }

    public Integer getMinLength() {
        return (minLength != null) ? minLength : Integer.MIN_VALUE;
    }

    public void setMinLength(final Integer length) {
        this.minLength = length;
    }

    public Integer getMaxLength() {
        return (maxLength != null) ? maxLength : Integer.MAX_VALUE;
    }

    public void setMaxLength(final Integer length) {
        this.maxLength = length;
    }

    public Validator getValidator() {
        return validator;
    }

    public void setValidator(final Validator validator) {
        this.validator = validator;
    }

    public String getRowClass() {
        return rowClass;
    }

    public void setRowClass(String rowClass) {
        this.rowClass = rowClass;
    }

	/*
     * @Override public void setEnabled(final boolean enabled) {
	 * 
	 * }
	 * 
	 * @Override public boolean isEnabled() {
	 * 
	 * } /*
	 */
}
