package org.consumersunion.stories.dashboard.client.application.widget.content;

import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardContentEditWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardLocaleEditWidget;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardTitleWidget;
import org.consumersunion.stories.dashboard.client.resource.Resources;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import static com.google.gwt.query.client.GQuery.$;

public class AddDocumentView extends ViewWithUiHandlers<AddDocumentUiHandlers>
        implements AddDocumentPresenter.MyView {
    interface Binder extends UiBinder<Widget, AddDocumentView> {
    }

    @UiField(provided = true)
    final CardContentEditWidget contentEdit;
    @UiField(provided = true)
    final CardTitleWidget cardTitle;
    @UiField(provided = true)
    final CardLocaleEditWidget editLocale;

    @UiField
    Resources resource;
    @UiField
    HTMLPanel main;
    @UiField
    SpanElement createActive;
    @UiField
    SpanElement cancel;
    @UiField
    SpanElement editToolbar;

    private ContentKind contentKind;

    @Inject
    AddDocumentView(
            Binder uiBinder,
            CardTitleWidget cardTitle,
            CardContentEditWidget contentEdit,
            CardLocaleEditWidget editLocale) {
        this.cardTitle = cardTitle;
        this.contentEdit = contentEdit;
        this.editLocale = editLocale;

        initWidget(uiBinder.createAndBindUi(this));

        bindClicks();
    }

    @Override
    public void init(ContentKind contentKind) {
        this.contentKind = contentKind;

        cardTitle.init(contentKind);
        contentEdit.init(contentKind);

        if (!hasLocale()) {
            editLocale.removeFromParent();
        }
    }

    @UiHandler("create")
    void onCreateClicked(ClickEvent event) {
        boolean localeValid = editLocale.validate();
        boolean contentValid = contentEdit.validate();

        if (hasLocale() && contentValid && localeValid || !hasLocale() && contentValid) {
            getUiHandlers().create(contentEdit.getTitle(), contentEdit.getContentText(), editLocale.getValue());
        }
    }

    private boolean hasLocale() {
        return contentKind.hasLocale();
    }

    private void bindClicks() {
        $(cancel).click(new Function() {
            @Override
            public void f() {
                getUiHandlers().cancel();
                asWidget().removeFromParent();
            }
        });
    }
}
