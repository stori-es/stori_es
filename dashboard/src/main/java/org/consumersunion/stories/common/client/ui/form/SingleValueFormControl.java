package org.consumersunion.stories.common.client.ui.form;

import java.util.ArrayList;
import java.util.List;

public abstract class SingleValueFormControl extends InputFormControl {

    public SingleValueFormControl(final String label, final String key, final boolean required, final String rowClass) {
        super(label, key, required, rowClass);
    }

    @Override
    public List<String> getValues() {
        final List<String> values = new ArrayList<String>();
        values.add(getValue());
        return values;
    }

    @Override
    public void setValues(final List<String> values) {
        setValue(values.get(0));
    }

    public abstract String getValue();

    public abstract void setValue(String value);
}
