package org.consumersunion.stories.common.client.api;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.consumersunion.stories.common.shared.api.EndPoints;
import org.consumersunion.stories.common.shared.api.UrlParameters;
import org.consumersunion.stories.common.shared.dto.tasks.Task;

import com.gwtplatform.dispatch.rest.shared.RestAction;

@Path(EndPoints.TASKS)
public interface TaskService {
    @POST
    RestAction<Task> createTask(Task task);

    @GET
    @Path(EndPoints.ID)
    RestAction<Task> getTask(@PathParam(UrlParameters.ID) int taskId);

    @POST
    @Path(EndPoints.ID + EndPoints.CANCEL)
    RestAction<Void> cancelTask(@PathParam(UrlParameters.ID) int taskId);
}
