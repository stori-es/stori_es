package org.consumersunion.stories.common.client.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.consumersunion.stories.common.shared.api.EndPoints;
import org.consumersunion.stories.common.shared.api.UrlParameters;
import org.consumersunion.stories.common.shared.dto.ResetPasswordRequest;
import org.consumersunion.stories.common.shared.model.entity.Contact;

import com.gwtplatform.dispatch.rest.shared.RestAction;

@Path(EndPoints.ACCOUNT)
public interface AccountService {
    @GET
    @Path(EndPoints.RESET_PASSWORD)
    RestAction<Void> reset(@QueryParam(UrlParameters.ID) String usernameOrEmail);

    @POST
    @Path(EndPoints.RESET_PASSWORD)
    RestAction<Void> reset(ResetPasswordRequest resetPasswordRequest);

    @GET
    @Path(EndPoints.EMAILS)
    RestAction<List<Contact>> getEmails();

    @GET
    @Path(EndPoints.EMAIL)
    RestAction<String> getPrimaryEmail();

    @POST
    @Path(EndPoints.EMAIL)
    RestAction<Void> setPrimaryEmail(Contact contact);
}
