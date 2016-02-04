package org.consumersunion.stories.server.api.rest;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.consumersunion.stories.common.shared.dto.ResetPasswordRequest;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.server.business_logic.AccountService;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class AccountResourceTest {

    @Inject
    private AccountResource accountResource;
    @Inject
    private AccountService accountService;

    @Test
    public void generateResetPassword_delegaoService_delegatesToService() {
        String aUsername = "a_username";

        accountResource.generateResetPassword(aUsername);

        verify(accountService).generateResetPassword(aUsername);
    }

    @Test
    public void resetPassword_delegatesToService() {
        ResetPasswordRequest passwordRequest = mock(ResetPasswordRequest.class);

        accountResource.resetPassword(passwordRequest);

        verify(accountService).resetPassword(same(passwordRequest));
    }

    @Test
    public void getEmails_delegatesToService() {
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        given(accountService.getEmails()).willReturn(contacts);

        Response response = accountResource.getEmails();

        assertThat(response.getEntity()).isSameAs(contacts);
    }

    @Test
    public void getPrimaryEmail_delegatesToService() {
        String primaryEmail = "primaryEmail";
        given(accountService.getPrimaryEmail()).willReturn(primaryEmail);

        Response response = accountResource.getPrimaryEmail();

        assertThat(response.getEntity()).isEqualTo(primaryEmail);
    }

    @Test
    public void setPrimaryEmail_delegatesToService() {
        Contact contact = new Contact();

        accountResource.setPrimaryEmail(contact);

        verify(accountService).setPrimaryEmail(same(contact));
    }
}
