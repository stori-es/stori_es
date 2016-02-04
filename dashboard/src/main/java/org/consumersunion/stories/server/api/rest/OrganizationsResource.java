package org.consumersunion.stories.server.api.rest;

import java.util.ArrayList;
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
import org.consumersunion.stories.common.shared.dto.ApiContact;
import org.consumersunion.stories.common.shared.dto.OrganizationResponse;
import org.consumersunion.stories.common.shared.dto.OrganizationsApiResponse;
import org.consumersunion.stories.common.shared.dto.post.OrganizationPost;
import org.consumersunion.stories.common.shared.dto.post.OrganizationPut;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.server.api.rest.converters.ApiAddressConverter;
import org.consumersunion.stories.server.api.rest.converters.ApiContactConverter;
import org.consumersunion.stories.server.api.rest.converters.OrganizationConverter;
import org.consumersunion.stories.server.api.rest.converters.OrganizationPostConverter;
import org.consumersunion.stories.server.api.rest.merger.OrganizationPutMerger;
import org.consumersunion.stories.server.business_logic.OrganizationService;
import org.consumersunion.stories.server.business_logic.UserService;
import org.consumersunion.stories.server.exception.NotFoundException;
import org.jboss.resteasy.plugins.validation.hibernate.ValidateRequest;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
@Path(EndPoints.ORGANIZATIONS)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ValidateRequest
public class OrganizationsResource {
    private final OrganizationService organizationService;
    private final UserService userService;
    private final OrganizationConverter organizationConverter;
    private final OrganizationPostConverter organizationPostConverter;
    private final OrganizationPutMerger organizationPutMerger;
    private final ApiContactConverter apiContactConverter;
    private final ApiAddressConverter apiAddressConverter;

    @Inject
    OrganizationsResource(
            OrganizationService organizationService,
            UserService userService,
            OrganizationConverter organizationConverter,
            OrganizationPostConverter organizationPostConverter,
            OrganizationPutMerger organizationPutMerger,
            ApiContactConverter apiContactConverter,
            ApiAddressConverter apiAddressConverter) {
        this.organizationService = organizationService;
        this.userService = userService;
        this.organizationConverter = organizationConverter;
        this.organizationPostConverter = organizationPostConverter;
        this.organizationPutMerger = organizationPutMerger;
        this.apiContactConverter = apiContactConverter;
        this.apiAddressConverter = apiAddressConverter;
    }

    @GET
    @Path(EndPoints.ID)
    public Response getOrganization(@PathParam(UrlParameters.ID) int id) {
        Organization organization = fetchOrganization(id);

        OrganizationsApiResponse response = new OrganizationsApiResponse();
        response.setData(toResponses(organization));

        return Response.ok(response).build();
    }

    @PUT
    @Path(EndPoints.ID)
    public Response saveOrganization(
            @PathParam(UrlParameters.ID) int id,
            @Valid OrganizationPut organizationPut) {
        Organization organization = fetchOrganization(id);
        organizationPutMerger.merge(organization, organizationPut);

        List<Contact> contacts = convertContacts(organizationPut);

        Organization updated = organizationService.update(organization, contacts, null, null);

        OrganizationsApiResponse response = new OrganizationsApiResponse();
        response.setData(toResponses(updated));

        return Response.ok(response).build();
    }

    @DELETE
    @Path(EndPoints.ID)
    public Response deleteOrganization(@PathParam(UrlParameters.ID) int id) {
        organizationService.delete(id);

        return Response.noContent().build();
    }

    @POST
    public Response createOrganization(OrganizationPost organizationPost) {
        Organization organization = organizationPostConverter.convert(organizationPost);
        List<Contact> contacts = apiContactConverter.convertAll(organizationPost.getContacts()).asList();
        List<Address> addresses = apiAddressConverter.convertAll(organizationPost.getContacts()).asList();
        List<Integer> themeIds = organizationPost.getThemeIds();

        organization.setOwner(userService.getLoggedInUser(true).getId());

        Organization createdOrganization = organizationService
                .create(organization, contacts, addresses, themeIds, new ArrayList<String>());
        OrganizationsApiResponse response = new OrganizationsApiResponse();
        response.setData(toResponses(createdOrganization));

        return Response.ok(response).build();
    }

    private Organization fetchOrganization(int id) {
        try {
            return organizationService.get(id);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    private List<OrganizationResponse> toResponses(Organization... organizations) {
        return toResponses(Arrays.asList(organizations));
    }

    private List<OrganizationResponse> toResponses(List<Organization> organizations) {
        Iterable<OrganizationResponse> responses = organizationConverter.convertAll(organizations);

        return Lists.newArrayList(responses);
    }

    private List<Contact> convertContacts(OrganizationPut organizationPut) {
        List<ApiContact> contacts = organizationPut.getContacts();

        if (contacts != null) {
            return apiContactConverter.convertAll(contacts).asList();
        }

        return null;
    }
}
