package org.consumersunion.stories.dashboard.client.application.widget.content;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.service.RpcDocumentServiceAsync;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.ui.stories.Redrawable;
import org.consumersunion.stories.common.client.util.CachedObjectKeys;
import org.consumersunion.stories.common.client.util.CachingService;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.client.util.StoriesJsonEncoderDecoder;
import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.model.Locale;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.dashboard.client.event.SavedDocumentEvent;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public abstract class DocumentCardPresenter<T extends Document, H extends DocumentCardUiHandlers>
        extends PresenterWidget<DocumentCardPresenter.MyView<T, H>>
        implements DocumentCardUiHandlers, Redrawable {
    interface MyView<T extends Document, H extends DocumentCardUiHandlers> extends View, HasUiHandlers<H> {
        void init(T document, String title, ContentKind contentKind);

        void setForContentDetails();

        void setForContentCard();

        void setEditMode(boolean isEditMode);

        void saveFailed();

        void saveSuccess(boolean done);
    }

    static final Object SLOT_DETAILS = new Object();

    protected final ContentKind contentKind;

    protected T document;
    protected T editedDocument;
    protected boolean isEditMode;

    @Inject
    protected RpcDocumentServiceAsync documentService;
    @Inject
    protected StoriesJsonEncoderDecoder jsonEncoderDecoder;
    @Inject
    protected CachingService cachingService;
    @Inject
    protected StoryTellerDashboardI18nLabels labels;

    DocumentCardPresenter(
            EventBus eventBus,
            MyView<T, H> view,
            T document,
            ContentKind contentKind) {
        super(eventBus, view);

        this.document = document;
        this.contentKind = contentKind;

        getView().setUiHandlers((H) this);
    }

    @Override
    public void redraw() {
        getView().init(document, getCardTitle(), contentKind);
    }

    @Override
    public void cancelEdit() {
        getView().init(document, getCardTitle(), contentKind);
    }

    @Override
    public void edit() {
        isEditMode = true;
        documentService.getDocument(document.getId(),
                new ResponseHandlerLoader<DatumResponse<Document>>() {
                    @Override
                    public void handleSuccess(DatumResponse<Document> result) {
                        onDocumentFetched(result);
                        doEdit(editedDocument);
                    }
                });
    }

    @Override
    public void save(final boolean done) {
        documentService.saveDocument(editedDocument, new ResponseHandlerLoader<DatumResponse<Document>>() {
            @Override
            public void handleSuccess(DatumResponse<Document> result) {
                onDocumentFetched(result);

                SavedDocumentEvent.fire(DocumentCardPresenter.this, document);

                getView().saveSuccess(done);
                messageDispatcher.displayMessage(MessageStyle.SUCCESS, labels.elementSaved(contentKind.toString()));
            }

            @Override
            public void onFailure(Throwable e) {
                super.onFailure(e);

                getView().saveFailed();
            }
        });
    }

    @Override
    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    @Override
    public void updateLocale(Locale locale) {
        editedDocument.setLocale(locale);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        redraw();

        getView().setForContentCard();

        if (isEditMode) {
            T openedContentData = cachingService.getCachedObject(getDocumentClass(), CachedObjectKeys.OPENED_CONTENT);
            doEdit(openedContentData);
        }
    }

    protected abstract Class<T> getDocumentClass();

    protected abstract String getCardTitle();

    protected void onDocumentFetched(DatumResponse<Document> result) {
        document = (T) result.getDatum();
        editedDocument = jsonEncoderDecoder.clone(getDocumentClass(), document);
        cachingService.putCachedObject(getDocumentClass(), CachedObjectKeys.OPENED_CONTENT, document);
        redraw();
    }

    protected void doEdit(T data) {
        editedDocument = data;
        getView().setEditMode(isEditMode);
    }
}
