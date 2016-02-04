package org.consumersunion.stories.common.client.ui.form.controls;

import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.ui.form.InputFormControl;
import org.consumersunion.stories.common.shared.service.GeneralException;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;

public class ListInput extends InputFormControl {
    public static final String BLANK_VALUE = "------";

    private final ListBox listBox;

    private String[] labels;
    private String[] options;
    private boolean includeBlank;

    public ListInput(final String labelText, final String key, final String[] options, final String[] values,
            final boolean required, final boolean includeBlank, final boolean multiSelect, final String style) {

        this(labelText, key, options, values, required, includeBlank, multiSelect);
        listBox.setStyleName(style);
    }

    public ListInput(final String labelText, final String key, final boolean required, final boolean includeBlank,
            final boolean multiSelect) {
        this(labelText, key, new String[0], new String[0], required, includeBlank, multiSelect);
    }

    public ListInput(final String labelText, final String key, final String[] options, final String[] values,
            final boolean required, final boolean includeBlank, final boolean multiSelect) {
        super(labelText, key, required, "stories-listGroup");
        listBox = new ListBox(multiSelect);
        setVisibleItemCount(multiSelect);
        getPanel().add(listBox);

        setIncludeBlank(includeBlank);
        setLabelsAndOptions(options, values);
    }

    public ListInput(ListInputBuilder listInputBuilder) {
        this(listInputBuilder.labelText, listInputBuilder.key,
                listInputBuilder.options, listInputBuilder.values,
                listInputBuilder.required, listInputBuilder.includeBlank, listInputBuilder.multiSelect);
    }

    private void setVisibleItemCount(final boolean multiSelect) {
        if (multiSelect) {
            listBox.setVisibleItemCount(4);
        } else {
            listBox.setVisibleItemCount(1);
        }
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabelsAndOptions(final String[] labels, final String[] options) {
        if (labels.length != options.length) {
            throw new GeneralException("Array sizes did not match");
        }

        this.labels = labels;
        this.options = options;
        listBox.clear();
        createOptions();
    }

    private void createOptions() {
        if (includeBlank) {
            listBox.addItem(BLANK_VALUE, BLANK_VALUE);
        }

        for (int i = 0; i < labels.length; i++) {
            listBox.addItem(labels[i], options[i]);
        }
    }

    public void setChangeHandler(final ChangeHandler ch) {
        listBox.addChangeHandler(ch);
    }

    public void setIncludeBlank(final boolean includeBlank) {
        this.includeBlank = includeBlank;
    }

    public String[] getOptions() {
        return options;
    }

    @Override
    public List<String> getValues() {
        final List<String> values = new ArrayList<String>();
        final int itemCount = listBox.getItemCount();

        for (int i = 0; i < itemCount; i++) {
            if (listBox.isItemSelected(i)) {
                final String value = listBox.getValue(i);
                if (isValue(value)) {
                    values.add(value);
                }
            }
        }

        return values;
    }

    @Override
    public void setValues(final List<String> values) {
        clearSelection();
        for (final String v : values) {
            setSelected(v);
        }
    }

    private void clearSelection() {
        for (int i = 0; i < listBox.getItemCount(); i++) {
            listBox.setItemSelected(i, false);
        }
    }

    private void setSelected(final String option) {
        if (options != null) {
            for (int i = 0; i < options.length; i++) {
                if (option.equals(options[i])) {
                    listBox.setItemSelected((includeBlank) ? i + 1 : i, true);
                    return;
                }
            }
        }
    }

    @Override
    public void setEnabled(final boolean enabled) {
        listBox.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return listBox.isEnabled();
    }

    public static class ListInputBuilder {
        private String labelText;
        private final String key;
        private Boolean required;
        private String[] values;
        private String[] options;
        private Boolean includeBlank;
        private Boolean multiSelect;

        public ListInputBuilder(String key) {
            this.key = key;
        }

        public ListInputBuilder label(String labelText) {
            this.labelText = labelText;
            return this;
        }

        public ListInputBuilder required(Boolean required) {
            this.required = required;
            return this;
        }

        public ListInputBuilder values(String[] values) {
            this.values = values;
            return this;
        }

        public ListInputBuilder options(String[] options) {
            this.options = options;
            return this;
        }

        public ListInputBuilder includeBlank(Boolean includeBlank) {
            this.includeBlank = includeBlank;
            return this;
        }

        public ListInputBuilder multipleSelect(Boolean multiSelect) {
            this.multiSelect = multiSelect;
            return this;
        }

        public ListInput build() {
            return new ListInput(this);
        }
    }
}
