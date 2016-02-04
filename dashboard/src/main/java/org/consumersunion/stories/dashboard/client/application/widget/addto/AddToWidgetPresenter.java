package org.consumersunion.stories.dashboard.client.application.widget.addto;

import java.util.List;
import java.util.Map;

import org.consumersunion.stories.common.client.ui.stories.StoriesListContainer;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.StorySelectField;
import org.consumersunion.stories.dashboard.client.application.ui.addto.AddToMenuHandler;
import org.consumersunion.stories.dashboard.client.event.StoriesSelectionEvent;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class AddToWidgetPresenter extends PresenterWidget<AddToWidgetPresenter.MyView>
        implements AddToMenuHandler, AddToWidgetUiHandlers {
    interface MyView extends View, HasUiHandlers<AddToWidgetUiHandlers> {
        void reset();

        void hideSlot(Object slot);

        void showSlot(Object slot);
    }

    static final Object SLOT_ADD_TO_COLLECTIONS = new Object();
    static final Object SLOT_ADD_TAGS = new Object();
    static final Object SLOT_ADD_NOTE = new Object();

    private final Map<Object, AddToPresenter> presenters = Maps.newHashMap();
    private final StorySummarySelectionHelper storySummarySelectionHelper;
    private final StoriesListContainer storiesListContainer;

    private Object visibleSlot;

    @Inject
    AddToWidgetPresenter(
            EventBus eventBus,
            MyView view,
            AddToPresenterFactory addToPresenterFactory,
            StorySummarySelectionHelper storySummarySelectionHelper,
            @Assisted StoriesListContainer storiesListContainer) {
        super(eventBus, view);

        getView().setUiHandlers(this);

        this.storySummarySelectionHelper = storySummarySelectionHelper;
        this.storiesListContainer = storiesListContainer;

        storiesListContainer.getRowHandler().addRowCountChangeHandler(new RowCountChangeEvent.Handler() {
            @Override
            public void onRowCountChange(RowCountChangeEvent event) {
                onRowCountChanged();
            }
        });

        AddStoriesToCollectionsPresenter addStoriesToCollections =
                addToPresenterFactory.createAddStoriesToCollections(this, storiesListContainer);
        AddTagsToStoriesPresenter addTagsToStories =
                addToPresenterFactory.createAddTagsToStories(this, storiesListContainer);
        AddNoteToStoriesPresenter addNoteToStories =
                addToPresenterFactory.createAddNoteToStories(this, storiesListContainer);

        setInSlot(SLOT_ADD_TO_COLLECTIONS, addStoriesToCollections);
        setInSlot(SLOT_ADD_TAGS, addTagsToStories);
        setInSlot(SLOT_ADD_NOTE, addNoteToStories);
    }

    @Override
    public void onDisplay() {
        getVisiblePresenter().onDisplay();
    }

    @Override
    public void setInSlot(Object slot, PresenterWidget<?> content) {
        super.setInSlot(slot, content);

        if (content instanceof AddToPresenter) {
            presenters.put(slot, (AddToPresenter) content);
        }
    }

    @Override
    public void onAddToCollections() {
        setVisibleSlot(SLOT_ADD_TO_COLLECTIONS);
    }

    @Override
    public void onAddTags() {
        setVisibleSlot(SLOT_ADD_TAGS);
    }

    @Override
    public void onAddNote() {
        setVisibleSlot(SLOT_ADD_NOTE);
    }

    @Override
    public void hideCurrent() {
        AddToPresenter visiblePresenter = getVisiblePresenter();

        setVisibleSlot(visibleSlot);
        if (visiblePresenter != null) {
            visiblePresenter.reset();
        }
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        hideCurrent();
        resetAllPresenters();
        getView().reset();
    }

    private void resetAllPresenters() {
        for (AddToPresenter presenter : presenters.values()) {
            presenter.reset();
        }
    }

    private void setVisibleSlot(Object slot) {
        if (slot != null) {
            if (visibleSlot == slot) {
                maybeUpdateSelection();

                visibleSlot = null;
                getView().hideSlot(slot);
            } else {
                StorySelectField lastVisibleField = getVisibleSelectField();

                visibleSlot = slot;

                StorySelectField currentSelectField = getVisibleSelectField();
                if (!currentSelectField.equals(lastVisibleField) &&
                        (isSelectedField(lastVisibleField) || isSelectedField(currentSelectField))) {
                    updateSelection(currentSelectField);
                }

                getView().showSlot(slot);
            }
        }

        updateSelectedStoriesCount();
    }

    private void maybeUpdateSelection() {
        StorySelectField currentSelectField = getVisibleSelectField();
        if (isSelectedField(currentSelectField)) {
            updateSelection(null);
        }
    }

    private void updateSelection(StorySelectField currentSelectField) {
        storySummarySelectionHelper.updateSelectionEnabled(storiesListContainer, getSelectedStories(),
                currentSelectField, true);
    }

    private StorySelectField getVisibleSelectField() {
        return visibleSlot == null ? null : getVisiblePresenter().getCurrentSelectField();
    }

    private boolean isSelectedField(StorySelectField currentSelectField) {
        return StorySelectField.SELECTED.equals(currentSelectField);
    }

    private void updateSelectedStoriesCount() {
        if (visibleSlot == null) {
            StoriesSelectionEvent.fire(this, false);
        } else {
            AddToPresenter addToPresenter = getVisiblePresenter();
            addToPresenter.fireStoriesSelectionEvent();
        }
    }

    private AddToPresenter getVisiblePresenter() {
        return presenters.get(visibleSlot);
    }

    private void onRowCountChanged() {
        storySummarySelectionHelper.updateSelectionEnabledNow(storiesListContainer, getSelectedStories(),
                getVisibleSelectField());
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                AddToPresenter visiblePresenter = getVisiblePresenter();
                if (visiblePresenter != null) {
                    visiblePresenter.fireStoriesSelectionEvent();
                }
            }
        });
    }

    private List<Story> getSelectedStories() {
        AddToPresenter visiblePresenter = getVisiblePresenter();
        if (visiblePresenter != null) {
            return visiblePresenter.getSelectedStories();
        } else {
            return Lists.newArrayList();
        }
    }
}
