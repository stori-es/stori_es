package org.consumersunion.stories.dashboard.client.application.collections.widget;

import org.consumersunion.stories.common.client.i18n.ExportKindLabels;
import org.consumersunion.stories.common.shared.ExportKind;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.client.ui.ValueListBox;

public class ExportListBox extends ValueListBox<ExportKind> {
    private static final ExportKindLabels labels = GWT.create(ExportKindLabels.class);
    private static final AbstractRenderer<ExportKind> RENDERER =
            new AbstractRenderer<ExportKind>() {
                @Override
                public String render(ExportKind kind) {
                    if (ExportKind.UNKNOWN.equals(kind)) {
                        return "";
                    } else {
                        return labels.getString(kind.name());
                    }
                }
            };

    public ExportListBox() {
        super(RENDERER);

        setValue(ExportKind.UNKNOWN);
        setAcceptableValues(Lists.newArrayList(ExportKind.values()));
    }
}
