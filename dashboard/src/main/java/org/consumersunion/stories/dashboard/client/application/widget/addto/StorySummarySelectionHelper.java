package org.consumersunion.stories.dashboard.client.application.widget.addto;

import java.util.List;

import org.consumersunion.stories.common.client.ui.stories.StoriesListContainer;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.StorySelectField;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.ui.Widget;

import static com.google.gwt.query.client.GQuery.$;

public class StorySummarySelectionHelper {
    public void updateSelectionEnabled(
            final StoriesListContainer storiesListContainer,
            final List<Story> selectedStories,
            final StorySelectField currentField,
            boolean isForSelection,
            final Function... functions) {
        final Widget storiesListWidget = (Widget) storiesListContainer.getWidgetContainer();

        Function redrawFunction = new Function() {
            @Override
            public void f() {
                updateSelectionState(storiesListContainer, selectedStories, isForSelection(currentField));

                for (Function function : functions) {
                    function.f();
                }

                $(storiesListWidget).fadeIn();
            }
        };

        if (isForSelection) {
            $(storiesListWidget).fadeOut(redrawFunction);
        } else {
            redrawFunction.f();
        }
    }

    public void updateSelectionEnabledNow(
            StoriesListContainer storiesListContainer,
            List<Story> selectedStories,
            StorySelectField currentField) {
        updateSelectionState(storiesListContainer, selectedStories, isForSelection(currentField));
    }

    private void updateSelectionState(
            StoriesListContainer storiesListContainer,
            List<Story> selectedStories,
            boolean setForSelection) {
        List<StorySummary> storiesList = storiesListContainer.getStoriesList();
        for (StorySummary storySummary : storiesList) {
            if (setForSelection && isStorySelected(selectedStories, storySummary)) {
                storySummary.setSelected(true);
            }
        }

        removeHiddenStoriesFromSelection(selectedStories, storiesList);

        storiesListContainer.redraw();
    }

    private boolean isStorySelected(List<Story> selectedStories, final StorySummary storySummary) {
        return Iterables.tryFind(selectedStories, new Predicate<Story>() {
            @Override
            public boolean apply(Story input) {
                return input.getId() == storySummary.getStoryId();
            }
        }).isPresent();
    }

    private void removeHiddenStoriesFromSelection(List<Story> selectedStories, List<StorySummary> storiesList) {
        final List<Story> stories = Lists.transform(storiesList,
                new com.google.common.base.Function<StorySummary, Story>() {
                    @Override
                    public Story apply(StorySummary input) {
                        return input.getStory();
                    }
                }
        );

        Iterables.removeIf(selectedStories, new Predicate<Story>() {
            @Override
            public boolean apply(Story input) {
                return !stories.contains(input);
            }
        });
    }

    private boolean isForSelection(StorySelectField currentField) {
        return StorySelectField.SELECTED.equals(currentField);
    }
}
