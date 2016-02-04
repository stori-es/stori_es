package org.consumersunion.stories.server.business_logic;

import org.consumersunion.stories.common.shared.dto.tasks.Task;
import org.consumersunion.stories.server.business_logic.interceptor.RequiresLoggedUser;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface TaskService {
    @RequiresLoggedUser
    Task getTask(int id);

    @RequiresLoggedUser
    void cancelTask(int id);

    @RequiresLoggedUser
    Task createTask(Task task) throws JsonProcessingException;
}
