package org.consumersunion.stories.dashboard.client.application.widget;

import javax.inject.Singleton;

import org.consumersunion.stories.dashboard.client.application.widget.addto.AddToModule;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardToolbarButtonFactory;
import org.consumersunion.stories.dashboard.client.application.widget.card.CardToolbarButtonFactoryImpl;
import org.consumersunion.stories.dashboard.client.application.widget.content.ContentModule;
import org.consumersunion.stories.dashboard.client.application.widget.search.SearchModule;

import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class WidgetModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new AddToModule());
        install(new ContentModule());
        install(new SearchModule());

        bindSingletonPresenterWidget(StoriesGrowthPresenter.class, StoriesGrowthPresenter.MyView.class,
                StoriesGrowthView.class);

        bindSingletonPresenterWidget(HeaderPresenter.class, HeaderPresenter.MyView.class, HeaderView.class);

        bindPresenterWidget(StoriesMapPresenter.class, StoriesMapPresenter.MyView.class, StoriesMapView.class);
        bindPresenterWidget(DocumentsListPresenter.class, DocumentsListPresenter.MyView.class, DocumentsListView.class);

        bind(CardToolbarButtonFactory.class).to(CardToolbarButtonFactoryImpl.class).in(Singleton.class);

        bind(TagsPresenter.MyView.class).to(TagsView.class);
        install(new GinFactoryModuleBuilder().build(TagWidgetFactory.class));
        install(new GinFactoryModuleBuilder().build(TagsPresenterFactory.class));
    }
}
