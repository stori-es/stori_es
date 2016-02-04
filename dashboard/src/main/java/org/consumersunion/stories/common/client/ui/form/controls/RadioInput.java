package org.consumersunion.stories.common.client.ui.form.controls;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.ui.form.SingleValueFormControl;
import org.consumersunion.stories.common.shared.service.GeneralException;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RadioInput extends SingleValueFormControl {
    private final List<RadioButton> buttonSet;

    private String groupName;
    private String[] labels;
    private String[] options;
    private ClickHandler clickHandler;

    @Override
    protected Panel createPanel() {
        return new VerticalPanel();
    }

    public RadioInput(final String labelText, final String key, final String groupName, final boolean required) {
        this(labelText, key, groupName, new String[0], new String[0], required, null);
    }

    public RadioInput(final String labelText, final String key, final String groupName, final String[] labels,
            final String[] values, final boolean required, final ClickHandler ch) {
        super(labelText, key, required, "stories-radioGroup");
        buttonSet = new ArrayList<RadioButton>();

        setGroupName(groupName);
        setClickHandler(ch);

        setLabelsAndOptions(labels, values);
    }

    public void setLabelsAndOptions(final String[] labels, final String[] options) {
        if (labels.length != options.length) {
            throw new GeneralException("Array sizes did not match");
        }

        this.labels = labels;
        this.options = options;

        createButtons();
    }

    private void createButtons() {
        buttonSet.clear();
        final Panel panel = getPanel();
        panel.clear();

        for (int i = 0; i < labels.length; i += 1) {
            final RadioButton rButton = new RadioButton(groupName, labels[i]);

            if (clickHandler != null) {
                rButton.addClickHandler(clickHandler);
            }

            rButton.setFormValue(options[i]);

            buttonSet.add(rButton);
            panel.add(rButton);
        }
    }

    public ClickHandler getClickHandler() {
        return clickHandler;
    }

    public void setClickHandler(final ClickHandler ch) {
        this.clickHandler = ch;
        for (final RadioButton btn : buttonSet) {
            btn.addClickHandler(ch);
        }
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(final String groupName) {
        this.groupName = groupName;
        for (final RadioButton btn : buttonSet) {
            btn.setName(groupName);
        }
    }

    public String[] getOptions() {
        return options;
    }

    public String[] getLabels() {
        return labels;
    }

    @Override
    public String getValue() {
        for (final RadioButton rbtn : buttonSet) {
            if (rbtn.getValue()) {
                return rbtn.getFormValue();
            }
        }

        return "";
    }

    public void resetValues() {
        for (final RadioButton rbtn : buttonSet) {
            rbtn.setValue(false);
        }
    }

    @Override
    public void setValue(final String value) {
        for (final RadioButton rbtn : buttonSet) {
            if (rbtn.getFormValue().equals(value)) {
                rbtn.setValue(true);
            }
        }
    }

    @Override
    public void setEnabled(final boolean enabled) {
        for (final RadioButton rbtn : buttonSet) {
            rbtn.setEnabled(enabled);
        }
    }

    @Override
    public boolean isEnabled() {
        for (final RadioButton rbtn : buttonSet) {
            if (!rbtn.isEnabled()) {
                return false;
            }
        }
        return true;
    }
}
