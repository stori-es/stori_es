package org.consumersunion.stories.server.api.rest;

import java.util.List;

import javax.inject.Inject;
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

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.api.EndPoints;
import org.consumersunion.stories.common.shared.api.UrlParameters;
import org.consumersunion.stories.common.shared.dto.ProfilesApiResponse;
import org.consumersunion.stories.common.shared.dto.post.ProfilePost;
import org.consumersunion.stories.common.shared.dto.post.ProfilePut;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.server.api.rest.converters.ProfileConverter;
import org.consumersunion.stories.server.api.rest.converters.ProfilePostConverter;
import org.consumersunion.stories.server.api.rest.merger.ProfilePutMerger;
import org.consumersunion.stories.server.business_logic.ContactService;
import org.consumersunion.stories.server.business_logic.ProfileService;
import org.springframework.stereotype.Component;

@Component
@Path(EndPoints.PROFILES)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProfilesResource {
    private final ProfileService profileService;
    private final ProfileConverter profileConverter;
    private final ProfilePostConverter profilePostConverter;
    private final ContactService contactService;
    private final ProfilePutMerger putMerger;

    @Inject
    ProfilesResource(
            ProfileService profileService,
            ProfileConverter profileConverter,
            ProfilePostConverter profilePostConverter,
            ContactService contactService,
            ProfilePutMerger putMerger) {
        this.profileService = profileService;
        this.profileConverter = profileConverter;
        this.profilePostConverter = profilePostConverter;
        this.contactService = contactService;
        this.putMerger = putMerger;
    }

    @POST
    public Response postProfile(ProfilePost profilePost) {
        ProfileSummary summaryModel = profilePostConverter.convert(profilePost);
        // 'createProfile()' returns a 'ProfileSummary' as a workaround / convenience for some callers, but it only
        // ever actually create Profile data, the extra summary data is blank at this point.
        Profile profile = profileService.createProfile(summaryModel.getProfile()).getProfile();
        contactService.saveContacts(profile.getId(), summaryModel.getContacts());

        ProfilesApiResponse response = new ProfilesApiResponse();
        response.setData(profileConverter.convertAll(profile).asList());

        return Response.ok(response).build();
    }

    @PUT
    @Path(EndPoints.ID)
    public Response putProfile(@PathParam(UrlParameters.ID) int id, ProfilePut profilePut) {
        ProfileSummary profileSummary = profileService.get(id);

        putMerger.merge(profileSummary, profilePut);

        Profile profile = profileSummary.getProfile();
        Profile updated = profileService.updateProfile(profile);

        List<Contact> contacts = profileSummary.getContacts();
        if (contacts != null && !contacts.isEmpty()) {
            contactService.saveContacts(profile.getId(), contacts);
        }

        ProfilesApiResponse response = new ProfilesApiResponse();
        response.setData(profileConverter.convertAll(updated).asList());

        return Response.ok(response).build();
    }

    @GET
    @Path(EndPoints.ID)
    public Response getProfile(@PathParam(UrlParameters.ID) int id) {
        Profile profile = profileService.getProfile(id);

        ProfilesApiResponse response = new ProfilesApiResponse();
        response.setData(profileConverter.convertAll(profile).asList());

        return Response.ok(response).build();
    }

    @DELETE
    @Path(EndPoints.ID)
    public Response deleteProfile(@PathParam(UrlParameters.ID) int id) {
        profileService.delete(id);

        return Response.noContent().build();
    }
}
