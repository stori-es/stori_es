package org.consumersunion.stories.server.business_logic;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.dto.tasks.Task;
import org.consumersunion.stories.common.shared.dto.tasks.TaskStatus;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.server.annotations.Amazon;
import org.consumersunion.stories.server.exception.NotAuthorizedException;
import org.consumersunion.stories.server.persistence.TaskPersister;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TaskServiceImpl implements TaskService {
    private final UserService userService;
    private final TaskPersister taskPersister;
    private final AmazonSQS amazonSQSClient;
    private final ObjectMapper objectMapper;
    private final String amazonSqsWorkerQueue;

    @Inject
    TaskServiceImpl(
            UserService userService,
            TaskPersister taskPersister,
            AmazonSQS amazonSQSClient,
            ObjectMapper objectMapper,
            @Amazon String amazonSqsWorkerQueue) {
        this.userService = userService;
        this.taskPersister = taskPersister;
        this.amazonSQSClient = amazonSQSClient;
        this.objectMapper = objectMapper;
        this.amazonSqsWorkerQueue = amazonSqsWorkerQueue;
    }

    @Override
    public Task createTask(Task task) throws JsonProcessingException {
        task.setProfileId(userService.getActiveProfileId());

        validateUserForTask(task);

        Task savedTask = taskPersister.create(task);

        String message = objectMapper.writeValueAsString(task);
        amazonSQSClient.sendMessage(amazonSqsWorkerQueue, message);

        return savedTask;
    }

    @Override
    public Task getTask(int id) {
        Task task = taskPersister.get(id);

        validateUserForTask(task);

        return task;
    }

    @Override
    public void cancelTask(int id) {
        Task task = getTask(id);

        taskPersister.changeStatus(task, TaskStatus.CANCELING);
    }

    private void validateUserForTask(Task task) throws NotAuthorizedException {
        User user = userService.getLoggedInUser();
        User taskUser = userService.getUserForProfile(task.getProfileId());

        if (user.getId() != taskUser.getId()) {
            throw new NotAuthorizedException();
        }
    }
}
