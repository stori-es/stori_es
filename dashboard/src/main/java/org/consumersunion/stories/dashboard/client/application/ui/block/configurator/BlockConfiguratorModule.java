package org.consumersunion.stories.dashboard.client.application.ui.block.configurator;

import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class BlockConfiguratorModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        requestStaticInjection(AbstractConfigurator.class);

        install(new GinFactoryModuleBuilder().build(BlockConfiguratorFactory.class));
        install(new GinFactoryModuleBuilder().build(OptionEditorFactory.class));
    }
}
