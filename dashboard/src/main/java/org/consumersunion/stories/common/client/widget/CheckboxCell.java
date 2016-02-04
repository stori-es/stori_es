package org.consumersunion.stories.common.client.widget;

import org.consumersunion.stories.common.shared.model.Checkable;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.inject.Inject;

public class CheckboxCell<T extends Checkable> extends AbstractEditableCell<T, T> {
    public interface Template extends SafeHtmlTemplates {
        @Template("<div class=\"listCheckBox {0}\" " +
                "style=\"float: left; display: block; margin-right: 6px; font-size: 22px;\"></div>" +
                "<div style=\"float: left\">{1}</div>" +
                "<div style=\"clear: both;\"></div>")
        SafeHtml checked(String checkedClass, SafeHtml label);
    }

    public static final String NOT_CHECKED = "icon-check-empty";
    public static final String CHECKED = "icon-check";

    private final Template template;

    @Inject
    CheckboxCell(Template template) {
        super(BrowserEvents.CLICK);

        this.template = template;
    }

    @Override
    public boolean isEditing(Context context, Element parent, T value) {
        return false;
    }

    @Override
    public void render(Context context, T value, SafeHtmlBuilder sb) {
        Object key = context.getKey();
        Checkable viewData = getViewData(key);
        if (viewData != null && viewData.getChecked() == value.getChecked()) {
            clearViewData(key);
            viewData = null;
        }

        if (value != null && ((viewData != null) ? viewData.getChecked() : value.getChecked())) {
            sb.append(template.checked(CHECKED, SafeHtmlUtils.fromString(value.getLabel())));
        } else {
            sb.append(template.checked(NOT_CHECKED, SafeHtmlUtils.fromString(value.getLabel())));
        }
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, T value,
            NativeEvent event, ValueUpdater<T> valueUpdater) {
        String type = event.getType();
        if (BrowserEvents.CLICK.equals(type)) {
            String className = parent.getFirstChildElement().getClassName();
            Boolean isChecked = className.equals(CHECKED);
            value.setChecked(!isChecked);

            String reversed = isChecked ? NOT_CHECKED : CHECKED;
            parent.getFirstChildElement().setClassName(reversed);

            if (value.getChecked() != isChecked) {
                setViewData(context.getKey(), value);
            } else {
                clearViewData(context.getKey());
            }

            if (valueUpdater != null) {
                valueUpdater.update(value);
            }
        }
    }
}
