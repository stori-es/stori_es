package org.consumersunion.stories.dashboard.client.application.widget.content;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.ui.stories.Redrawable;
import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.common.shared.model.document.Document;

import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;

public class DocumentTextCardPresenter extends DocumentCardPresenter<Document, DocumentTextCardUiHandlers>
        implements DocumentTextCardUiHandlers, Redrawable {
    interface MyView extends DocumentCardPresenter.MyView<Document, DocumentTextCardUiHandlers> {
    }

    private final ContentDetailPresenter contentDetailPresenter;

    @Inject
    DocumentTextCardPresenter(
            EventBus eventBus,
            MyView view,
            ContentDetailPresenter contentDetailPresenter,
            @Assisted Document documentText,
            @Assisted ContentKind contentKind) {
        super(eventBus, view, documentText, contentKind);

        this.contentDetailPresenter = contentDetailPresenter;
    }

    @Override
    public void closeCard() {
        getView().setForContentCard();
    }

    @Override
    public void openCard(boolean editMode, boolean addContent) {
        contentDetailPresenter.setContent(document);

        setInSlot(SLOT_DETAILS, contentDetailPresenter);

        getView().setForContentDetails();
    }

    @Override
    public void updateEditedDocument(String title, String contentText) {
        editedDocument.setTitle(title);
        switch (contentKind) {
            case NOTE:
                editedDocument.setOnlyContent(contentText);
                contentDetailPresenter.setContent(editedDocument);
                break;
            case STORY:
            case CONTENT:
                editedDocument.setSummary(contentText);
                contentDetailPresenter.setContent(editedDocument);
                break;
            case ATTACHMENT:
                editedDocument.setOnlyContent(title);
                editedDocument.setPermalink(contentText);
                break;
        }
    }

    @Override
    public boolean hasLocale() {
        return ContentKind.CONTENT.equals(contentKind);
    }

    @Override
    protected Class<Document> getDocumentClass() {
        return Document.class;
    }

    @Override
    protected String getCardTitle() {
        return document.getTitle();
    }
}
