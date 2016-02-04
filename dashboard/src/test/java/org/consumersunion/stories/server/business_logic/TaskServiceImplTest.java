package org.consumersunion.stories.server.business_logic;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.dto.tasks.Task;
import org.consumersunion.stories.common.shared.dto.tasks.TaskStatus;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.server.annotations.Amazon;
import org.consumersunion.stories.server.exception.NotAuthorizedException;
import org.consumersunion.stories.server.persistence.TaskPersister;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class TaskServiceImplTest {
    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(TaskPersister.class);
            forceMock(ObjectMapper.class);
            bindConstant().annotatedWith(Amazon.class).to(AMAZON_SQS_QUEUE);
        }
    }

    private static final String AMAZON_SQS_QUEUE = "amazonsqs";
    private static final int USER_ID = 9;
    private static final int PROFILE_ID = 39;
    private static final int TASK_ID = 999;
    private static final int ANOTHER_USER_ID = 907;

    @Inject
    private TaskServiceImpl taskService;
    @Inject
    private UserService userService;
    @Inject
    private TaskPersister taskPersister;
    @Inject
    private ObjectMapper objectMapper;
    @Inject
    private AmazonSQS amazonSqs;

    @Test
    public void createTask() throws Exception {
        Task task = mock(Task.class);
        Task expected = mock(Task.class);
        given(task.getProfileId()).willReturn(PROFILE_ID);
        given(userService.getActiveProfileId()).willReturn(PROFILE_ID);
        given(userService.getLoggedInUser()).willReturn(new User(USER_ID, 1));
        given(userService.getUserForProfile(PROFILE_ID)).willReturn(new User(USER_ID, 1));
        given(taskPersister.create(same(task))).willReturn(expected);

        String message = "a_message";
        given(objectMapper.writeValueAsString(same(task))).willReturn(message);

        Task result = taskService.createTask(task);

        InOrder inOrder = Mockito.inOrder(task, amazonSqs);
        inOrder.verify(task).setProfileId(PROFILE_ID);
        inOrder.verify(amazonSqs).sendMessage(eq(AMAZON_SQS_QUEUE), eq(message));
        assertThat(result).isSameAs(expected);
    }

    @Test
    public void getTask() throws Exception {
        Task expected = mock(Task.class);
        given(expected.getProfileId()).willReturn(PROFILE_ID);
        given(userService.getActiveProfileId()).willReturn(PROFILE_ID);
        given(userService.getLoggedInUser()).willReturn(new User(USER_ID, 1));
        given(userService.getUserForProfile(PROFILE_ID)).willReturn(new User(USER_ID, 1));
        given(taskPersister.get(TASK_ID)).willReturn(expected);

        Task result = taskService.getTask(TASK_ID);

        assertThat(result).isSameAs(expected);
    }

    @Test(expected = NotAuthorizedException.class)
    public void getTask_throwsWhenNotSameUserId() throws Exception {
        Task expected = mock(Task.class);
        given(expected.getProfileId()).willReturn(PROFILE_ID);
        given(userService.getActiveProfileId()).willReturn(PROFILE_ID);
        given(userService.getLoggedInUser()).willReturn(new User(ANOTHER_USER_ID, 1));
        given(userService.getUserForProfile(PROFILE_ID)).willReturn(new User(USER_ID, 1));
        given(taskPersister.get(TASK_ID)).willReturn(expected);

        Task result = taskService.getTask(TASK_ID);
    }

    @Test
    public void cancelTask() throws Exception {
        Task task = mock(Task.class);
        given(task.getProfileId()).willReturn(PROFILE_ID);
        given(userService.getActiveProfileId()).willReturn(PROFILE_ID);
        given(userService.getLoggedInUser()).willReturn(new User(USER_ID, 1));
        given(userService.getUserForProfile(PROFILE_ID)).willReturn(new User(USER_ID, 1));
        given(taskPersister.get(TASK_ID)).willReturn(task);

        taskService.cancelTask(TASK_ID);

        verify(taskPersister).changeStatus(same(task), eq(TaskStatus.CANCELING));
    }
}
