package org.consumersunion.stories.dashboard.client.application.collections.widget;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.client.service.RpcQuestionnaireServiceAsync;
import org.consumersunion.stories.common.client.service.response.CollectionSurveyI15dResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.widget.ContentKind;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Locale;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.dashboard.client.AsyncMockStubber;
import org.consumersunion.stories.dashboard.client.BaseTestModule;
import org.consumersunion.stories.dashboard.client.PlaceRequestMatcher;
import org.consumersunion.stories.dashboard.client.TestBase;
import org.consumersunion.stories.dashboard.client.application.widget.content.AddDocumentPresenter;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JukitoRunner.class)
public class NewCollectionPresenterTest extends TestBase {
    public static class Module extends BaseTestModule {
        @Override
        protected void configureTest() {
            super.configureTest();

            install(new FactoryModuleBuilder().build(NewCollectionPresenterFactory.class));
        }
    }

    private static final String SOME_TITLE = "some title";

    @Inject
    NewCollectionPresenterFactory factory;

    @Test
    public void createQuestionnaire_hideAndRedirects(
            PlaceManager placeManager,
            RpcQuestionnaireServiceAsync questionnaireServiceAsync,
            AddDocumentPresenter.MyView view) {
        // given
        NewCollectionPresenter newCollectionPresenter = factory.create(ContentKind.QUESTIONNAIRE);

        CollectionSurveyI15dResponse response = mock(CollectionSurveyI15dResponse.class, RETURNS_DEEP_STUBS);
        when(response.isLoggedIn()).thenReturn(true);
        AsyncMockStubber.callSuccessWith(response)
                .when(questionnaireServiceAsync)
                .saveQuestionnaire(any(QuestionnaireI15d.class), (AsyncCallback<CollectionSurveyI15dResponse>) any());

        // when
        newCollectionPresenter.create(SOME_TITLE, "", Locale.ENGLISH);

        // then
        verify(view).init(ContentKind.QUESTIONNAIRE);
        verify(placeManager).revealPlace(argThat(new PlaceRequestMatcher(NameTokens.collection)));
    }

    @Test
    public void createCollection_hideAndRedirects(
            PlaceManager placeManager,
            RpcCollectionServiceAsync collectionServiceAsync,
            AddDocumentPresenter.MyView view) {
        // given
        NewCollectionPresenter newCollectionPresenter = factory.create(ContentKind.COLLECTION);

        DatumResponse<Collection> response = mock(DatumResponse.class);
        Collection collection = mock(Collection.class);
        when(response.getDatum()).thenReturn(collection);
        when(response.isLoggedIn()).thenReturn(true);

        AsyncMockStubber.callSuccessWith(response)
                .when(collectionServiceAsync)
                .createCollection(any(Collection.class), (AsyncCallback) any());

        // when
        newCollectionPresenter.create(SOME_TITLE, "", Locale.ENGLISH);

        // then
        verify(view).init(ContentKind.COLLECTION);
        verify(placeManager).revealPlace(argThat(new PlaceRequestMatcher(NameTokens.collection)));
    }
}
