package org.consumersunion.stories.common.client.ui.form.controls;

import com.google.common.base.Strings;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.RichTextArea;

public class RichTextEditor extends RichTextArea implements LeafValueEditor<String> {
    @Override
    public void setValue(String value) {
        setHTML(SafeHtmlUtils.fromTrustedString(Strings.nullToEmpty(value)));
    }

    @Override
    public String getValue() {
        return getHTML();
    }
}
