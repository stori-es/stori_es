package org.consumersunion.stories.dashboard.client.application.collection.widget.collectionstoken;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionSummary;

import com.google.inject.TypeLiteral;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class CollectionsTokenModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<CollectionsTokenView<CollectionSummary, Collection, CollectionData>>() {
        }).to(CollectionsTokenViewImpl.class);
        bind(new TypeLiteral<CollectionsTokenView<QuestionnaireI15d, QuestionnaireI15d, CollectionData>>() {
        }).to(QuestionnairesTokenViewImpl.class);
    }
}
