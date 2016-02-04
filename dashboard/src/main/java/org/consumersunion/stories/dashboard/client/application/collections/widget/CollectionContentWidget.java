package org.consumersunion.stories.dashboard.client.application.collections.widget;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.common.shared.model.Locale;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardContentEditWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardLocaleEditWidget;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class CollectionContentWidget implements IsWidget {
    interface Binder extends UiBinder<Widget, CollectionContentWidget> {
    }

    @UiField(provided = true)
    final CardContentEditWidget contentEdit;
    @UiField(provided = true)
    final CardLocaleEditWidget localeEdit;

    @UiField
    Resources resource;
    @UiField
    HTMLPanel main;

    private final Widget widget;

    @Inject
    CollectionContentWidget(
            Binder uiBinder,
            CardContentEditWidget contentEdit,
            CardLocaleEditWidget localeEdit) {
        this.contentEdit = contentEdit;
        this.localeEdit = localeEdit;

        widget = uiBinder.createAndBindUi(this);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    public void resetForCopy() {
        contentEdit.init(ContentKind.QUESTIONNAIRE);
        localeEdit.setValue(Locale.UNKNOWN);
    }

    public void init(ContentKind contentKind, Document bodyDocument) {
        contentEdit.init(bodyDocument, contentKind);
        localeEdit.setValue(bodyDocument.getLocale());
    }

    public CollectionContent getData() {
        boolean localeValid = localeEdit.validate();
        boolean contentValid = contentEdit.validate();

        if (contentValid && localeValid) {
            return new CollectionContent(contentEdit.getTitle(), contentEdit.getContentText(),
                    localeEdit.getValue());
        } else {
            return new CollectionContent();
        }
    }
}
