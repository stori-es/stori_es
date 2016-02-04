package org.consumersunion.stories.dashboard.client.application.questionnaire.ui;

import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class QuestionnaireUiModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new GinFactoryModuleBuilder().build(BlockBuildersPanelFactory.class));
        install(new GinFactoryModuleBuilder().build(QuestionnaireCellFactory.class));
    }
}
