package org.consumersunion.stories.server.api.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.consumersunion.stories.common.shared.api.EndPoints;
import org.consumersunion.stories.common.shared.api.UrlParameters;
import org.consumersunion.stories.common.shared.dto.ResetPasswordRequest;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.server.business_logic.AccountService;
import org.springframework.stereotype.Component;

@Component
@Path(EndPoints.API + EndPoints.ACCOUNT)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {
    private final AccountService accountService;

    @Inject
    AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @GET
    @Path(EndPoints.RESET_PASSWORD)
    public Response generateResetPassword(@QueryParam(UrlParameters.ID) String usernameOrEmail) {
        usernameOrEmail = usernameOrEmail.replaceAll("\"", "");
        accountService.generateResetPassword(usernameOrEmail);

        return Response.ok().build();
    }

    @POST
    @Path(EndPoints.RESET_PASSWORD)
    public Response resetPassword(ResetPasswordRequest resetPasswordRequest) {
        accountService.resetPassword(resetPasswordRequest);

        return Response.ok().build();
    }

    @GET
    @Path(EndPoints.EMAILS)
    public Response getEmails() {
        List<Contact> emails = accountService.getEmails();

        return Response.ok(emails).build();
    }

    @GET
    @Path(EndPoints.EMAIL)
    public Response getPrimaryEmail() {
        String primaryEmail = accountService.getPrimaryEmail();

        return Response.ok(primaryEmail).build();
    }

    @POST
    @Path(EndPoints.EMAIL)
    public Response setPrimaryEmail(Contact contact) {
        accountService.setPrimaryEmail(contact);

        return Response.ok().build();
    }
}
