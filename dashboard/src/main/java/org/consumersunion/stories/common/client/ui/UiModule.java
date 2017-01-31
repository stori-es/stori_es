package org.consumersunion.stories.common.client.ui;

import org.consumersunion.stories.common.client.ui.block.ElementFactory;
import org.consumersunion.stories.common.client.ui.block.content.CollectionContentPresenter;
import org.consumersunion.stories.common.client.ui.block.content.CollectionContentView;
import org.consumersunion.stories.common.client.ui.block.question.QuestionWidget;
import org.consumersunion.stories.common.client.ui.block.question.ValidatorsFactory;
import org.consumersunion.stories.common.client.ui.questionnaire.QuestionnaireModule;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.HasTitle;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;

import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class UiModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new QuestionnaireModule());

        bind(CollectionContentPresenter.MyView.class).to(CollectionContentView.class);

        install(new GinFactoryModuleBuilder().build(ElementFactory.class));
        install(new GinFactoryModuleBuilder()
                .implement(HasTitle.class, Collection.class)
                .build(new TypeLiteral<EntitySuggestFactory<Collection>>() {
                }));
        install(new GinFactoryModuleBuilder()
                .implement(HasTitle.class, StorySummary.class)
                .build(new TypeLiteral<EntitySuggestFactory<StorySummary>>() {
                }));

        requestStaticInjection(QuestionWidget.class);
    }

    @Provides
    EntitySuggest<Collection> getCollectionSuggest(
            EntitySuggestFactory<Collection> suggestFactory,
            CollectionSuggestionOracle collectionSuggestionOracle) {
        return suggestFactory.create(collectionSuggestionOracle);
    }
}
