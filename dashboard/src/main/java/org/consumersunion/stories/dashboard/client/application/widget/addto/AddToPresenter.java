package org.consumersunion.stories.dashboard.client.application.widget.addto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.event.ReloadStoriesEvent;
import org.consumersunion.stories.common.client.i18n.CommonI18nMessages;
import org.consumersunion.stories.common.client.service.datatransferobject.AssignableStory;
import org.consumersunion.stories.common.client.service.response.Response;
import org.consumersunion.stories.common.client.ui.stories.StoriesListContainer;
import org.consumersunion.stories.common.client.util.MessageDispatcher;
import org.consumersunion.stories.common.client.util.ResponseHandlerLoader;
import org.consumersunion.stories.common.client.widget.MessageStyle;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.StorySelectField;
import org.consumersunion.stories.common.shared.service.datatransferobject.StoryAndStorytellerData;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.dashboard.client.application.ui.addto.AddToMenuHandler;
import org.consumersunion.stories.dashboard.client.event.ReloadCollectionsEvent;
import org.consumersunion.stories.dashboard.client.event.StoriesSelectionEvent;
import org.consumersunion.stories.dashboard.client.event.StorySelectedEvent;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;

abstract class AddToPresenter<H extends AddToUiHandlers, V extends AddToView<H>> extends PresenterWidget<V>
        implements AddToUiHandlers, StorySelectedEvent.StorySelectedHandler {
    protected final StoriesListContainer storiesListContainer;
    protected final CommonI18nMessages messages;
    protected final AddToMenuHandler addToMenuHandler;

    @Inject
    protected MessageDispatcher messageDispatcher;
    protected StorySelectField currentSelectField;
    protected List<Story> selectedStories;

    private final StorySummarySelectionHelper storySummarySelectionHelper;

    protected AddToPresenter(
            EventBus eventBus,
            V view,
            StorySummarySelectionHelper storySummarySelectionHelper,
            AddToMenuHandler addToMenuHandler,
            StoriesListContainer storiesListContainer,
            CommonI18nMessages messages) {
        super(eventBus, view);

        this.storySummarySelectionHelper = storySummarySelectionHelper;
        this.addToMenuHandler = addToMenuHandler;
        this.storiesListContainer = storiesListContainer;
        this.messages = messages;
        selectedStories = Lists.newArrayList();

        getView().setUiHandlers((H) this);
    }

    @Override
    public void onStorySelected(StorySelectedEvent event) {
        if (getView().isVisible()) {
            Story story = event.getStory();
            if (selectedStories.contains(story)) {
                selectedStories.remove(story);
            } else {
                selectedStories.add(story);
            }

            if (isForSelectedStories()) {
                fireStoriesSelectionEvent();
            }
        }
    }

    @Override
    public int getNumberOfSelectedStories() {
        switch (currentSelectField) {
            case CURRENT_PAGE_OF:
                return Iterables.size(getVisibleItems());
            case SELECTED:
                return selectedStories.size();
            case ENTIRE_SET:
                return storiesListContainer.getRowHandler().getRowCount();
            default:
                return 0;
        }
    }

    @Override
    public void reset() {
        selectedStories = new ArrayList<Story>();
        currentSelectField = StorySelectField.CURRENT_PAGE_OF;

        getView().reset();
    }

    @Override
    public final void onGoClicked() {
        FluentIterable<AssignableStory> assignableStories;

        if (StorySelectField.ENTIRE_SET.equals(currentSelectField)) {
            onGoClicked(storiesListContainer.getStorySearchParameters(0, 0));
        } else {
            if (isForCurrentPage()) {
                assignableStories = FluentIterable.from(getVisibleItems())
                        .transform(new Function<StorySummary, AssignableStory>() {
                            @Override
                            public AssignableStory apply(StorySummary storySummary) {
                                return new StoryAndStorytellerData(storySummary.getStory());
                            }
                        });
            } else {
                assignableStories = FluentIterable.from(selectedStories)
                        .transform(new Function<Story, AssignableStory>() {
                            @Override
                            public AssignableStory apply(Story selectedStory) {
                                return new StoryAndStorytellerData(selectedStory);
                            }
                        });
            }

            Set<Integer> storyIds = assignableStories.transform(new TransformAssignableStory()).toSet();

            if (!storyIds.isEmpty()) {
                onGoClicked(storyIds);
            }
        }
    }

    public abstract void onGoClicked(Set<Integer> storyIds);

    public abstract void onGoClicked(StorySearchParameters storySearchParameters);

    public void onDisplay() {
        if (!StorySelectField.CURRENT_PAGE_OF.equals(currentSelectField)) {
            onStorySelectFieldChanged(currentSelectField);
        } else {
            fireStoriesSelectionEvent();
        }
    }

    public StorySelectField getCurrentSelectField() {
        return currentSelectField;
    }

    public List<Story> getSelectedStories() {
        return selectedStories;
    }

    @Override
    public void onStorySelectFieldChanged(StorySelectField currentSelectField) {
        boolean isForSelection = StorySelectField.SELECTED.equals(currentSelectField)
                || StorySelectField.SELECTED.equals(this.currentSelectField);

        this.currentSelectField = currentSelectField;

        storySummarySelectionHelper.updateSelectionEnabled(storiesListContainer, selectedStories,
                currentSelectField, isForSelection, new com.google.gwt.query.client.Function() {
                    @Override
                    public void f() {
                        fireStoriesSelectionEvent();
                    }
                });
    }

    public void fireStoriesSelectionEvent() {
        StoriesSelectionEvent.fire(this, getNumberOfSelectedStories(), isForSelectedStories());
    }

    protected final <T extends Response> AsyncCallback<T> createCallback(
            final String successMessage,
            final AsyncCallback<T>... callbacksArray) {
        return new ResponseHandlerLoader<T>() {
            @Override
            public void onFailure(Throwable e) {
                super.onFailure(e);

                AddToPresenter.this.onFailure(e, callbacksArray);
            }

            @Override
            public void handleSuccess(T result) {
                AddToPresenter.this.onSuccess(result, callbacksArray);

                messageDispatcher.displayMessage(MessageStyle.SUCCESS, successMessage);
            }
        };
    }

    protected final <T> AsyncCallback<T> createNoLoaderCallback(
            final String successMessage,
            final AsyncCallback<T>... callbacksArray) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                resetAndHide();
            }
        });

        return new AsyncCallback<T>() {
            @Override
            public void onFailure(Throwable e) {
                AddToPresenter.this.onFailure(e, callbacksArray);
            }

            @Override
            public void onSuccess(T result) {
                doCallbacks(result, callbacksArray);

                if (successMessage != null) {
                    messageDispatcher.displayMessage(MessageStyle.SUCCESS, successMessage);
                }
            }
        };
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        reset();
    }

    @Override
    protected void onBind() {
        super.onBind();

        addVisibleHandler(StorySelectedEvent.TYPE, this);

        reset();
    }

    protected Iterable<StorySummary> getVisibleItems() {
        if (storiesListContainer.getStoriesList() == null) {
            return Lists.newArrayList();
        } else {
            return storiesListContainer.getStoriesList();
        }
    }

    protected boolean isForCurrentPage() {
        return currentSelectField.equals(StorySelectField.CURRENT_PAGE_OF);
    }

    protected boolean isForSelectedStories() {
        return currentSelectField.equals(StorySelectField.SELECTED);
    }

    protected void onSaveSuccess() {
        resetAndHide();
        ReloadCollectionsEvent.fire(this, null);
        ReloadStoriesEvent.fire(this);
    }

    private void resetAndHide() {
        addToMenuHandler.hideCurrent();
        reset();
    }

    private <T> void onFailure(Throwable e, AsyncCallback<T>[] callbacks) {
        for (AsyncCallback<T> callback : callbacks) {
            callback.onFailure(e);
        }
    }

    private <T> void onSuccess(
            T result,
            AsyncCallback<T>[] callbacks) {
        onSaveSuccess();

        doCallbacks(result, callbacks);
    }

    private <T> void doCallbacks(T result, AsyncCallback<T>[] callbacks) {
        for (AsyncCallback<T> callback : callbacks) {
            callback.onSuccess(result);
        }
    }
}
