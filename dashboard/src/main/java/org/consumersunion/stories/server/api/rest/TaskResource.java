package org.consumersunion.stories.server.api.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.consumersunion.stories.common.shared.api.EndPoints;
import org.consumersunion.stories.common.shared.api.UrlParameters;
import org.consumersunion.stories.common.shared.dto.tasks.Task;
import org.consumersunion.stories.server.business_logic.TaskService;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
@Path(EndPoints.API + EndPoints.TASKS)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource {
    @Inject
    private TaskService taskService;

    @POST
    public Response createTask(Task task) throws JsonProcessingException {
        Task createdTask = taskService.createTask(task);

        return Response.ok(createdTask).build();
    }

    @GET
    @Path(EndPoints.ID)
    public Response getTask(@PathParam(UrlParameters.ID) int id) {
        Task task = taskService.getTask(id);

        return Response.ok(task).build();
    }

    @POST
    @Path(EndPoints.ID + EndPoints.CANCEL)
    public Response cancelTask(@PathParam(UrlParameters.ID) int id) {
        taskService.cancelTask(id);

        return Response.ok().build();
    }
}
