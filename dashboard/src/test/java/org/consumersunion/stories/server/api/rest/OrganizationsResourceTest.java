package org.consumersunion.stories.server.api.rest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.consumersunion.stories.common.shared.dto.OrganizationsApiResponse;
import org.consumersunion.stories.common.shared.dto.post.OrganizationPost;
import org.consumersunion.stories.common.shared.dto.post.OrganizationPut;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.server.api.rest.converters.AbstractConverter.PendingConverter;
import org.consumersunion.stories.server.api.rest.converters.ApiAddressConverter;
import org.consumersunion.stories.server.api.rest.converters.ApiContactConverter;
import org.consumersunion.stories.server.api.rest.converters.OrganizationConverter;
import org.consumersunion.stories.server.api.rest.converters.OrganizationPostConverter;
import org.consumersunion.stories.server.api.rest.merger.OrganizationPutMerger;
import org.consumersunion.stories.server.business_logic.OrganizationService;
import org.consumersunion.stories.server.business_logic.UserService;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.isNull;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class OrganizationsResourceTest {
    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(OrganizationPutMerger.class);
            forceMock(OrganizationPostConverter.class);
            forceMock(ApiContactConverter.class);
            forceMock(ApiAddressConverter.class);
        }
    }

    private static final int ORGANIZATION_ID = 400;

    @Inject
    private OrganizationsResource organizationsResource;
    @Inject
    private OrganizationService organizationService;
    @Inject
    private UserService userService;
    @Inject
    private OrganizationConverter organizationConverter;
    @Inject
    private OrganizationPostConverter organizationPostConverter;
    @Inject
    private OrganizationPutMerger organizationPutMerger;
    @Inject
    private ApiContactConverter apiContactConverter;
    @Inject
    private ApiAddressConverter apiAddressConverter;

    @Test
    public void getOrganization() throws Exception {
        Organization organization = new Organization(ORGANIZATION_ID, 1);
        given(organizationService.get(ORGANIZATION_ID)).willReturn(organization);

        Response response = organizationsResource.getOrganization(ORGANIZATION_ID);

        OrganizationsApiResponse apiResponse = (OrganizationsApiResponse) response.getEntity();
        assertThat(apiResponse.getData().get(0).getId()).isEqualTo(ORGANIZATION_ID);
    }

    @Test
    public void saveOrganization() throws Exception {
        OrganizationPut organizationPut = new OrganizationPut();
        Organization organization = new Organization(ORGANIZATION_ID, 1);
        given(organizationService.get(ORGANIZATION_ID)).willReturn(organization);

        organizationsResource.saveOrganization(ORGANIZATION_ID, organizationPut);

        InOrder inOrder = Mockito.inOrder(organizationPutMerger, organizationService);
        inOrder.verify(organizationPutMerger).merge(same(organization), same(organizationPut));
        inOrder.verify(organizationService)
                .update(same(organization), isNull(List.class), isNull(List.class), isNull(List.class));
    }

    @Test
    public void deleteOrganization() throws Exception {
        organizationsResource.deleteOrganization(ORGANIZATION_ID);

        verify(organizationService).delete(ORGANIZATION_ID);
    }

    @Test
    public void createOrganization() throws Exception {
        int userId = 3;
        OrganizationPost organizationPost = new OrganizationPost();
        given(userService.getLoggedInUser(true)).willReturn(new User(userId, 1));
        Organization organization = spy(new Organization());
        given(organizationPostConverter.convert(same(organizationPost))).willReturn(organization);

        PendingConverter pendingConverterContacts = mock(PendingConverter.class);
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        given(pendingConverterContacts.asList()).willReturn(contacts);
        given(apiContactConverter.convertAll(organizationPost.getContacts())).willReturn(pendingConverterContacts);

        PendingConverter pendingConverterAddress = mock(PendingConverter.class);
        ArrayList<Address> addresses = new ArrayList<Address>();
        given(pendingConverterAddress.asList()).willReturn(addresses);
        given(apiAddressConverter.convertAll(organizationPost.getContacts())).willReturn(pendingConverterAddress);

        organizationsResource.createOrganization(organizationPost);

        InOrder inOrder = Mockito.inOrder(organizationService, organization);
        assertThat(organization.getOwner()).isEqualTo(userId);
        inOrder.verify(organization).setOwner(userId);
        inOrder.verify(organizationService)
                .create(same(organization), same(contacts), same(addresses), anyList(), anyList());
    }
}
