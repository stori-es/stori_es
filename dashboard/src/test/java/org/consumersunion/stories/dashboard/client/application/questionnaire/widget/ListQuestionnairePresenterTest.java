package org.consumersunion.stories.dashboard.client.application.questionnaire.widget;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.event.CollectionChangedEvent;
import org.consumersunion.stories.common.client.event.RedrawEvent;
import org.consumersunion.stories.common.client.service.RpcCollectionServiceAsync;
import org.consumersunion.stories.common.client.service.RpcQuestionnaireServiceAsync;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.client.service.response.PagedDataResponse;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.common.shared.place.ParameterTokens;
import org.consumersunion.stories.common.shared.service.datatransferobject.CollectionData;
import org.consumersunion.stories.dashboard.client.AsyncMockStubber;
import org.consumersunion.stories.dashboard.client.BaseTestModule;
import org.consumersunion.stories.dashboard.client.application.collection.CollectionIdsUtil;
import org.consumersunion.stories.dashboard.client.event.ReloadCollectionsEvent;
import org.consumersunion.stories.dashboard.shared.place.NameTokens;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class ListQuestionnairePresenterTest {
    public static class Module extends BaseTestModule {
        @Override
        protected void configureTest() {
            super.configureTest();

            bindSpy(ListQuestionnairePresenter.class);

            forceMock(CollectionIdsUtil.class);
        }
    }

    private static final int COLLECTION_ID = 9;
    private static final int QUESTIONNAIRE_ID = 10;

    @Inject
    private ListQuestionnairePresenter presenter;
    @Inject
    private ListQuestionnairePresenter.MyView view;
    @Inject
    private RpcQuestionnaireServiceAsync questionnaireService;
    @Inject
    private RpcCollectionServiceAsync collectionService;
    @Inject
    private PlaceManager placeManager;
    @Inject
    private CollectionIdsUtil collectionIdsUtil;

    @Test
    public void initPresenter_withCollection() {
        presenter.initPresenter(new Collection());

        verify(view).init();
    }

    @Test
    public void initPresenter_withSourceQuestionnaires_setsData() {
        ArrayList<QuestionnaireI15d> questionnaires = Lists.newArrayList(new QuestionnaireI15d());

        presenter.initPresenter(new Collection(), questionnaires);

        verify(view).setData(same(questionnaires), eq(0), eq(1));
    }

    @Test
    public void onCollectionChanged_isRelatedCollection_refreshQuestionnaires() {
        CollectionChangedEvent event = mock(CollectionChangedEvent.class);
        Collection collection = new Collection();
        Collection newCollection = new Collection();
        given(event.getCollection()).willReturn(newCollection);
        given(collectionIdsUtil.isRelatedCollection(same(collection), same(newCollection))).willReturn(true);
        presenter.initPresenter(collection);

        presenter.onCollectionChanged(event);

        verify(view).refreshQuestionnaires();
    }

    @Test
    public void onCollectionChanged_isNotRelatedCollection_doesNothing() {
        CollectionChangedEvent event = mock(CollectionChangedEvent.class);
        Collection collection = new Collection();
        Collection newCollection = new Collection();
        given(event.getCollection()).willReturn(newCollection);
        given(collectionIdsUtil.isRelatedCollection(same(collection), same(newCollection))).willReturn(false);
        presenter.initPresenter(collection);

        presenter.onCollectionChanged(event);

        verify(view, never()).refreshQuestionnaires();
    }

    @Test
    public void loadQuestionnaire_setsDataIntoView_onSuccess() {
        int totalCount = 100;
        int length = 8;
        int start = 0;

        PagedDataResponse<QuestionnaireI15d> response = new PagedDataResponse<QuestionnaireI15d>();
        List<QuestionnaireI15d> questionnaires = new ArrayList<QuestionnaireI15d>();
        response.setData(questionnaires);
        response.setTotalCount(totalCount);
        AsyncMockStubber.callHandleSuccessWith(response)
                .when(questionnaireService)
                .getQuestionnaireSummaries(eq(COLLECTION_ID), eq(start), eq(length), any(AsyncCallback.class));
        presenter.initPresenter(new Collection(COLLECTION_ID, 1));

        presenter.loadQuestionnaire(start, length);

        verify(view).setData(same(questionnaires), eq(start), eq(totalCount));
        verify(presenter).fireEvent(isA(RedrawEvent.class));
    }

    @Test
    public void onReloadCollections_sameCollection_reloadsData() {
        ReloadCollectionsEvent event = mock(ReloadCollectionsEvent.class);
        Collection collection = new Collection(COLLECTION_ID, 1);
        given(event.getSourceCollection()).willReturn(collection);
        presenter.initPresenter(collection);

        DatumResponse<CollectionData> response = new DatumResponse<CollectionData>();
        Collection result = new Collection();
        response.setDatum(new CollectionData(result, new HashSet<String>()));
        AsyncMockStubber.callHandleSuccessWith(response)
                .when(collectionService)
                .getCollection(eq(COLLECTION_ID), eq(ROLE_READER), any(AsyncCallback.class));

        presenter.onReloadCollections(event);

        verify(collectionService).getCollection(eq(COLLECTION_ID), eq(ROLE_READER), any(AsyncCallback.class));
        verify(presenter).initPresenter(same(result));
    }

    @Test
    public void onReloadCollections_relatedCollection_reloadsData() {
        ReloadCollectionsEvent event = mock(ReloadCollectionsEvent.class);
        Collection collection = new Collection(COLLECTION_ID, 1);
        given(event.getRelatedCollectionIds()).willReturn(Lists.<Integer>newArrayList(COLLECTION_ID));
        presenter.initPresenter(collection);

        presenter.onReloadCollections(event);

        verify(collectionService).getCollection(eq(COLLECTION_ID), eq(ROLE_READER), any(AsyncCallback.class));
    }

    @Test
    public void questionnaireDetails_revealsDetailPlace() {
        presenter.questionnaireDetails(new QuestionnaireI15d(COLLECTION_ID, 1));

        PlaceRequest placeRequest = new PlaceRequest.Builder()
                .nameToken(NameTokens.collection)
                .with(ParameterTokens.id, String.valueOf(COLLECTION_ID))
                .build();
        verify(placeManager).revealPlace(placeRequest);
    }

    @Test
    public void testRemoveSourceQuestionnaire() {
        Collection collection = new Collection(COLLECTION_ID, 1);
        QuestionnaireI15d source = new QuestionnaireI15d(QUESTIONNAIRE_ID, 1);
        presenter.initPresenter(collection);

        presenter.removeSourceQuestionnaire(source);

        assertThat(collection.getCollectionSources()).doesNotContain(QUESTIONNAIRE_ID);
        verify(collectionService).updateCollection(same(collection), any(AsyncCallback.class));
    }
}
