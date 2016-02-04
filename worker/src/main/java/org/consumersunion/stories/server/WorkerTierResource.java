package org.consumersunion.stories.server;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.consumersunion.stories.common.shared.dto.tasks.Task;
import org.consumersunion.stories.server.handlers.HandlerChain;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class WorkerTierResource {
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final HandlerChain<Task> taskHandlerChain;

    @Inject
    WorkerTierResource(
            ThreadPoolTaskExecutor threadPoolTaskExecutor,
            HandlerChain<Task> taskHandlerChain) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.taskHandlerChain = taskHandlerChain;
    }

    @GET
    public Response get() {
        return Response.ok().build();
    }

    @POST
    public Response onMessage(final Task task) throws Exception {
        // Task is handled is a background thread, because SQS expects an answer within a certain amount of time
        // Otherwise, the message will be put back in the queue
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    taskHandlerChain.process(task);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return Response.ok().build();
    }
}
