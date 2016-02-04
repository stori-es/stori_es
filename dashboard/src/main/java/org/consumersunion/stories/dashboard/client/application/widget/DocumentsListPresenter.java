package org.consumersunion.stories.dashboard.client.application.widget;

import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.service.datatransferobject.Documents;
import org.consumersunion.stories.common.shared.service.datatransferobject.DocumentsContainer;
import org.consumersunion.stories.dashboard.client.application.widget.content.DocumentCardFactory;
import org.consumersunion.stories.dashboard.client.event.AddNewDocumentEvent;
import org.consumersunion.stories.dashboard.client.event.CancelNewDocumentEvent;
import org.consumersunion.stories.dashboard.client.event.CreateContentEvent;

import com.google.common.collect.FluentIterable;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

import static org.consumersunion.stories.dashboard.client.util.Comparators.ATTACHMENT_TITLE_AZ_COMPARATOR;
import static org.consumersunion.stories.dashboard.client.util.Comparators.NOTE_CREATED_TIME_COMPARATOR;

public class DocumentsListPresenter extends PresenterWidget<DocumentsListPresenter.MyView>
        implements CreateContentEvent.CreateNewContentHandler, CancelNewDocumentEvent.CancelNewDocumentHandler,
        AddNewDocumentEvent.AddNewDocumentHandler {
    interface MyView extends View {
        void setVisible(boolean visible);
    }

    static final Object SLOT_DOCUMENTS = new Object();

    private final DocumentCardFactory documentCardFactory;

    @Inject
    DocumentsListPresenter(
            EventBus eventBus,
            MyView view,
            DocumentCardFactory documentCardFactory) {
        super(eventBus, view);

        this.documentCardFactory = documentCardFactory;
    }

    @Override
    public void onCancelNewDocument(CancelNewDocumentEvent event) {
        getView().setVisible(true);
    }

    @Override
    public void onCreateNewContent(CreateContentEvent event) {
        addToSlot(SLOT_DOCUMENTS,
                documentCardFactory.createContentCard(event.getDocument(), event.getContentKind()));
        getView().setVisible(true);
    }

    @Override
    public void onAddNewDocument(AddNewDocumentEvent event) {
        getView().setVisible(false);
    }

    public void clear() {
        clearSlot(SLOT_DOCUMENTS);
    }

    public void setDocuments(DocumentsContainer documents, SystemEntityRelation... relations) {
        clearSlot(SLOT_DOCUMENTS);

        for (SystemEntityRelation relation : relations) {
            switch (relation) {
                case CONTENT:
                case BODY:
                    addDocuments(documents.getDocuments(relation), null);
                    break;
                case NOTE:
                    addDocuments(documents.getDocuments(SystemEntityRelation.NOTE), NOTE_CREATED_TIME_COMPARATOR);
                    break;
                case ANSWER_SET:
                    addDocuments(documents.getDocuments(SystemEntityRelation.ANSWER_SET), null);
                    break;
                case ATTACHMENT:
                    addDocuments(documents.getDocuments(SystemEntityRelation.ATTACHMENT),
                            ATTACHMENT_TITLE_AZ_COMPARATOR);
                    break;
            }
        }
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        getView().setVisible(true);
    }

    @Override
    protected void onBind() {
        super.onBind();

        addVisibleHandler(CancelNewDocumentEvent.TYPE, this);
        addVisibleHandler(CreateContentEvent.TYPE, this);
        addVisibleHandler(AddNewDocumentEvent.TYPE, this);
    }

    private void addDocuments(Documents documents, Comparator<Document> predicate) {
        List<Document> sortedDocuments = documents.getDocuments();
        if (predicate != null) {
            sortedDocuments = FluentIterable.from(documents.getDocuments()).toSortedList(predicate);
        }

        for (Document document : sortedDocuments) {
            addDocument(document);
        }
    }

    private void addDocument(Document document) {
        addToSlot(SLOT_DOCUMENTS, documentCardFactory.createContentCard(document,
                ContentKind.fromRelation(document.getSystemEntityRelation())));
    }
}
