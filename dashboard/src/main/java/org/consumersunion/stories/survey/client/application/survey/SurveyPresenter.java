package org.consumersunion.stories.survey.client.application.survey;

import java.util.List;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire;
import org.consumersunion.stories.survey.client.application.ApplicationPresenter;
import org.consumersunion.stories.survey.client.common.MethodCallbackLoader;
import org.consumersunion.stories.survey.client.gin.SurveyBootstrapper;
import org.consumersunion.stories.survey.client.place.NameTokens;
import org.consumersunion.stories.survey.client.rest.CollectionService;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class SurveyPresenter
        extends Presenter<SurveyPresenter.MyView, SurveyPresenter.MyProxy>
        implements SurveyUiHandlers {
    public interface MyView extends View, HasUiHandlers<SurveyUiHandlers> {
        void setData(Collection collection, Boolean displayTitle);

        void hideSubmit();
    }

    @ProxyStandard
    @NameToken(NameTokens.survey)
    interface MyProxy extends ProxyPlace<SurveyPresenter> {
    }

    private final SurveyBootstrapper bootStrapper;
    private final CollectionService collectionService;
    private final PlaceManager placeManager;

    private Collection collection;

    @Inject
    SurveyPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            SurveyBootstrapper bootStrapper,
            CollectionService collectionService,
            PlaceManager placeManager) {
        super(eventBus, view, proxy, ApplicationPresenter.TYPE_SetMainContent);

        this.bootStrapper = bootStrapper;
        this.collectionService = collectionService;
        this.placeManager = placeManager;

        getView().setUiHandlers(this);
    }

    @Override
    public void save(List<Answer> answers) {
        Questionnaire questionnaire = (Questionnaire) collection;
        AnswerSet answerSet = new AnswerSet();
        answerSet.setQuestionnaire(questionnaire.getId());
        answerSet.setLocale(questionnaire.getLocale());
        answerSet.setAnswers(answers);
        collectionService.saveAnswers(answerSet, new MethodCallbackLoader<Void>() {
            @Override
            public void handleSuccess(Void response) {
                PlaceRequest place = new PlaceRequest.Builder().nameToken(NameTokens.getEndSurvey()).build();
                placeManager.revealPlace(place);
            }
        });
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        collection = bootStrapper.getCollection();
        getView().setData(collection, false);

        if (!collection.isQuestionnaire()) {
            getView().hideSubmit();
        }
    }
}
