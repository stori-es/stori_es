package org.consumersunion.stories.dashboard.client.application.ui;

import org.consumersunion.stories.dashboard.client.application.ui.addto.AddToWidgetFactory;
import org.consumersunion.stories.dashboard.client.application.ui.block.BlockModule;
import org.consumersunion.stories.dashboard.client.application.ui.builder.MetaBlockFactory;
import org.consumersunion.stories.dashboard.client.application.ui.token.TokenCellFactory;

import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class UiModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new BlockModule());

        requestStaticInjection(ConfirmationModal.class);
        requestStaticInjection(SelectionDropdownBox.class);
        requestStaticInjection(OptionsPopup.class);
        requestStaticInjection(ListItem.class);

        install(new GinFactoryModuleBuilder().build(TokenCellFactory.class));
        install(new GinFactoryModuleBuilder().build(AddToWidgetFactory.class));
        install(new GinFactoryModuleBuilder().build(MetaBlockFactory.class));
    }
}
