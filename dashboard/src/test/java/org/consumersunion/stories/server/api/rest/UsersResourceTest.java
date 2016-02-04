package org.consumersunion.stories.server.api.rest;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.consumersunion.stories.common.shared.dto.UserResponse;
import org.consumersunion.stories.common.shared.dto.UsersApiResponse;
import org.consumersunion.stories.common.shared.dto.post.UserPost;
import org.consumersunion.stories.common.shared.dto.post.UserPut;
import org.consumersunion.stories.common.shared.model.CredentialedUser;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.server.api.rest.converters.UserConverter;
import org.consumersunion.stories.server.api.rest.converters.UserPostConverter;
import org.consumersunion.stories.server.api.rest.merger.UserPutMerger;
import org.consumersunion.stories.server.business_logic.UserService;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.google.common.collect.Lists;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class UsersResourceTest {
    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(UserConverter.class);
            forceMock(UserPostConverter.class);
            forceMock(UserPutMerger.class);
        }
    }

    private static final int USER_ID = 39;

    @Inject
    private UsersResource usersResource;
    @Inject
    private UserConverter userConverter;
    @Inject
    private UserPostConverter userPostConverter;
    @Inject
    private UserPutMerger userPutMerger;
    @Inject
    private UserService userService;

    @Test
    public void getSelfUser() throws Exception {
        User user = new User(USER_ID, 1);
        given(userService.getLoggedInUser(true)).willReturn(user);
        given(userConverter.convertAll((Iterable<? extends User>) argThat(hasItem(user))))
                .willReturn(Lists.newArrayList(UserResponse.builder().withId(USER_ID).build()));

        Response response = usersResource.getSelfUser();

        UsersApiResponse apiResponse = (UsersApiResponse) response.getEntity();
        assertThat(apiResponse.getData().get(0).getId()).isEqualTo(USER_ID);
    }

    @Test
    public void getUser() throws Exception {
        User user = new User(USER_ID, 1);
        given(userService.getUser(USER_ID)).willReturn(user);
        given(userConverter.convertAll((Iterable<? extends User>) argThat(hasItem(user))))
                .willReturn(Lists.newArrayList(UserResponse.builder().withId(USER_ID).build()));

        Response response = usersResource.getUser(USER_ID);

        UsersApiResponse apiResponse = (UsersApiResponse) response.getEntity();
        assertThat(apiResponse.getData().get(0).getId()).isEqualTo(USER_ID);
    }

    @Test
    public void createUser() throws Exception {
        UserPost userPost = new UserPost();
        User user = new User();
        CredentialedUser credentialedUser = new CredentialedUser();
        credentialedUser.setUser(new User());
        given(userPostConverter.convert(same(userPost))).willReturn(user);
        given(userService.createUser(argThat(new CredentialedUserArgumentMatcher(user)))).willReturn(credentialedUser);
        given(userConverter.convertAll((Iterable<? extends User>) argThat(hasItem(credentialedUser.getUser()))))
                .willReturn(Lists.newArrayList(UserResponse.builder().withId(USER_ID).build()));

        Response response = usersResource.createUser(userPost);

        ArgumentCaptor<CredentialedUser> credentialedUserCaptor = ArgumentCaptor.forClass(CredentialedUser.class);
        verify(userService).createUser(credentialedUserCaptor.capture());
        assertThat(credentialedUserCaptor.getValue().getUser()).isSameAs(user);
        UsersApiResponse apiResponse = (UsersApiResponse) response.getEntity();
        assertThat(apiResponse.getData().get(0).getId()).isEqualTo(USER_ID);
    }

    @Test
    public void updateUser() throws Exception {
        UserPut userPut = new UserPut();
        User user = new User();
        CredentialedUser credentialedUser = new CredentialedUser();
        credentialedUser.setUser(user);
        given(userService.getCredentialedUser(USER_ID)).willReturn(credentialedUser);
        CredentialedUser updatedUser = new CredentialedUser();
        updatedUser.setUser(new User());
        given(userService.updateUser(same(credentialedUser))).willReturn(updatedUser);
        given(userConverter.convertAll((Iterable<? extends User>) argThat(hasItem(updatedUser.getUser()))))
                .willReturn(Lists.newArrayList(UserResponse.builder().withId(USER_ID).build()));

        Response response = usersResource.updateUser(USER_ID, userPut);

        InOrder inOrder = Mockito.inOrder(userService, userPutMerger);
        inOrder.verify(userPutMerger).merge(same(user), same(userPut));
        inOrder.verify(userService).updateUser(same(credentialedUser));
        UsersApiResponse apiResponse = (UsersApiResponse) response.getEntity();
        assertThat(apiResponse.getData().get(0).getId()).isEqualTo(USER_ID);
    }

    @Test
    public void testDeleteUser() throws Exception {
        usersResource.deleteUser(USER_ID);

        verify(userService).deleteUser(USER_ID);
    }

    private static class CredentialedUserArgumentMatcher extends ArgumentMatcher<CredentialedUser> {
        private final User user;

        public CredentialedUserArgumentMatcher(User user) {
            this.user = user;
        }

        @Override
        public boolean matches(Object argument) {
            return user.equals(((CredentialedUser) argument).getUser());
        }
    }
}
