package org.consumersunion.stories.dashboard.client.application.collection;

import java.util.Map;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.event.HideLoadingEvent;
import org.consumersunion.stories.common.client.event.ShowLoadingEvent;
import org.consumersunion.stories.common.client.i18n.StoryTellerDashboardI18nLabels;
import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.client.service.RpcQuestionnaireServiceAsync;
import org.consumersunion.stories.common.client.service.response.CollectionSurveyI15dResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.dashboard.client.event.DisplayEvent;
import org.consumersunion.stories.dashboard.client.event.SavedCollectionEvent;
import org.consumersunion.stories.dashboard.client.event.SavedCollectionEvent.SaveCollectionHandler;

import com.google.common.collect.Maps;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

import static org.consumersunion.stories.dashboard.client.event.DisplayEvent.DisplayHandler;

public class CollectionObserver implements HasHandlers {
    public interface CollectionHandler<T extends Collection> extends DisplayHandler<T>, SaveCollectionHandler {
        boolean isVisible();
    }

    private static class CollectionHandlerRegistration<T extends Collection> implements HandlerRegistration {
        private final HandlerRegistration displayRegistration;
        private final HandlerRegistration saveRegistration;

        private CollectionHandlerRegistration(EventBus eventBus, final CollectionHandler handler) {
            displayRegistration = eventBus.addHandler(DisplayEvent.TYPE, handler);
            saveRegistration = eventBus.addHandler(SavedCollectionEvent.TYPE, handler);
        }

        public void removeHandler() {
            displayRegistration.removeHandler();
            saveRegistration.removeHandler();
        }
    }

    private final EventBus eventBus;
    private final RpcQuestionnaireServiceAsync questionnaireService;
    private final RpcCollectionServiceAsync collectionService;
    private final String questionnaireSaved;
    private final String collectionSaved;
    private final Map<CollectionHandler, HandlerRegistration> handlers = Maps.newHashMap();

    private Collection collection;

    @Inject
    CollectionObserver(
            EventBus eventBus,
            RpcQuestionnaireServiceAsync questionnaireService,
            RpcCollectionServiceAsync collectionService,
            StoryTellerDashboardI18nLabels labels) {
        this.eventBus = eventBus;
        this.questionnaireService = questionnaireService;
        this.collectionService = collectionService;
        questionnaireSaved = labels.questionnaireSaved();
        collectionSaved = labels.collectionSaved();
    }

    public void save() {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                ShowLoadingEvent.fire(CollectionObserver.this);
            }
        });
        if (collection instanceof QuestionnaireI15d) {
            saveQuestionnaire();
        } else {
            saveCollection();
        }
    }

    public Collection getCollection() {
        return collection;
    }

    public void display(Collection collection) {
        setData(collection);
        DisplayEvent.fire(this, collection);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        eventBus.fireEvent(event);
    }

    public <T extends Collection> void register(final CollectionHandler<T> handler) {
        if (!handlers.containsKey(handler)) {
            handlers.put(handler, new CollectionHandlerRegistration(eventBus, handler));
        }
    }

    public <T extends Collection> void register(CollectionHandler<T>... handlers) {
        for (CollectionHandler<T> handler : handlers) {
            if (!this.handlers.containsKey(handler)) {
                register(handler);
            }
        }
    }

    public <T extends Collection> void unregister(CollectionHandler<T> handler) {
        HandlerRegistration handlerRegistration = handlers.get(handler);
        if (handlerRegistration != null) {
            handlerRegistration.removeHandler();
            handlers.remove(handler);
        }
    }

    private void saveQuestionnaire() {
        questionnaireService.saveQuestionnaire((QuestionnaireI15d) collection,
                new ResponseHandler<CollectionSurveyI15dResponse>() {
                    @Override
                    public void onFailure(Throwable e) {
                        super.onFailure(e);

                        HideLoadingEvent.fire(CollectionObserver.this);
                    }

                    @Override
                    public void handleSuccess(CollectionSurveyI15dResponse result) {
                        messageDispatcher.displayMessage(MessageStyle.SUCCESS, questionnaireSaved);

                        onSaveSuccess(result.getQuestionnaire());
                    }
                }
        );
    }

    private void saveCollection() {
        collectionService.updateCollection(collection, new ResponseHandler<DatumResponse<Collection>>() {
            @Override
            public void onFailure(Throwable e) {
                super.onFailure(e);

                HideLoadingEvent.fire(CollectionObserver.this);
            }

            @Override
            public void handleSuccess(DatumResponse<Collection> result) {
                messageDispatcher.displayMessage(MessageStyle.SUCCESS, collectionSaved);

                onSaveSuccess(result.getDatum());
            }
        });
    }

    private void onSaveSuccess(Collection collection) {
        setData(collection);
        SavedCollectionEvent.fire(this, collection);
        HideLoadingEvent.fire(CollectionObserver.this);
    }

    private void setData(Collection collection) {
        this.collection = collection;
    }
}
