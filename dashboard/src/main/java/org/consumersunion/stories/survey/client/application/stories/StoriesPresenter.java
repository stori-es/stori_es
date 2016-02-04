package org.consumersunion.stories.survey.client.application.stories;

import java.util.List;

import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.survey.client.application.ApplicationPresenter;
import org.consumersunion.stories.survey.client.common.MethodCallbackImpl;
import org.consumersunion.stories.survey.client.gin.SurveyBootstrapper;
import org.consumersunion.stories.survey.client.place.NameTokens;
import org.consumersunion.stories.survey.client.rest.StoryService;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

public class StoriesPresenter extends Presenter<StoriesPresenter.MyView, StoriesPresenter.MyProxy> {
    interface MyView extends View {
        void setData(List<StorySummary> data);
    }

    @ProxyStandard
    @NameToken(NameTokens.stories)
    interface MyProxy extends ProxyPlace<StoriesPresenter> {
    }

    private final StoryService storyService;
    private final SurveyBootstrapper bootStrapper;

    @Inject
    StoriesPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            StoryService storyService,
            SurveyBootstrapper bootStrapper) {
        super(eventBus, view, proxy, ApplicationPresenter.TYPE_SetMainContent);

        this.storyService = storyService;
        this.bootStrapper = bootStrapper;
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        loadStories();
    }

    private void loadStories() {
        storyService.getRecentStories(bootStrapper.getCollection().getId(),
                new MethodCallbackImpl<DataResponse<StorySummary>>() {
                    @Override
                    public void handleSuccess(DataResponse<StorySummary> response) {
                        getView().setData(response.getData());
                    }
                });
    }
}
