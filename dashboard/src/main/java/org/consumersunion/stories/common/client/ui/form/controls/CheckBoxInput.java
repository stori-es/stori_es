package org.consumersunion.stories.common.client.ui.form.controls;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.ui.form.InputFormControl;
import org.consumersunion.stories.common.shared.service.GeneralException;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

public class CheckBoxInput extends InputFormControl {

    private String[] labels;
    private String[] options;
    private ClickHandler clickHandler;
    private final List<CheckBox> checkBoxes;

    public CheckBoxInput(final String labelText, final String key, final boolean required) {
        this(labelText, key, new String[0], new String[0], required);
    }

    public CheckBoxInput(final String labelText, final String key, final String[] labels, final String[] values,
            final boolean required) {
        super(labelText, key, required, "stories-checkBoxGroup");
        checkBoxes = new ArrayList<CheckBox>();
        setLabelsAndOptions(labels, values);
    }

    @Override
    protected Panel createPanel() {
        final FlowPanel panel = new FlowPanel();
        panel.getElement().addClassName("cu-checkPanel");
        return panel;
    }

    public void setLabelsAndOptions(final String[] labels, final String[] options) {
        if (labels.length != options.length) {
            throw new GeneralException("Array sizes did not match");
        }

        this.labels = labels;
        this.options = options;
        createCheckBoxes();
    }

    private void createCheckBoxes() {
        final Panel panel = getPanel();
        panel.clear();
        checkBoxes.clear();

        for (int i = 0; i < options.length; i += 1) {
            final CheckBox cBox = new CheckBox(labels[i]);

            if (clickHandler != null) {
                cBox.addClickHandler(clickHandler);
            }

            panel.add(cBox);
            checkBoxes.add(cBox);
            cBox.setFormValue(options[i]);
        }
    }

    public void setClickHandler(final ClickHandler ch) {
        this.clickHandler = ch;
        for (final CheckBox cb : checkBoxes) {
            cb.addClickHandler(ch);
        }
    }

    public String[] getOptions() {
        return options;
    }

    public String[] getLabels() {
        return labels;
    }

    @Override
    public List<String> getValues() {
        final List<String> values = new ArrayList<String>();

        for (final CheckBox cb : checkBoxes) {
            if (cb.getValue()) {
                values.add(cb.getFormValue());
            }
        }

        return values;
    }

    public void resetValues() {
        for (final CheckBox cb : checkBoxes) {
            cb.setValue(false);
        }
    }

    @Override
    public void setValues(final List<String> values) {
        for (final CheckBox cb : checkBoxes) {
            cb.setValue(false);
            setCBValue(cb, values);
        }
    }

    private void setCBValue(final CheckBox cb, final List<String> selectedValues) {
        for (final String v : selectedValues) {
            if (cb.getFormValue().equals(v)) {
                cb.setValue(true);
                return;
            }
        }
    }

    @Override
    public void setEnabled(final boolean enabled) {
        for (final CheckBox cb : checkBoxes) {
            cb.setEnabled(enabled);
        }
    }

    @Override
    public boolean isEnabled() {
        for (final CheckBox cb : checkBoxes) {
            if (!cb.isEnabled()) {
                return false;
            }
        }

        return true;
    }
}
