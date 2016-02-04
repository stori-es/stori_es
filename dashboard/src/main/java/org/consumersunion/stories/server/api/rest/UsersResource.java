package org.consumersunion.stories.server.api.rest;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.consumersunion.stories.common.shared.api.EndPoints;
import org.consumersunion.stories.common.shared.api.UrlParameters;
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
import org.jboss.resteasy.plugins.validation.hibernate.ValidateRequest;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import static javax.ws.rs.core.Response.Status.CREATED;

@Component
@Path(EndPoints.USERS)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ValidateRequest
public class UsersResource {
    private final UserConverter userConverter;
    private final UserPostConverter userPostConverter;
    private final UserPutMerger userPutMerger;
    private final UserService userService;

    @Inject
    UsersResource(
            UserConverter userConverter,
            UserPostConverter userPostConverter,
            UserPutMerger userPutMerger,
            UserService userService) {
        this.userConverter = userConverter;
        this.userPostConverter = userPostConverter;
        this.userPutMerger = userPutMerger;
        this.userService = userService;
    }

    @GET
    @Path(EndPoints.SELF)
    public Response getSelfUser() {
        User user = userService.getLoggedInUser(true);

        UsersApiResponse response = createUsersApiResponse(user);

        return Response.ok(response).build();
    }

    @GET
    @Path(EndPoints.ID)
    public Response getUser(@PathParam(UrlParameters.ID) int id) {
        User user = userService.getUser(id);

        UsersApiResponse response = createUsersApiResponse(user);

        return Response.ok(response).build();
    }

    @POST
    public Response createUser(@Valid UserPost userPost) {
        User user = userPostConverter.convert(userPost);

        CredentialedUser credentialedUser = new CredentialedUser();
        credentialedUser.setUser(user);

        credentialedUser = userService.createUser(credentialedUser);

        UsersApiResponse usersApiResponse = new UsersApiResponse();
        usersApiResponse.setData(toResponses(credentialedUser.getUser()));

        return Response.status(CREATED).entity(usersApiResponse).build();
    }

    @PUT
    @Path(EndPoints.ID)
    public Response updateUser(@PathParam(UrlParameters.ID) Integer id, @Valid UserPut userPut) {
        CredentialedUser credentialedUser = userService.getCredentialedUser(id);
        userPutMerger.merge(credentialedUser.getUser(), userPut);

        credentialedUser = userService.updateUser(credentialedUser);

        UsersApiResponse usersApiResponse = new UsersApiResponse();
        usersApiResponse.setData(toResponses(credentialedUser.getUser()));

        return Response.ok(usersApiResponse).build();
    }

    @DELETE
    @Path(EndPoints.ID)
    public Response deleteUser(@PathParam(UrlParameters.ID) int id) {
        userService.deleteUser(id);

        return Response.noContent().build();
    }

    private UsersApiResponse createUsersApiResponse(User... users) {
        UsersApiResponse response = new UsersApiResponse();
        response.setData(toResponses(users));

        return response;
    }

    private List<UserResponse> toResponses(User... users) {
        return toResponses(Arrays.asList(users));
    }

    private List<UserResponse> toResponses(List<User> users) {
        Iterable<UserResponse> responses = userConverter.convertAll(users);

        return Lists.newArrayList(responses);
    }
}
