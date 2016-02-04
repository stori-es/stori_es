package org.consumersunion.stories.dashboard.client.application.questionnaire.widget.tab;

import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.service.RpcEntityServiceAsync;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.dashboard.client.BaseTestModule;
import org.consumersunion.stories.dashboard.client.application.util.TagsService;
import org.consumersunion.stories.dashboard.client.application.util.TagsService.TagsCallback;
import org.consumersunion.stories.dashboard.client.event.DisplayEvent;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Sets;
import com.google.gwt.user.client.rpc.AsyncCallback;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class OnSubmitTabPresenterTest {
    public static class Module extends BaseTestModule {
        @Override
        protected void configureTest() {
            super.configureTest();

            forceMock(TagsService.class);
        }
    }

    @Inject
    private OnSubmitTabPresenter presenter;
    @Inject
    private TagsService tagsService;
    @Inject
    private RpcEntityServiceAsync entityService;

    @Test
    public void onDisplay_collection_doesNothing() {
        DisplayEvent event = mock(DisplayEvent.class);
        Collection collection = new Collection();
        given(event.get()).willReturn(collection);

        presenter.onDisplay(event);

        verify(tagsService, never()).loadTagsForSuggestionList(any(TagsCallback.class));
        verify(entityService, never()).getAutoTags(any(SystemEntity.class), any(AsyncCallback.class));
    }

    @Test
    public void onDisplay_questionnaire_loadsTags() {
        DisplayEvent event = mock(DisplayEvent.class);
        QuestionnaireI15d questionnaireI15d = mock(QuestionnaireI15d.class);
        Questionnaire questionnaire = new Questionnaire();
        given(questionnaireI15d.toQuestionnaire()).willReturn(questionnaire);
        given(event.get()).willReturn(questionnaireI15d);

        presenter.onDisplay(event);

        verify(tagsService).loadTagsForSuggestionList(any(TagsCallback.class));
        verify(entityService).getAutoTags(same(questionnaire), any(AsyncCallback.class));
    }

    @Test
    public void saveQuestionnaireTags_callsUpdateTags() throws Exception {
        DisplayEvent event = mock(DisplayEvent.class);
        QuestionnaireI15d questionnaireI15d = mock(QuestionnaireI15d.class);
        Questionnaire questionnaire = new Questionnaire();
        given(questionnaireI15d.toQuestionnaire()).willReturn(questionnaire);
        given(event.get()).willReturn(questionnaireI15d);
        presenter.onDisplay(event);
        Set<String> tags = Sets.newHashSet("tag1", "tag2");

        presenter.saveQuestionnaireTags(tags);

        verify(entityService).updateAutoTags(same(questionnaire), same(tags), any(AsyncCallback.class));
    }

    @Test
    public void onReveal_setsFocusIntoView(OnSubmitTabPresenter.MyView view) throws Exception {
        presenter.onReveal();

        verify(view).setFocus();
    }
}
