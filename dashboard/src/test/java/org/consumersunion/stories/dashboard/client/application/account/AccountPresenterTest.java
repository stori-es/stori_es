package org.consumersunion.stories.dashboard.client.application.account;

import java.util.ArrayList;

import javax.inject.Inject;

import org.consumersunion.stories.Random;
import org.consumersunion.stories.common.client.place.ClientPlaceManager;
import org.consumersunion.stories.common.client.service.RpcUserServiceAsync;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.ui.stories.ListStoriesPresenter;
import org.consumersunion.stories.dashboard.client.AsyncMockStubber;
import org.consumersunion.stories.dashboard.client.BaseTestModule;
import org.consumersunion.stories.dashboard.client.application.stories.widget.StoryCard;
import org.consumersunion.stories.dashboard.client.application.widget.StoriesMapPresenter;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(JukitoRunner.class)
public class AccountPresenterTest {
    public static class MyModule extends BaseTestModule {
        @Override
        protected void configureTest() {
            super.configureTest();
            bind(ListStoriesPresenter.class).annotatedWith(StoryCard.class)
                    .toInstance(mock(ListStoriesPresenter.class));
            forceMock(StoriesMapPresenter.class);
            forceMock(ClientPlaceManager.class);
        }
    }

    @Inject
    AccountPresenter presenter;
    @Inject
    RpcUserServiceAsync userService;
    @Inject
    AccountPresenter.MyView myView;

    private final String username = Random.string();
    private final String password = Random.string();

    @Test
    public void changeAccountPassword_enablesPasswordUpdateButton() {
        // Given
        AsyncMockStubber.callHandleSuccessWith(mock(ActionResponse.class)).when(userService)
                .updatePassword(Matchers.eq(username), Matchers.eq(password),
                        Matchers.<AsyncCallback<ActionResponse>>any());

        // When
        presenter.changeAccountPassword(username, password);

        // Then
        Mockito.verify(myView).setPasswordUpdateButtonEnabled(true);
    }

    @Test
    public void changeAccountPassword_willClearPassword_whenNoErrorMessages() {
        // Given
        ActionResponse response = mock(ActionResponse.class);
        given(response.getGlobalErrorMessages()).willReturn(new ArrayList<String>());
        AsyncMockStubber.callHandleSuccessWith(response).when(userService)
                .updatePassword(Matchers.eq(username), Matchers.eq(password),
                        Matchers.<AsyncCallback<ActionResponse>>any());

        // When
        presenter.changeAccountPassword(username, password);

        // Then
        Mockito.verify(myView).clearPassword();
    }

    @Test
    public void changeAccountPassword_willNotClearPassword_whenErrorMessages() {
        // Given
        ActionResponse response = mock(ActionResponse.class);
        given(response.getGlobalErrorMessages()).willReturn(Lists.newArrayList("error message"));
        AsyncMockStubber.callHandleSuccessWith(response).when(userService)
                .updatePassword(Matchers.eq(username), Matchers.eq(password),
                        Matchers.<AsyncCallback<ActionResponse>>any());

        // When
        presenter.changeAccountPassword(username, password);

        // Then
        Mockito.verify(myView, Mockito.never()).clearPassword();
    }
}
