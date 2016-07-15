package org.consumersunion.stories.questionnaire.client.application.questionnaire;

import java.util.List;

import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.client.service.RpcQuestionnaireServiceAsync;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.QuestionnaireSurveyResponse;
import org.consumersunion.stories.common.client.util.PublicResponseHandler;
import org.consumersunion.stories.common.client.util.URLUtils;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire;
import org.consumersunion.stories.questionnaire.client.application.ApplicationPresenter;
import org.consumersunion.stories.questionnaire.client.place.NameTokens;

import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Window;
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

public class QuestionnairePresenter extends Presenter<QuestionnairePresenter.MyView, QuestionnairePresenter.MyProxy>
        implements QuestionnaireUiHandlers {
    interface MyView extends View, HasUiHandlers<QuestionnaireUiHandlers> {
        void setData(Collection collection);

        void setSubmitEnabled(boolean enabled);

        void hideSubmit();
    }

    @ProxyStandard
    @NameToken(NameTokens.questionnaire)
    interface MyProxy extends ProxyPlace<QuestionnairePresenter> {
    }

    private final PlaceManager placeManager;
    private final CollectionHolder collectionHolder;
    private final RpcQuestionnaireServiceAsync questionnaireService;
    private final RpcCollectionServiceAsync collectionService;

    @Inject
    QuestionnairePresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            PlaceManager placeManager,
            CollectionHolder collectionHolder,
            RpcQuestionnaireServiceAsync questionnaireService,
            RpcCollectionServiceAsync collectionService) {
        super(eventBus, view, proxy, ApplicationPresenter.TYPE_SetMainContent);

        this.placeManager = placeManager;
        this.collectionHolder = collectionHolder;
        this.questionnaireService = questionnaireService;
        this.collectionService = collectionService;

        getView().setUiHandlers(this);
    }

    @Override
    public void save(List<Answer> answers) {
        Questionnaire currentQuestionnaire = (Questionnaire) collectionHolder.getCollection();

        AnswerSet answerSet = new AnswerSet();
        answerSet.setQuestionnaire(currentQuestionnaire.getId());
        answerSet.setLocale(currentQuestionnaire.getLocale());
        answerSet.setAnswers(answers);

        collectionService.saveAnswersAndStory(answerSet,
                new PublicResponseHandler<QuestionnaireSurveyResponse>() {
                    @Override
                    public void onFailure(Throwable e) {
                        super.onFailure(e);

                        getView().setSubmitEnabled(true);
                    }

                    @Override
                    public void handleSuccess(QuestionnaireSurveyResponse result) {
                        goToNextDocument();
                    }
                }
        );
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        Dictionary params = Dictionary.getDictionary("requestParameters");
        String permalink = params.get("permalink");
        Boolean isQuestionnaire = Boolean.valueOf(params.get("isQuestionnaire"));

        if (isQuestionnaire) {
            questionnaireService.getQuestionnaireSurvey(permalink,
                    new PublicResponseHandler<QuestionnaireSurveyResponse>() {
                        @Override
                        public void handleSuccess(QuestionnaireSurveyResponse result) {
                            setData(result.getQuestionnaire());
                        }
                    }
            );
        } else {
            collectionService.getCollectionByPermalink(permalink,
                    new PublicResponseHandler<DatumResponse<Collection>>() {
                        @Override
                        public void handleSuccess(DatumResponse<Collection> result) {
                            setData(result.getDatum());
                            getView().hideSubmit();
                        }
                    }
            );
        }
    }

    private void goToNextDocument() {
        Questionnaire questionnaire = (Questionnaire) collectionHolder.getCollection();
        Document confirmationDocument = questionnaire.getNextDocument();

        if (SystemEntityRelation.ATTACHMENT.equals(confirmationDocument.getSystemEntityRelation())) {
            Window.Location.assign(URLUtils.appendDefaultProtocol(confirmationDocument.getPermalink()));
        } else {
            PlaceRequest place = new PlaceRequest.Builder().nameToken(
                    NameTokens.getEndquestionnaire()).build();
            placeManager.revealPlace(place);
        }
    }

    private void setData(Collection collection) {
        collectionHolder.setCollection(collection);
        getView().setData(collection);
    }
}
