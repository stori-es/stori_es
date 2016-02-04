package org.consumersunion.stories.server.api.rest;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.consumersunion.stories.common.shared.dto.tasks.Task;
import org.consumersunion.stories.server.business_logic.TaskService;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.core.JsonProcessingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class TaskResourceTest {
    private static final int TASK_ID = 40;

    @Inject
    private TaskResource taskResource;
    @Inject
    private TaskService taskService;

    @Test
    public void createTask_delegatesToService() throws JsonProcessingException {
        Task task = mock(Task.class);
        given(taskService.createTask(same(task))).willReturn(task);

        Response response = taskResource.createTask(task);

        assertThat(response.getEntity()).isSameAs(task);
    }

    @Test
    public void getTask_delegatesToService() throws JsonProcessingException {
        Task task = mock(Task.class);
        given(taskService.getTask(TASK_ID)).willReturn(task);

        Response response = taskResource.getTask(TASK_ID);

        assertThat(response.getEntity()).isSameAs(task);
    }

    @Test
    public void cancelTask_delegatesToService() throws JsonProcessingException {
        taskResource.cancelTask(TASK_ID);

        verify(taskService).cancelTask(TASK_ID);
    }
}
