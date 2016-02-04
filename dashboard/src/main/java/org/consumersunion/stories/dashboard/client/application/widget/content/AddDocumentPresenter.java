package org.consumersunion.stories.dashboard.client.application.widget.content;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.service.RpcDocumentServiceAsync;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.model.Locale;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.Content.TextType;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.dashboard.client.event.CancelNewDocumentEvent;
import org.consumersunion.stories.dashboard.client.event.CreateContentEvent;

import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

/**
 * Widget used to create and add new content to a container
 */
public class AddDocumentPresenter
        extends PresenterWidget<AddDocumentPresenter.MyView>
        implements AddDocumentUiHandlers {
    public interface MyView extends View, HasUiHandlers<AddDocumentUiHandlers> {
        void init(ContentKind contentKind);
    }

    private final RpcDocumentServiceAsync documentService;
    private final StoryTellerDashboardI18nLabels labels;
    private final SystemEntity systemEntity;
    private final SystemEntityRelation entityRelation;

    @Inject
    AddDocumentPresenter(
            EventBus eventBus,
            MyView view,
            RpcDocumentServiceAsync documentService,
            StoryTellerDashboardI18nLabels labels,
            @Assisted SystemEntity systemEntity,
            @Assisted SystemEntityRelation entityRelation) {
        super(eventBus, view);

        this.documentService = documentService;
        this.labels = labels;
        this.systemEntity = systemEntity;
        this.entityRelation = entityRelation;

        getView().setUiHandlers(this);
        getView().init(ContentKind.fromRelation(entityRelation));
    }

    @Override
    public void create(String title, String content, Locale locale) {
        switch (entityRelation) {
            case BODY:
            case CONTENT:
                createTextDocument(title, content, locale, ContentKind.CONTENT);
                break;
            case NOTE:
                createTextDocument(title, content, locale, ContentKind.NOTE);
                break;
            case ATTACHMENT:
                createAttachment(title, content);
                break;
        }
    }

    @Override
    public void cancel() {
        CancelNewDocumentEvent.fire(this);
    }

    private void createAttachment(String title, String url) {
        Document newAttachment = new Document();
        newAttachment.setSystemEntityRelation(entityRelation);
        newAttachment.setEntity(systemEntity.getId());
        newAttachment.setTitle(title);
        newAttachment.addBlock(new Content(BlockType.CONTENT, title, TextType.PLAIN));
        newAttachment.setPermalink(url);

        documentService.createAttachment(newAttachment, new ResponseHandlerLoader<DatumResponse<Document>>() {
            @Override
            public void handleSuccess(DatumResponse<Document> result) {
                messageDispatcher.displayMessage(MessageStyle.SUCCESS, labels.attachmentCreated());
                CreateContentEvent.fire(AddDocumentPresenter.this, result.getDatum(), ContentKind.ATTACHMENT);
            }
        });
    }

    private void createTextDocument(String title, String summary, Locale locale, final ContentKind contentKind) {
        Document documentText = new Document();
        documentText.setTitle(title);
        documentText.addBlock(new Content(BlockType.CONTENT, summary, TextType.PLAIN));
        documentText.setSystemEntityRelation(entityRelation);
        documentText.setEntity(systemEntity.getId());
        documentText.setLocale(locale);

        documentService.createDocument(documentText, new ResponseHandlerLoader<DatumResponse<Document>>() {
            @Override
            public void handleSuccess(DatumResponse<Document> result) {
                String message = SystemEntityRelation.BODY.equals(entityRelation) ||
                        SystemEntityRelation.CONTENT.equals(entityRelation)
                        ? labels.contentCreated() : labels.noteCreated();
                messageDispatcher.displayMessage(MessageStyle.SUCCESS, message);
                CreateContentEvent.fire(AddDocumentPresenter.this, result.getDatum(), contentKind);
            }
        });
    }
}
