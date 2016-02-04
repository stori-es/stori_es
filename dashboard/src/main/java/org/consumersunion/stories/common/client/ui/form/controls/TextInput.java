package org.consumersunion.stories.common.client.ui.form.controls;

import org.consumersunion.stories.common.client.ui.form.SingleValueFormControl;

import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.TextBox;

public class TextInput extends SingleValueFormControl implements Focusable {
    private final TextBox textBox;

    public TextInput(final String labelText, final String key, final boolean required) {
        this(labelText, key, required, null, null, "stories-textInput");
    }

    public TextInput(final String labelText, final String key, final boolean required, final String style) {
        this(labelText, key, required, null, null, style);
    }

    public TextInput(final String labelText, final String key, final boolean required, final ChangeHandler ch,
            final BlurHandler blurHandler, final String style) {
        super(labelText, key, required, "stories-textGroup");
        this.textBox = new TextBox();
        addChangeHandler(ch);
        addBlurHandler(blurHandler);
        setStyle(style);
        textBox.getElement().setAttribute("name", key);
        getPanel().add(textBox);
    }

    public void addChangeHandler(final ChangeHandler ch) {
        if (ch != null) {
            textBox.addChangeHandler(ch);
        }
    }

    public void addBlurHandler(final BlurHandler blurHandler) {
        if (blurHandler != null) {
            textBox.addBlurHandler(blurHandler);
        }
    }

    public void addKeyUpHandler(final KeyUpHandler handler) {
        textBox.addKeyUpHandler(handler);
    }

    public void addFocusHandler(final FocusHandler handler) {
        textBox.addFocusHandler(handler);
    }

    public void setStyle(final String style) {
        if (style != null && !"".equals(style.trim())) {
            textBox.addStyleName(style);
        }
    }

    protected TextBox getTextBox() {
        return textBox;
    }

    public void setPlaceHolder(String placeHolder) {
        textBox.getElement().setAttribute("placeholder", placeHolder);
    }

    @Override
    public void setMaxLength(final Integer length) {
        super.setMaxLength(length);
        textBox.setMaxLength(getMaxLength());
    }

    @Override
    public String getValue() {
        return textBox.getValue();
    }

    @Override
    public void setValue(final String value) {
        textBox.setValue(value);
    }

    @Override
    public int getTabIndex() {
        return textBox.getTabIndex();
    }

    @Override
    public void setAccessKey(final char key) {
        textBox.setAccessKey(key);
    }

    @Override
    public void setFocus(final boolean focused) {
        textBox.setFocus(focused);
    }

    @Override
    public void setTabIndex(final int index) {
        textBox.setTabIndex(index);
    }

    @Override
    public void setEnabled(final boolean enabled) {
        textBox.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return textBox.isEnabled();
    }

    @Override
    public void setWidth(final String width) {
        textBox.setWidth(width);
    }
}
