package org.consumersunion.stories.dashboard.client.application.widget.content;

import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ContentModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bind(AddDocumentPresenter.MyView.class).to(AddDocumentView.class);
        bind(DocumentTextCardPresenter.MyView.class).to(DocumentTextCardView.class);
        bind(ResponseCardPresenter.MyView.class).to(ResponseCardView.class);

        bindPresenterWidget(ResponseBuilderPresenter.class, ResponseBuilderPresenter.MyView.class,
                ResponseBuilderView.class);
        bindPresenterWidget(ContentDetailPresenter.class, ContentDetailPresenter.MyView.class, ContentDetailView.class);

        install(new GinFactoryModuleBuilder().build(DocumentCardPresenterFactory.class));
    }
}
