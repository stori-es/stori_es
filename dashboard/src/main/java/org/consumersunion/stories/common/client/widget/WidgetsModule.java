package org.consumersunion.stories.common.client.widget;

import org.consumersunion.stories.common.client.widget.messages.MessageFactory;
import org.consumersunion.stories.common.client.widget.messages.MessagesContainer;

import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class WidgetsModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new GinFactoryModuleBuilder().build(MessageFactory.class));
        install(new GinFactoryModuleBuilder().build(CollectionListItemFactory.class));
        install(new GinFactoryModuleBuilder().build(QuestionnaireListItemFactory.class));

        bindSingletonPresenterWidget(MessagesPresenter.class, MessagesPresenter.MyView.class, MessagesView.class);
        bind(MessagesContainer.class).to(MessagesPresenter.MyView.class);
    }
}
