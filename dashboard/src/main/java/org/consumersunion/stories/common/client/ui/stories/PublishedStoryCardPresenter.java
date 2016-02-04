package org.consumersunion.stories.common.client.ui.stories;

import org.consumersunion.stories.common.client.event.RedrawEvent;
import org.consumersunion.stories.common.client.service.RpcStoryServiceAsync;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.ui.block.ContentElement;
import org.consumersunion.stories.common.client.util.PublicResponseHandler;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class PublishedStoryCardPresenter extends PresenterWidget<PublishedStoryCardPresenter.MyView>
        implements StoryCard, ContentElement<Content> {
    public interface MyView extends View {
        void init(StorySummary storySummary);
    }

    private final RpcStoryServiceAsync storyService;

    private StorySummary storySummary;

    @AssistedInject
    PublishedStoryCardPresenter(
            EventBus eventBus,
            MyView view,
            RpcStoryServiceAsync storyService,
            @Assisted PresenterWidget ignored,
            @Assisted StorySummary storySummary) {
        super(eventBus, view);

        this.storyService = storyService;
        this.storySummary = storySummary;

        redraw();
    }

    @AssistedInject
    PublishedStoryCardPresenter(
            EventBus eventBus,
            MyView view,
            RpcStoryServiceAsync storyService,
            @Assisted Content content) {
        super(eventBus, view);

        this.storyService = storyService;

        display(content);
    }

    @Override
    public void redraw() {
        getView().init(storySummary);

        RedrawEvent.fire(this);
    }

    @Override
    public StorySummary getStorySummary() {
        return storySummary;
    }

    @Override
    public void display(Content content) {
        String contentValue = content.getContent();
        if (contentValue != null) {

            String storyId = contentValue.split("/")[1];
            storyService.getStorySummary(Integer.parseInt(storyId), true,
                    new PublicResponseHandler<DatumResponse<StorySummary>>() {
                        @Override
                        public void handleSuccess(DatumResponse<StorySummary> result) {
                            storySummary = result.getDatum();
                            redraw();
                        }
                    });
        }
    }
}
