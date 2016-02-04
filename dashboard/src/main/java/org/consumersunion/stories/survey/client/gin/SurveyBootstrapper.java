package org.consumersunion.stories.survey.client.gin;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Provider;

import org.consumersunion.stories.common.client.resource.CommonResources;
import org.consumersunion.stories.common.client.service.response.QuestionnaireSurveyResponse;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.survey.client.application.error.ErrorType;
import org.consumersunion.stories.survey.client.common.MethodCallbackImpl;
import org.consumersunion.stories.survey.client.place.NameTokens;
import org.consumersunion.stories.survey.client.resource.JsClientBundle;
import org.consumersunion.stories.survey.client.resource.Resources;
import org.consumersunion.stories.survey.client.rest.CollectionService;
import org.consumersunion.stories.survey.client.rest.QuestionnaireService;
import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Method;

import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LinkElement;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Bootstrapper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class SurveyBootstrapper implements Bootstrapper {
    private final static Logger logger = Logger.getLogger(SurveyBootstrapper.class.getName());

    private static final String SURVEY_ATTR = "class";
    private static final String SURVEY_PREFIX = "stories-";
    private static final String FONT_AWESOME_URL =
            "https://netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css";

    private final Provider<JsClientBundle> jsClientBundleProvider;
    private final PlaceManager placeManager;
    private final QuestionnaireService questionnaireService;
    private final CollectionService collectionService;
    private final String surveyEmbeddedDiv;

    private Collection currentEntity;

    @Inject
    SurveyBootstrapper(
            @Embedded String surveyEmbeddedDiv,
            Provider<JsClientBundle> jsClientBundleProvider,
            PlaceManager placeManager,
            QuestionnaireService questionnaireService,
            CollectionService collectionService,
            Resources resources,
            CommonResources commonResources) {
        this.surveyEmbeddedDiv = surveyEmbeddedDiv;
        this.jsClientBundleProvider = jsClientBundleProvider;
        this.placeManager = placeManager;
        this.questionnaireService = questionnaireService;
        this.collectionService = collectionService;

        Defaults.setDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.SSSZ");

        injectFontAwesome();
        resources.generalStyleCss().ensureInjected();
        commonResources.generalStyleCss().ensureInjected();
    }

    @Override
    public void onBootstrap() {
        Element surveyWrapper = Document.get().getElementById(surveyEmbeddedDiv);
        if (surveyWrapper != null) {
            final String surveyId = loadSurveyId(surveyWrapper);

            if (Strings.isNullOrEmpty(surveyId)) {
                logger.log(Level.INFO, "Survey ID attribute not found.");
                revealErrorPlace(ErrorType.NO_SURVEY_ATTR);
            } else {
                if (requiresXdm()) {
                    injectEasyXdmScript();
                }

                int idStart = surveyId.lastIndexOf('/') + 1;
                String entitySlug = surveyId.substring(idStart);
                String tmpKind = surveyId.replace("/" + entitySlug, "");

                idStart = tmpKind.lastIndexOf('/') + 1;
                String entityKind = tmpKind.substring(idStart);

                if (isQuestionnaire(entityKind)) {
                    questionnaireService.getCollectionSurvey(entitySlug,
                            new MethodCallbackImpl<QuestionnaireSurveyResponse>() {
                                @Override
                                public void handleSuccess(QuestionnaireSurveyResponse response) {
                                    onQuestionnaireLoaded(response);
                                }

                                @Override
                                public void onSuccess(Method method, QuestionnaireSurveyResponse response) {
                                    handleSuccess(response);

                                    handleAnalytics(method.getResponse().getHeader("ga-key"));
                                }

                                @Override
                                public void onFailure(Method method, Throwable throwable) {
                                    super.onFailure(method, throwable);

                                    if (method.getResponse().getStatusCode() == 401) {
                                        revealErrorPlace(ErrorType.QUESTIONNAIRE_NOT_FOUND);
                                    }
                                }
                            }
                    );
                } else {
                    collectionService.getCollection(entitySlug, new MethodCallbackImpl<Collection>() {
                        @Override
                        public void handleSuccess(Collection result) {
                            onCollectionLoaded(result);
                        }

                        @Override
                        public void onSuccess(Method method, Collection response) {
                            handleSuccess(response);

                            handleAnalytics(method.getResponse().getHeader("ga-key"));
                        }

                        @Override
                        public void onFailure(Method method, Throwable throwable) {
                            super.onFailure(method, throwable);

                            if (method.getResponse().getStatusCode() == 401) {
                                revealErrorPlace(ErrorType.QUESTIONNAIRE_NOT_FOUND);
                            }
                        }
                    });
                }
            }
        }
    }

    public Collection getCollection() {
        return currentEntity;
    }

    private void handleAnalytics(String key) {
        if (!Strings.isNullOrEmpty(key)) {
            JsClientBundle jsClientBundle = jsClientBundleProvider.get();

            ScriptInjector.fromString(jsClientBundle.analytics().getText().replace("%API_KEY%", key))
                    .setWindow(ScriptInjector.TOP_WINDOW)
                    .inject();
        }
    }

    private boolean isQuestionnaire(String entityKind) {
        String lowerCaseEntity = entityKind.toLowerCase();
        return "questionnaires".equals(lowerCaseEntity);
    }

    private void injectEasyXdmScript() {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
            }

            @Override
            public void onSuccess() {
                JsClientBundle jsClientBundle = jsClientBundleProvider.get();

                ScriptInjector.fromString(jsClientBundle.easyXdm().getText())
                        .setWindow(ScriptInjector.TOP_WINDOW)
                        .inject();
            }
        });
    }

    private boolean requiresXdm() {
        String userAgent = Window.Navigator.getUserAgent();

        return userAgent.contains("MSIE 8") || userAgent.contains("MSIE 9") || userAgent.contains("MSIE 10");
    }

    private void onQuestionnaireLoaded(QuestionnaireSurveyResponse response) {
        currentEntity = response.getQuestionnaire();
        if (currentEntity == null) {
            logger.info("Questionnaire not found or not published");
            revealErrorPlace(ErrorType.QUESTIONNAIRE_NOT_FOUND);
        } else {
            Questionnaire questionnaire = (Questionnaire) this.currentEntity;
            Iterables.removeIf(questionnaire.getSurvey().getBlocks(), Predicates.isNull());
            questionnaire.initBlocksKey();
            placeManager.revealDefaultPlace();
        }
    }

    private void onCollectionLoaded(Collection result) {
        currentEntity = result;
        if (currentEntity == null) {
            logger.info("Collection not found or not published");
            revealErrorPlace(ErrorType.COLLECTION_NOT_FOUND);
        } else {
            placeManager.revealDefaultPlace();
        }
    }

    private void revealErrorPlace(ErrorType errorType) {
        PlaceRequest place = new PlaceRequest.Builder()
                .nameToken(NameTokens.getError())
                .with(ParameterTokens.errortype, errorType.name())
                .build();
        placeManager.revealPlace(place);
    }

    private String loadSurveyId(Element surveyWrapper) {
        // IE8 has problems getting the 'class' attribute as a
        // regular attribute, we have to use the special purpose method.
        String surveyId = surveyWrapper.getClassName();
        if (Strings.isNullOrEmpty(surveyId)) {
            surveyId = Window.Location.getParameter(SURVEY_ATTR);
        }

        return surveyId.replaceFirst(SURVEY_PREFIX, "/").replaceFirst("-", "s/");
    }

    private void injectFontAwesome() {
        LinkElement link = Document.get().createLinkElement();
        link.setRel("stylesheet");
        link.setHref(FONT_AWESOME_URL);
        Document.get().getHead().appendChild(link);
    }
}
