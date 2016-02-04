package org.consumersunion.stories.common.client.ui.form.controls;

import org.consumersunion.stories.common.client.ui.form.SingleValueFormControl;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.PasswordTextBox;

public class PasswordInput extends SingleValueFormControl {
    private final PasswordTextBox pwtb;

    public PasswordInput(final String labelText, final String key, final boolean required) {
        super(labelText, key, required, "stories-passwordGroup");
        pwtb = new PasswordTextBox();
        getPanel().add(pwtb);
    }

    public void setChangeHandler(final ChangeHandler ch) {
        pwtb.addChangeHandler(ch);
    }

    @Override
    public void setMaxLength(final Integer length) {
        super.setMaxLength(length);
        pwtb.setMaxLength(getMaxLength());
    }

    @Override
    public String getValue() {
        return pwtb.getValue();
    }

    @Override
    public void setValue(final String value) {
        pwtb.setValue(value);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        pwtb.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return pwtb.isEnabled();
    }
}
