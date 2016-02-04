package org.consumersunion.stories.dashboard.client.application.widget;

import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.RpcEntityServiceAsync;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.util.MessageDispatcher;
import org.consumersunion.stories.common.client.util.ResponseHandler;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.dashboard.client.application.util.TagsService;

import com.google.common.collect.Sets;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import edu.umd.cs.findbugs.annotations.Nullable;

/**
 * Widget used to add/remove tags. It is initialized through the {@link #setTags(java.util.List)} method,
 * and updates are notified to the
 * {@link org.consumersunion.stories.dashboard.client.application.widget.TagsPresenter.Handler}
 */
public class TagsPresenter extends PresenterWidget<TagsPresenter.MyView> implements TagsUiHandlers {
    public interface Handler {
        void onTagAdded(String token);

        void onTagRemoved(String token);

        void onEditComplete();

        void onTagsReceived();
    }

    public interface SearchTagHandler {
        void onSearchTag(String tag);
    }

    interface MyView extends View, HasUiHandlers<TagsUiHandlers> {
        void setSuggestions(Set<String> tags);

        void addTag(String tag);

        void startEdit();

        void clearTags();
    }

    private final PlaceManager placeManager;
    private final MessageDispatcher messageDispatcher;
    private final RpcEntityServiceAsync entityService;
    private final TagsService tagsService;
    @Nullable
    private final Handler handler;

    private SearchTagHandler searchTagHandler;
    private SystemEntity entity;
    private Set<String> tags;
    private boolean withNotification;

    @Inject
    @AssistedInject
    TagsPresenter(
            EventBus eventBus,
            MyView view,
            PlaceManager placeManager,
            MessageDispatcher messageDispatcher,
            RpcEntityServiceAsync entityService,
            TagsService tagsService) {
        this(eventBus, view, placeManager, messageDispatcher, entityService, tagsService, null);
    }

    @AssistedInject
    TagsPresenter(
            EventBus eventBus,
            MyView view,
            PlaceManager placeManager,
            MessageDispatcher messageDispatcher,
            RpcEntityServiceAsync entityService,
            TagsService tagsService,
            @Nullable @Assisted Handler handler) {
        super(eventBus, view);

        this.placeManager = placeManager;
        this.messageDispatcher = messageDispatcher;
        this.entityService = entityService;
        this.tagsService = tagsService;
        this.handler = handler;
        this.tags = Sets.newLinkedHashSet();

        getView().setUiHandlers(this);
    }

    public void setSearchTagHandler(SearchTagHandler searchTagHandler) {
        this.searchTagHandler = searchTagHandler;
    }

    public void setEntity(SystemEntity entity, boolean withNotification) {
        this.withNotification = withNotification;
        setEntity(entity);
    }

    public void setTags(SystemEntity entity, Set<String> tags) {
        this.entity = entity;
        setTags(tags);
    }

    public void setEntity(SystemEntity entity) {
        this.entity = entity;

        entityService.getTags(entity, new AsyncCallback<DatumResponse<Set<String>>>() {
            @Override
            public void onFailure(Throwable caught) {
                setTags(Sets.<String>newLinkedHashSet());
            }

            @Override
            public void onSuccess(DatumResponse<Set<String>> result) {
                setTags(result.getDatum());
            }
        });
    }

    public Set<String> getTags() {
        return tags;
    }

    public void refreshSuggestions() {
        tagsService.loadTagsForSuggestionList(new TagsService.TagsCallback() {
            @Override
            public void onTagsReceived(Set<String> tags) {
                getView().setSuggestions(tags);
            }
        });
    }

    public boolean hasTags() {
        return !tags.isEmpty();
    }

    public void startEdit() {
        refreshSuggestions();
        getView().startEdit();
    }

    @Override
    public void onTagAdded(String tag) {
        if (!tag.isEmpty() && !tags.contains(tag)) {
            getView().addTag(tag);
            tags.add(tag);

            saveTags();

            if (handler != null) {
                handler.onTagAdded(tag);
            }
        }
    }

    @Override
    public void onTagRemoved(String tag) {
        tags.remove(tag);

        saveTags();

        if (handler != null) {
            handler.onTagRemoved(tag);
        }
    }

    @Override
    public void onTagClicked(String tag) {
        if (searchTagHandler == null) {
            PlaceRequest placeRequest = placeManager.getCurrentPlaceRequest();
            placeRequest = new PlaceRequest.Builder(placeRequest)
                    .without(ParameterTokens.page)
                    .with(ParameterTokens.search, "tag:(" + tag + ")")
                    .build();

            placeManager.revealPlace(placeRequest);
        } else {
            searchTagHandler.onSearchTag(tag);
        }
    }

    @Override
    public void onEditComplete() {
        if (handler != null) {
            handler.onEditComplete();
        }
    }

    private void setTags(Set<String> tags) {
        this.tags = tags;

        getView().clearTags();

        for (String tag : tags) {
            getView().addTag(tag);
        }

        if (handler != null) {
            handler.onTagsReceived();
        }
    }

    private void saveTags() {
        if (entity != null) {
            final boolean hasTags = hasTags();
            entityService.setTags(entity, tags, new ResponseHandler<ActionResponse>() {
                @Override
                public void handleSuccess(ActionResponse result) {
                    onTagsSaved(hasTags);
                }
            });
        }
    }

    private void onTagsSaved(boolean hasTags) {
        if (withNotification) {
            if (hasTags) {
                messageDispatcher.displayMessage(MessageStyle.SUCCESS, "Tags saved successfully.");
            } else {
                messageDispatcher.displayMessage(MessageStyle.SUCCESS, "Tags removed successfully.");
            }
        }
    }
}
