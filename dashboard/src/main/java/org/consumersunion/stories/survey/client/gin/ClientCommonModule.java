package org.consumersunion.stories.survey.client.gin;

import org.consumersunion.stories.common.client.CommonModule;
import org.consumersunion.stories.common.client.place.ClientPlaceManager;
import org.consumersunion.stories.survey.client.application.ApplicationModule;
import org.consumersunion.stories.survey.client.common.MethodCallbackImpl;
import org.consumersunion.stories.survey.client.common.MethodCallbackLoader;
import org.consumersunion.stories.survey.client.place.NameTokens;
import org.consumersunion.stories.survey.client.resource.Resources;
import org.consumersunion.stories.survey.client.rest.CollectionService;
import org.consumersunion.stories.survey.client.rest.QuestionnaireService;
import org.consumersunion.stories.survey.client.rest.StoryService;
import org.consumersunion.stories.survey.client.util.CorsVariableHelper;
import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Dispatcher;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestServiceProxy;
import org.fusesource.restygwt.client.dispatcher.FilterawareDispatcher;
import org.fusesource.restygwt.client.dispatcher.FilterawareRetryingDispatcher;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Dictionary;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;

import de.devbliss.gwt.xdm.client.TransportLayer;
import de.devbliss.gwt.xdm.client.impl.CustomEasyXDMTransportLayer;

public class ClientCommonModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new DefaultModule.Builder().placeManager(ClientPlaceManager.class).build());
        install(new CommonModule());
        install(new ApplicationModule());

        requestStaticInjection(MethodCallbackImpl.class);
        requestStaticInjection(MethodCallbackLoader.class);

        bind(Resources.class).in(Singleton.class);
        bind(SurveyBootstrapper.class).in(Singleton.class);

        bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.survey);
        bindConstant().annotatedWith(Embedded.class).to("survey");
    }

    @Provides
    @Singleton
    @Inject
    private QuestionnaireService provideQuestionnaireService(@RestPath String restPath, Dispatcher dispatcher) {
        return initService(GWT.<QuestionnaireService>create(QuestionnaireService.class), restPath, dispatcher);
    }

    @Provides
    @Singleton
    @Inject
    private CollectionService provideCollectionService(@RestPath String restPath, Dispatcher dispatcher) {
        return initService(GWT.<CollectionService>create(CollectionService.class), restPath, dispatcher);
    }

    @Provides
    @Singleton
    @Inject
    private StoryService provideStoryService(@RestPath String restPath, Dispatcher dispatcher) {
        return initService(GWT.<StoryService>create(StoryService.class), restPath, dispatcher);
    }

    @Provides
    @Singleton
    @Inject
    private TransportLayer getTransportLayer() {
        return GWT.create(CustomEasyXDMTransportLayer.class);
    }

    @Provides
    @Singleton
    @Inject
    private Dispatcher getDispatcher() {
        FilterawareDispatcher dispatcher = GWT.create(FilterawareRetryingDispatcher.class);
        Defaults.setDispatcher(dispatcher);

        return dispatcher;
    }

    @Provides
    @RestPath
    @Singleton
    private String getRestPath(CorsVariableHelper corsVariableHelper) {
        String domain = null;
        String port = null;

        try {
            final Dictionary storiesConfig = Dictionary.getDictionary("storiesConfig");

            domain = storiesConfig.get("corsDomain");
            port = storiesConfig.get("corsPort");
        } catch (Exception e) {
        } // Problem with Dictionary? Fall back to dev defaults.
        finally {
            if (domain == null) {
                domain = "stori.es";
            }
            if (port == null) { // Note: for no port, you must set the environment variable to "".
                port = "";
            }
        }

        corsVariableHelper.registerCorsDomain(domain);
        corsVariableHelper.registerCorsPort(port);

        String urlPort = Strings.isNullOrEmpty(port) ? "" : ":" + port;

        return "//" + domain + urlPort + "/rest";
    }

    private <T> T initService(T service, String restPath, Dispatcher dispatcher) {
        Resource resource = new Resource(restPath);
        ((RestServiceProxy) service).setResource(resource);
        ((RestServiceProxy) service).setDispatcher(dispatcher);

        return service;
    }
}
