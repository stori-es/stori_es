package org.consumersunion.stories.dashboard.client.application.collections.widget;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.client.service.RpcQuestionnaireServiceAsync;
import org.consumersunion.stories.common.client.service.response.CollectionSurveyI15dResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Locale;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireBase;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.dashboard.client.application.widget.content.AddDocumentPresenter;
import org.consumersunion.stories.dashboard.client.application.widget.content.AddDocumentUiHandlers;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;

import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class NewCollectionPresenter extends PresenterWidget<AddDocumentPresenter.MyView>
        implements AddDocumentUiHandlers {
    private final ContentKind contentKind;
    private final PlaceManager placeManager;
    private final RpcCollectionServiceAsync collectionService;
    private final RpcQuestionnaireServiceAsync questionnaireService;

    @Inject
    NewCollectionPresenter(
            EventBus eventBus,
            PlaceManager placeManager,
            AddDocumentPresenter.MyView view,
            RpcCollectionServiceAsync collectionService,
            RpcQuestionnaireServiceAsync questionnaireService,
            @Assisted ContentKind contentKind) {
        super(eventBus, view);

        this.placeManager = placeManager;
        this.collectionService = collectionService;
        this.questionnaireService = questionnaireService;
        this.contentKind = contentKind;

        getView().setUiHandlers(this);
        getView().init(contentKind);
    }

    @Override
    public void create(String title, String summary, Locale locale) {
        switch (contentKind) {
            case QUESTIONNAIRE:
                QuestionnaireI15d questionnaire = createBaseObject(new QuestionnaireI15d(), title, summary, locale);
                createQuestionnaire(questionnaire);
                break;
            case COLLECTION:
                Collection collection = createBaseObject(new Collection(), title, summary, locale);
                createCollection(collection);
                break;
            default:
                throw new IllegalArgumentException(contentKind.name());
        }
    }

    @Override
    public void cancel() {
    }

    private <T extends Collection> T createBaseObject(T collection, String title, String summary, Locale locale) {
        Document bodyDocument = collection.getBodyDocument();
        bodyDocument.setTitle(title);
        bodyDocument.setSummary(summary);
        bodyDocument.setLocale(locale);
        collection.setPublic(true);
        collection.setQuestionnaire(collection instanceof QuestionnaireBase);

        return collection;
    }

    private void createCollection(Collection newCollection) {
        collectionService.createCollection(newCollection, new ResponseHandlerLoader<DatumResponse<Collection>>() {
            @Override
            public void handleSuccess(DatumResponse<Collection> result) {
                goToCollectionPage(result.getDatum().getId());
            }
        });
    }

    private void createQuestionnaire(QuestionnaireI15d newQuestionnaire) {
        questionnaireService.saveQuestionnaire(newQuestionnaire,
                new ResponseHandlerLoader<CollectionSurveyI15dResponse>() {
                    @Override
                    public void handleSuccess(CollectionSurveyI15dResponse result) {
                        goToCollectionPage(result.getQuestionnaire().getId());
                    }
                });
    }

    private void goToCollectionPage(int id) {
        PlaceRequest place = new PlaceRequest.Builder()
                .nameToken(NameTokens.collection)
                .with(ParameterTokens.id, String.valueOf(id))
                .build();
        placeManager.revealPlace(place);
    }
}
