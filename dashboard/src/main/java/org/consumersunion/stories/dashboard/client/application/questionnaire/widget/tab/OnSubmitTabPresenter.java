package org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab;

import java.util.Set;

import org.consumersunion.stories.common.client.service.RpcEntityServiceAsync;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.dashboard.client.application.collection.CollectionObserver;
import org.consumersunion.stories.dashboard.client.application.util.TagsService;
import org.consumersunion.stories.dashboard.client.event.DisplayEvent;
import org.consumersunion.stories.dashboard.client.event.SavedCollectionEvent;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

/**
 * Presenting handling the edit of a Questionnaire's "autotags"
 */
public class OnSubmitTabPresenter extends PresenterWidget<OnSubmitTabPresenter.MyView>
        implements OnSubmitTabUiHandlers, CollectionObserver.CollectionHandler<Collection> {

    public interface MyView extends View, HasUiHandlers<OnSubmitTabUiHandlers> {
        void setTags(Set<String> tags);

        void populateSuggestionList(Set<String> tags);

        void setFocus();
    }

    private final TagsService tagsService;
    private final RpcEntityServiceAsync entityService;

    private QuestionnaireI15d currentQuestionnaireI15d;

    @Inject
    OnSubmitTabPresenter(
            EventBus eventBus,
            MyView view,
            TagsService tagsService,
            RpcEntityServiceAsync entityService) {
        super(eventBus, view);

        this.tagsService = tagsService;
        this.entityService = entityService;

        getView().setUiHandlers(this);
    }

    @Override
    public void onDisplay(DisplayEvent<Collection> event) {
        Collection collection = event.get();
        if (collection instanceof QuestionnaireI15d) {
            currentQuestionnaireI15d = (QuestionnaireI15d) collection;
            loadAttachedTags();
            loadTagsForSuggestionList();
        }
    }

    @Override
    public void onCollectionSaved(SavedCollectionEvent event) {
    }

    @Override
    public void saveQuestionnaireTags(final Set<String> tags) {
        Questionnaire currentQuestionnaire = currentQuestionnaireI15d.toQuestionnaire();
        if (currentQuestionnaire != null) {
            entityService.updateAutoTags(currentQuestionnaire, tags, new ResponseHandler<ActionResponse>() {
                @Override
                public void handleSuccess(ActionResponse result) {
                    if (tags.isEmpty()) {
                        messageDispatcher.displayMessage(MessageStyle.SUCCESS, "Tags removed successfully.");
                    } else {
                        messageDispatcher.displayMessage(MessageStyle.SUCCESS, "Tags saved successfully.");
                    }
                }
            });
        }
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        getView().setFocus();
    }

    private void loadAttachedTags() {
        Questionnaire currentQuestionnaire = currentQuestionnaireI15d.toQuestionnaire();
        entityService.getAutoTags(currentQuestionnaire, new ResponseHandler<DatumResponse<Set<String>>>() {
            @Override
            public void handleSuccess(DatumResponse<Set<String>> result) {
                getView().setTags(result.getDatum());
            }
        });
    }

    private void loadTagsForSuggestionList() {
        tagsService.loadTagsForSuggestionList(new TagsService.TagsCallback() {
            @Override
            public void onTagsReceived(Set<String> tags) {
                getView().populateSuggestionList(tags);
            }
        });
    }
}
