package org.consumersunion.stories.dashboard.client.application.widget.card;

import org.consumersunion.stories.common.shared.ExportKind;
import org.consumersunion.stories.dashboard.client.application.collections.widget.ExportListBox;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import static com.google.gwt.query.client.GQuery.$;

public class ExportFormatWidget extends Composite implements LeafValueEditor<ExportKind> {
    interface Binder extends UiBinder<Widget, ExportFormatWidget> {
    }

    @UiField(provided = true)
    final ValueListBox<ExportKind> exportListBox;

    @UiField
    Resources resource;
    @UiField
    DivElement formatError;

    @Inject
    ExportFormatWidget(
            Binder uiBinder,
            ExportListBox exportListBox) {
        this.exportListBox = exportListBox;

        initWidget(uiBinder.createAndBindUi(this));

        clearErrors();
    }

    public boolean validate() {
        clearErrors();

        boolean valid = true;
        Widget elementToFocus = null;

        if (ExportKind.UNKNOWN.equals(exportListBox.getValue())) {
            valid = false;
            $(formatError).show();
            formatError.focus();
            elementToFocus = exportListBox;
        }

        if (elementToFocus != null) {
            elementToFocus.getElement().focus();
        }

        return valid;
    }

    @Override
    public ExportKind getValue() {
        return exportListBox.getValue();
    }

    @Override
    public void setValue(ExportKind value) {
        exportListBox.setValue(value);
    }

    public void clearErrors() {
        $(formatError).hide();
    }
}
