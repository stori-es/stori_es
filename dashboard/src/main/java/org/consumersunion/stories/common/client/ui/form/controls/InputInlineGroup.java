package org.consumersunion.stories.common.client.ui.form.controls;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class InputInlineGroup extends Composite {
    private final ComplexPanel layout;

    public InputInlineGroup(final Widget label, final Widget input, String rowClass) {
        this.layout = new HorizontalPanel();
        initWidget(layout);
        label.addStyleName("stories-formElement-label"); // Needed for styling
        input.addStyleName("stories-formElement-input"); // Needed for styling
        layout.add(label);
        layout.add(input);
        getElement().addClassName("stories-inputGroup"); // Needed for styling
        if (rowClass != null && rowClass.trim().length() > 0) {
            getElement().addClassName(rowClass);
        }
    }

    public Widget getWidget(final int i) {
        return layout.getWidget(i);
    }

    public void reverseLayout() {
        final Widget one = layout.getWidget(0);
        final Widget two = layout.getWidget(1);
        layout.remove(one);
        layout.remove(two);
        layout.add(two);
        layout.add(one);
    }

    public HorizontalPanel getLayout() {
        return (HorizontalPanel) layout;
    }
}
