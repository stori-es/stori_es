package org.consumersunion.stories.server.rest.api.convio;

import javax.inject.Inject;

import org.consumersunion.stories.SpringTestCase;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.server.service.datatransferobject.ConvioConstituent;
import org.consumersunion.stories.common.shared.model.CredentialedUser;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.server.api.gwt_rpc.RpcUserServiceImpl;
import org.consumersunion.stories.server.persistence.CredentialedUserPersister;
import org.consumersunion.stories.server.persistence.ProfilePersister;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.consumersunion.stories.server.persistence.CredentialedUserPersister.UserProfileStruct;

public class ConvioServiceTest_Hidden extends SpringTestCase {
    @Inject
    private CredentialedUserPersister credentialedUserPersister;
    @Inject
    private SyncFromSysPersonToConvioConstituentRequestFactory requestFactory;
    @Inject
    private ProfilePersister profilePersister;

    private ConvioDataSynchronizationWorker convioSyncWorker;
    private RpcUserServiceImpl userService;
    private PollConvioForConstituentData loadConvioAccountRequest;
    private SyncFromSysPersonToConvioConstituentRequest syncRequest;

    @Override
    public void setUp() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/META-INF/applicationContext.xml");
        convioSyncWorker = applicationContext.getBean("convioSyncWorker", ConvioDataSynchronizationWorker.class);
        userService = applicationContext.getBean(RpcUserServiceImpl.class);
    }

    public void testCreateThenLoadUser() {
        User newUser = new User();
        newUser.setActive(true);
        newUser.setHandle("hello@convio.com");
        CredentialedUser credentialedUser = new CredentialedUser();
        credentialedUser.setUser(newUser);
        Profile initialProfile = new Profile();
        initialProfile.setOrganizationId(2);
        UserProfileStruct userProfile = new UserProfileStruct(credentialedUser, initialProfile);
        userProfile = credentialedUserPersister.createUserProfile(userProfile);
        newUser = userProfile.credentialedUser.getUser();
        syncRequest = requestFactory.create(profilePersister.get(newUser.getId()));

        DatumResponse<User> result = userService.login("root", "password");
        assertFalse("Unexpeted errors.", result.isError());
        convioSyncWorker.process(syncRequest);
        assertNotNull(syncRequest.getResponse().getCons_id());
        final int consId = Integer.parseInt(syncRequest.getResponse().getCons_id());

        loadConvioAccountRequest = new PollConvioForConstituentData(consId, 2);
        ConvioConstituent convioMainObject = new ConvioConstituent();
        convioMainObject.setUser_name("hello@convio.com");
        convioMainObject.setSid(0);

        assertFalse("Unexpeted errors.", result.isError());
        convioSyncWorker.process(loadConvioAccountRequest);
        assertNotNull(loadConvioAccountRequest.getConvioConstituent());
    }

    public void testUpdateContactAddress() {
        DatumResponse<User> result = userService.login("root", "password");
        assertFalse("Unexpeted errors.", result.isError());

        SyncFromSysPersonToConvioConstituentRequest syncRequest =
                requestFactory.create(profilePersister.get(235551));
        convioSyncWorker.processAsync(syncRequest);

        assertEquals(syncRequest.getResponse().getMessage(), "User updated.");
    }
}
