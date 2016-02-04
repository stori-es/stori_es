package org.consumersunion.stories.dashboard.client.application.ui.block;

import org.consumersunion.stories.dashboard.client.application.ui.block.configurator.BlockConfiguratorModule;

import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class BlockModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new BlockConfiguratorModule());

        install(new GinFactoryModuleBuilder().build(QuestionnaireBuilderFactory.class));
    }
}
