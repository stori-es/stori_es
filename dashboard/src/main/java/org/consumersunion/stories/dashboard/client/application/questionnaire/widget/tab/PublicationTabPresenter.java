package org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab;

import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.dashboard.client.application.collection.CollectionObserver;
import org.consumersunion.stories.dashboard.client.event.DisplayEvent;
import org.consumersunion.stories.dashboard.client.event.SavedCollectionEvent;
import org.consumersunion.stories.dashboard.client.util.AbstractAsyncCallback;

import com.google.gwt.http.client.Request;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

/**
 * Presenting handling the edit of a Questionnaire's permalink and published status
 */
public class PublicationTabPresenter extends PresenterWidget<PublicationTabPresenter.MyView>
        implements PublicationTabUiHandlers, CollectionObserver.CollectionHandler<Collection> {
    public interface MyView extends View, HasUiHandlers<PublicationTabUiHandlers> {
        void refreshView(Collection collection);

        void updatePermalink(Collection collection);

        void setPublished(boolean published);

        void setLinkDisponibility(Boolean available);

        void setPermalinkUrl(String url);
    }

    private final PermalinkUtil permalinkUtil;
    private final PermalinkValidator permalinkValidator;
    private final CollectionObserver collectionObserver;
    private final RpcCollectionServiceAsync collectionService;

    private Request lastRequest;

    @Inject
    PublicationTabPresenter(
            EventBus eventBus,
            MyView view,
            PermalinkUtil permalinkUtil,
            PermalinkValidator permalinkValidator,
            CollectionObserver collectionObserver,
            RpcCollectionServiceAsync collectionService) {
        super(eventBus, view);

        this.permalinkUtil = permalinkUtil;
        this.permalinkValidator = permalinkValidator;
        this.collectionObserver = collectionObserver;
        this.collectionService = collectionService;

        getView().setUiHandlers(this);
    }

    @Override
    public void onDisplay(DisplayEvent<Collection> event) {
        Collection collection = event.get();
        getView().refreshView(collection);
        updatePermalinkTargetUrl();
    }

    @Override
    public void onCollectionSaved(SavedCollectionEvent event) {
        Collection collection = event.get();
        getView().setPublished(collection.isPublished());
        updatePermalinkTargetUrl();

        getView().updatePermalink(collection);
    }

    @Override
    public void toggleState() {
        Collection collection = collectionObserver.getCollection();
        collection.setPublished(!collection.isPublished());
        collectionObserver.save();
    }

    @Override
    public void updatePermalink(String link) {
        if (!isSamePermalink(link)) {
            Collection collection = collectionObserver.getCollection();
            String contextLink = getContextPermalink(link, collection);
            collection.setPermalink(contextLink);

            collectionObserver.save();
        }
    }

    @Override
    public void checkIfLinkExists(String collectionLink) {
        if (isSamePermalink(collectionLink)) {
            getView().setLinkDisponibility(true);
        } else if (permalinkValidator.isValidLink(collectionLink)) {
            if (lastRequest != null && lastRequest.isPending()) {
                lastRequest.cancel();
            }

            lastRequest = collectionService.checkUnusedLink(collectionLink,
                    new AbstractAsyncCallback<DatumResponse<Collection>>() {
                        @Override
                        public void onSuccess(final DatumResponse<Collection> result) {
                            getView().setLinkDisponibility(result.getDatum() == null);
                            result.getGlobalErrorMessages().clear();
                        }
                    });
        } else {
            getView().setLinkDisponibility(false);
        }
    }

    private String getContextPermalink(String link, Collection collection) {
        return (collection.isQuestionnaire() ? "/questionnaires/" : "/collections/") + link;
    }

    private boolean isSamePermalink(String collectionLink) {
        Collection collection = collectionObserver.getCollection();
        return collection.getPermalink().equals(getContextPermalink(collectionLink, collection));
    }

    private void updatePermalinkTargetUrl() {
        Collection collection = collectionObserver.getCollection();
        String url = permalinkUtil.getPermalink(collection.getPermalink(), collection.getPreviewKey());

        getView().setPermalinkUrl(url);
    }
}
