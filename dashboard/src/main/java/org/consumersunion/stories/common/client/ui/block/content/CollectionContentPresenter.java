package org.consumersunion.stories.common.client.ui.block.content;

import org.consumersunion.stories.common.client.ui.block.ContentElement;
import org.consumersunion.stories.common.client.ui.stories.ListStoriesPresenter;
import org.consumersunion.stories.common.client.ui.stories.StoriesListHandler;
import org.consumersunion.stories.common.shared.model.StorySortField;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySearchParameters;

import com.google.common.base.Strings;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.CustomPresenterWidget;
import com.gwtplatform.mvp.client.View;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_ANY;

public class CollectionContentPresenter extends CustomPresenterWidget<CollectionContentPresenter.MyView>
        implements ContentElement<Content>, StoriesListHandler {
    public interface MyView extends View {
        void setNoStories(boolean noStories);
    }

    static final Object SLOT_STORIES = new Object();

    private final ListStoriesPresenter listStoriesPresenter;

    private Integer collectionId;

    @Inject
    CollectionContentPresenter(EventBus eventBus,
            MyView view,
            ListStoriesPresenter listStoriesPresenter,
            @Assisted Content content) {
        super(eventBus, view);

        this.listStoriesPresenter = listStoriesPresenter;

        display(content);
    }

    @Override
    public void display(Content content) {
        if (!Strings.isNullOrEmpty(content.getContent())) {
            collectionId = Integer.valueOf(content.getContent());
        } else {
            collectionId = null;
        }

        if (collectionId != null) {
            setInSlot(SLOT_STORIES, listStoriesPresenter);
        } else {
            clearSlot(SLOT_STORIES);
        }

        listStoriesPresenter.initPresenter(this);
        forceReveal();
    }

    @Override
    public StorySearchParameters getStorySearchParameters(int start, int length) {
        StorySearchParameters storySearchParameters = new StorySearchParameters(start, length, collectionId, null, "",
                null, null, ACCESS_MODE_ANY, StorySortField.CREATED_NEW);
        storySearchParameters.setIncludeFullText(true);

        return storySearchParameters;
    }

    @Override
    public void onRowCountChange(RowCountChangeEvent event) {
        getView().setNoStories(event.getNewRowCount() == 0);
    }
}
