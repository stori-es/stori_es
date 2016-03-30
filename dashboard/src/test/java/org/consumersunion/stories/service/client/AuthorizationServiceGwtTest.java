package org.consumersunion.stories.service.client;

import java.util.List;

import org.consumersunion.stories.common.client.service.RpcAuthorizationService;
import org.consumersunion.stories.common.client.service.RpcAuthorizationServiceAsync;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.AuthorizationResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.User;

import com.google.gwt.core.client.GWT;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

public class AuthorizationServiceGwtTest extends GWTTestCaseExposed {
    private final String NOT_LOGGED_IN = "Not logged in.";

    @Override
    public String getModuleName() {
        return "org.consumersunion.stories.storiesJUnit";
    }

    public void testAuthorizationService() {
        // kick it off
        noAuthAccessGrant();
    }

    private void noAuthAccessGrant() {
        final RpcAuthorizationServiceAsync authService = GWT.create(RpcAuthorizationService.class);

        userService.logout(new AsyncFail<ActionResponse>(AuthorizationServiceGwtTest.this, "noAuthAccessGrant:logout") {
            @Override
            public void onSuccess(ActionResponse result) {
                authService.grant(1053, ROLE_READER, 44, new AsyncFail<ActionResponse>(AuthorizationServiceGwtTest.this,
                        "noAuthAccessGrant:onSuccess") {
                    @Override
                    public void onSuccess(ActionResponse result) {
                        assertTrue("Result should indicate error.", result.isError());
                        assertEquals("Exception does not match with the one expected for method grant()",
                                NOT_LOGGED_IN, result.getGlobalErrorMessages().get(0));
                        noAuthAccessDeny();
                    }
                });
            }
        });

        delayTestFinish(1000001);
    }

    private void noAuthAccessDeny() {
        final RpcAuthorizationServiceAsync authService = GWT.create(RpcAuthorizationService.class);

        userService.logout(new AsyncFail<ActionResponse>(AuthorizationServiceGwtTest.this, "noAuthAccessDeny:logout") {
            @Override
            public void onSuccess(ActionResponse result) {
                authService.deny(1053, ROLE_READER, 44, new AsyncFail<ActionResponse>(AuthorizationServiceGwtTest.this,
                        "noAuthAccessDeny:deny") {
                    @Override
                    public void onSuccess(ActionResponse result) {
                        assertTrue("Result should indicate error.", result.isError());
                        assertEquals("Exception does not match with the one expected for method grant()",
                                NOT_LOGGED_IN, result.getGlobalErrorMessages().get(0));
                        denyAccessGrant();
                    }
                });
            }
        });
    }

    private void denyAccessGrant() {
        final RpcAuthorizationServiceAsync authService = GWT.create(RpcAuthorizationService.class);

        userService.login("test", "password", new AsyncFail<DatumResponse<User>>(AuthorizationServiceGwtTest.this,
                "denyAccessGrant:login") {
            @Override
            public void onSuccess(DatumResponse<User> result) {
                authService.grant(1053, ROLE_READER, 44, new AsyncFail<ActionResponse>(AuthorizationServiceGwtTest.this,
                        "denyAccessGrant:grant") {
                    @Override
                    public void onSuccess(ActionResponse result) {
                        assertTrue("Result should indicate error.", result.isError());
                        assertEquals("Exception does not match with the one expected for method grant()",
                                "Invalid request.", result.getGlobalErrorMessages().get(0));
                        denyAccessDeny();
                    }
                });
                /*
				 * authService.deny(1053, OPERATION_READ, 44, new
				 * AsyncCallback<ActionResponse>() {
				 * 
				 * @Override public void onSuccess(ActionResponse result) {
				 * fail("This should have failed; no one is logged in.");
				 * finishTest(); }
				 * 
				 * @Override public void onFailure(Throwable caught) {
				 * assertEquals(
				 * "Exception does not match with the one expected for method grant()"
				 * , NOT_LOGGED_IN, caught.getMessage());
				 * finishTest(); } });
				 */
            }
        });
    }

    private void denyAccessDeny() {
        final RpcAuthorizationServiceAsync authService = GWT.create(RpcAuthorizationService.class);

        userService.login("test", "password", new AsyncFail<DatumResponse<User>>(AuthorizationServiceGwtTest.this,
                "denyAccessDeny:login") {
            @Override
            public void onSuccess(DatumResponse<User> result) {
                authService.deny(1053, ROLE_READER, 44, new AsyncFail<ActionResponse>(AuthorizationServiceGwtTest.this,
                        "denyAccessDeny:deny") {
                    @Override
                    public void onSuccess(ActionResponse result) {
                        assertTrue("Result should indicate error.", result.isError());
                        assertEquals("Exception does not match with the one expected for method grant()",
                                "Invalid request.", result.getGlobalErrorMessages().get(0));
                        grantAccess();
                    }
                });
            }
        });
    }

    private void grantAccess() {
        final RpcAuthorizationServiceAsync authService = GWT.create(RpcAuthorizationService.class);

        userService.login("root", "password", new AsyncFail<DatumResponse<User>>(AuthorizationServiceGwtTest.this,
                "grantAccess:login") {
            @Override
            public void onSuccess(DatumResponse<User> result) {
                final int SID = 1053;
                // first check that the target user cannot access the target
                authService.isAuthorized(SID, ROLE_READER, 44, new AsyncFail<AuthorizationResponse>(
                        AuthorizationServiceGwtTest.this, "grantAccess:isAuthorized") {
                    @Override
                    public void onSuccess(AuthorizationResponse result) {
                        assertFalse("Unexpeted error.", result.isError());
                        assertFalse("Authorization should have been denied.", result.isGranted());
                        // now grant the auth
                        authService.grant(SID, ROLE_READER, 44, new AsyncFail<ActionResponse>(
                                AuthorizationServiceGwtTest.this, "grantAccess:grant") {
                            @Override
                            public void onSuccess(ActionResponse result) {
                                assertFalse("Unexpeted error.", result.isError());
                                authService.isAuthorized(SID, ROLE_READER, 44, new AsyncFail<AuthorizationResponse>(
                                        AuthorizationServiceGwtTest.this, "grantAccess:isAuthorized") {
                                    @Override
                                    public void onSuccess(AuthorizationResponse result) {
                                        assertFalse("Unexpeted error.", result.isError());
                                        assertTrue("Authorization should have been granted.", result.isGranted());
                                        // now reset everything
                                        authService.deny(SID, ROLE_READER, 44, new AsyncFail<ActionResponse>(
                                                AuthorizationServiceGwtTest.this, "grantAccess:deny") {
                                            @Override
                                            public void onSuccess(ActionResponse result) {
                                                assertFalse("Unexpeted error.", result.isError());
                                                // and verify it
                                                authService.isAuthorized(SID, ROLE_READER, 44,
                                                        new AsyncFail<AuthorizationResponse>(
                                                                AuthorizationServiceGwtTest.this,
                                                                "grantAccess:isAuthorized") {
                                                            @Override
                                                            public void onSuccess(AuthorizationResponse result) {
                                                                assertFalse("Unexpeted error.", result.isError());
                                                                assertFalse("Authorization should have been denied.",
                                                                        result.isGranted());
                                                                getValidPrincipalsNoAuthentication();
                                                            }
                                                        });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });

				/*
				 * authService.grant(1053, OPERATION_READ, 44, new
				 * AsyncCallback<ActionResponse>() {
				 * 
				 * @Override public void onSuccess(ActionResponse result) {
				 * assertFalse("Unexpected error.", result.isError());
				 * finishTest(); }
				 * 
				 * @Override public void onFailure(Throwable caught) {
				 * fail("This should not have failed; no one is logged in.");
				 * finishTest(); } });
				 * 
				 * authService.deny(1053, OPERATION_READ, 44, new
				 * AsyncCallback<ActionResponse>() {
				 * 
				 * @Override public void onSuccess(ActionResponse result) {
				 * fail("This should have failed; no one is logged in.");
				 * finishTest(); }
				 * 
				 * @Override public void onFailure(Throwable caught) {
				 * assertEquals(
				 * "Exception does not match with the one expected for method grant()"
				 * , NOT_LOGGED_IN, caught.getMessage()); } });
				 */
            }
        });
    }

    private void getValidPrincipalsNoAuthentication() {
        final RpcAuthorizationServiceAsync authService = GWT.create(RpcAuthorizationService.class);

        userService.logout(new AsyncFail<ActionResponse>(AuthorizationServiceGwtTest.this,
                "getValidPrincipalsNoAuthenticaiton:logout") {
            @Override
            public void onSuccess(ActionResponse result) {
                assertFalse("Unexpeted errors.", result.isError());
                authService.getValidPrincipals(ROLE_READER, 4, new AsyncFail<DatumResponse<List<SystemEntity>>>(
                        AuthorizationServiceGwtTest.this, "getValidPrincipalsNoAuthenticaiton:getValidPrincipals") {
                    @Override
                    public void onSuccess(DatumResponse<List<SystemEntity>> result) {
                        assertEquals("Unexpected error message count", 1, result.getGlobalErrorMessages().size());
                        assertEquals("Unexpected result.", NOT_LOGGED_IN, result.getGlobalErrorMessages().get(0));
                        getValidPrincipalsNoAuthorization();
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        super.onFailure(caught);
                    }
                });
            }
        });
    }

    private void getValidPrincipalsNoAuthorization() {
        final RpcAuthorizationServiceAsync authService = GWT.create(RpcAuthorizationService.class);

        userService.login("testUser", "password", new AsyncFail<DatumResponse<User>>(AuthorizationServiceGwtTest.this,
                "getValidPrincipalsNoAuthorization:login") {
            @Override
            public void onSuccess(DatumResponse<User> result) {
                assertFalse("Unexpeted errors.", result.isError());
                authService.getValidPrincipals(ROLE_READER, 1061, new AsyncFail<DatumResponse<List<SystemEntity>>>(
                        AuthorizationServiceGwtTest.this, "getValidPrincipalsNoAuthorization:getValidPrincipals") {
                    @Override
                    public void onSuccess(DatumResponse<List<SystemEntity>> result) {
                        assertEquals("Unexpected message count.", 1, result.getGlobalErrorMessages().size());
                        assertEquals("Unexpected result.", "Invalid parameters.",
                                result.getGlobalErrorMessages().get(0));
                        getValidPrincipalsAuthorized();
                    }
                });
            }
        });
    }

    private void getValidPrincipalsAuthorized() {
        final RpcAuthorizationServiceAsync authService = GWT.create(RpcAuthorizationService.class);

        userService.login("aclUser1", "password", new AsyncFail<DatumResponse<User>>(AuthorizationServiceGwtTest.this,
                "getValidPrincipalsAuthorized:login") {
            @Override
            public void onSuccess(DatumResponse<User> result) {
                assertFalse("Unexpeted errors.", result.isError());
                authService.getValidPrincipals(ROLE_READER, 4, new AsyncFail<DatumResponse<List<SystemEntity>>>(
                        AuthorizationServiceGwtTest.this, "getValidPrincipalsAuthorized:getValidPrincipals") {
                    @Override
                    public void onSuccess(DatumResponse<List<SystemEntity>> result) {
                        assertFalse("Unexpected errors.", result.isError());
                        assertEquals("Unexpected principal count.", 2, result.getDatum().size());
                        getValidPrincipalsSuperUser();
                    }
                });
            }
        });
    }

    private void getValidPrincipalsSuperUser() {
        final RpcAuthorizationServiceAsync authService = GWT.create(RpcAuthorizationService.class);

        userService.login("root", "password", new AsyncFail<DatumResponse<User>>(AuthorizationServiceGwtTest.this,
                "getValidPrincipalsSuperUser:login") {
            @Override
            public void onSuccess(DatumResponse<User> result) {
                assertFalse("Unexpeted errors.", result.isError());
                authService.getValidPrincipals(ROLE_READER, 4, new AsyncFail<DatumResponse<List<SystemEntity>>>(
                        AuthorizationServiceGwtTest.this, "getValidPrincipalsSuperUser:getValidPrincipals") {
                    @Override
                    public void onSuccess(DatumResponse<List<SystemEntity>> result) {
                        assertFalse("Unexpected errors.", result.isError());
                        assertEquals("Unexpected principal count.", 2, result.getDatum().size());
                        getValidTargetsNoAuthentication();
                    }
                });
            }
        });
    }

    private void getValidTargetsNoAuthentication() {
        final RpcAuthorizationServiceAsync authService = GWT.create(RpcAuthorizationService.class);

        userService.logout(new AsyncFail<ActionResponse>(AuthorizationServiceGwtTest.this,
                "getValidTargetsNoAuthentiaciton:logout") {
            @Override
            public void onSuccess(ActionResponse result) {
                assertFalse("Unexpeted errors.", result.isError());
                authService.getValidTargets(38, ROLE_READER, new AsyncFail<DatumResponse<List<SystemEntity>>>(
                        AuthorizationServiceGwtTest.this, "getValidTargetsNoAuthentiaciton:getValidTargets") {
                    @Override
                    public void onSuccess(DatumResponse<List<SystemEntity>> result) {
                        assertEquals("Unexpected error message count", 1, result.getGlobalErrorMessages().size());
                        assertEquals("Unexpected result.", NOT_LOGGED_IN, result.getGlobalErrorMessages().get(0));
                        getValidTargetsNoAuthorization();
                    }
                });
            }
        });
    }

    private void getValidTargetsNoAuthorization() {
        final RpcAuthorizationServiceAsync authService = GWT.create(RpcAuthorizationService.class);

        // User ID=30
        userService.login("dryan", "password", new AsyncFail<DatumResponse<User>>(AuthorizationServiceGwtTest.this,
                "getValidTargetsNoAuthorization:login") {
            @Override
            public void onSuccess(DatumResponse<User> result) {
                assertFalse("Unexpeted errors.", result.isError());
                authService.getValidTargets(1038, ROLE_READER, new AsyncFail<DatumResponse<List<SystemEntity>>>(
                        AuthorizationServiceGwtTest.this, "getValidTargetsNoAuthorization:getValidTargets") {
                    @Override
                    public void onSuccess(DatumResponse<List<SystemEntity>> result) {
                        assertEquals("Unexpected message count.", 1, result.getGlobalErrorMessages().size());
                        assertEquals("Unexpected result.", "Invalid request.", result.getGlobalErrorMessages().get(0));
                        getValidTargetsAuthorized();
                    }
                });
            }
        });
    }

    private void getValidTargetsAuthorized() {
        final RpcAuthorizationServiceAsync authService = GWT.create(RpcAuthorizationService.class);

        userService.login("testUser", "password", new AsyncFail<DatumResponse<User>>(AuthorizationServiceGwtTest.this,
                "getValidTargetsAuthorized:login") {
            @Override
            public void onSuccess(DatumResponse<User> result) {
                assertFalse("Unexpeted errors.", result.isError());
                authService.getValidTargets(1001, ROLE_READER, new AsyncFail<DatumResponse<List<SystemEntity>>>(
                        AuthorizationServiceGwtTest.this, "getValidTargetsAuthorized:getValidTargets") {
                    @Override
                    public void onSuccess(DatumResponse<List<SystemEntity>> result) {
                        assertFalse("Unexpected errors.", result.isError());
                        assertEquals("Unexpected principal count.", 20, result.getDatum().size());
                        getValidTargetsSuperUser();
                    }
                });
            }
        });
    }

    private void getValidTargetsSuperUser() {
        final RpcAuthorizationServiceAsync authService = GWT.create(RpcAuthorizationService.class);

        userService.login("root", "password", new AsyncFail<DatumResponse<User>>(AuthorizationServiceGwtTest.this,
                "getValidTargetsSuperUser:login") {
            @Override
            public void onSuccess(DatumResponse<User> result) {
                assertFalse("Unexpeted errors.", result.isError());
                // we use 'collection' because it's number is constant throughout all the tests (others should be, but
                // the test implementation is a bit lacking in that regard); first, root asks about someone else
                authService.getValidTargets(1001, ROLE_READER, "collection",
                        new AsyncFail<DatumResponse<List<SystemEntity>>>(
                                AuthorizationServiceGwtTest.this, "getValidTargetsSuperUser:getValidTargets") {
                            @Override
                            public void onSuccess(DatumResponse<List<SystemEntity>> result) {
                                assertFalse("Unexpected errors.", result.isError());
                                assertEquals("Unexpected principal count.", 9, result.getDatum().size());

                                // now check that root asking about root. Root *can* read anything, but it's not through grants
                                // so valid targets is correctly '0'
                                authService.getValidTargets(0, ROLE_READER, "collection",
                                        new AsyncFail<DatumResponse<List<SystemEntity>>>(
                                                AuthorizationServiceGwtTest.this,
                                                "getValidTargetsSuperUser:getValidTargets") {
                                            @Override
                                            public void onSuccess(DatumResponse<List<SystemEntity>> result) {
                                                assertFalse("Unexpected errors.", result.isError());
                                                assertEquals("Unexpected target count.", 21, result.getDatum().size());
                                                finishTest();
                                            }
                                        });
                            }
                        });
            }
        });
    }
}
