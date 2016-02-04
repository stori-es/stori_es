package org.consumersunion.stories.server.handlers;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.dto.tasks.Task;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.server.business_logic.UserService;
import org.springframework.stereotype.Component;

@Component
public class TaskHandlerChain implements HandlerChain<Task> {
    private final List<Handler<Task>> handlers;
    private final UserService userService;

    @Inject
    TaskHandlerChain(
            List<Handler<Task>> handlers,
            UserService userService) {
        this.handlers = handlers;
        this.userService = userService;
    }

    @Override
    public void process(Task task) throws Exception {
        setCurrentUser(task);

        for (Handler<Task> handler : handlers) {
            if (handler.canHandle(task)) {
                handler.handle(task);
                return;
            }
        }
    }

    private void setCurrentUser(Task task) {
        int profileId = task.getProfileId();

        User user = userService.getUserForProfile(profileId);
        userService.setLoggedInUser(user);
        userService.setActiveProfileId(profileId);
    }
}
