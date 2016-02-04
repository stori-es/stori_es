package org.consumersunion.stories.service.client;

import java.util.List;

import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.i18n.CommonI18nErrorMessages;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;

public class UserServiceGwtTest extends GWTTestCaseExposed {
    @Override
    public String getModuleName() {
        return "org.consumersunion.stories.storiesJUnit";
    }

    ;

    public void testUserService() {
        registerLocale();
    }

    /**
     * testing registerLocale() method in UserService
     */
    private void registerLocale() {
        userService.registerLocale("de", new AsyncFail<ActionResponse>(UserServiceGwtTest.this,
                "registerLocale:registerLocale-1") {
            @Override
            public void onSuccess(final ActionResponse result) {
                final List<String> errorMessagesB = result.getGlobalErrorMessages();
                assertEquals("Unexpected number of error messages.", 0, errorMessagesB.size());
                // okay, now ready to see if we get the German!
                String defaultOrg = "2";
                userService.createAccount(null, null, null, null, null, null, Lists.newArrayList(defaultOrg),
                        defaultOrg,
                        new AsyncFail<ActionResponse>(UserServiceGwtTest.this, "registerLocale:createAccount") {
                            @Override
                            public void onSuccess(final ActionResponse result) {
                                final List<String> errorMessagesC = result.getGlobalErrorMessages();
                                assertEquals("Unexpected number of error messages.", 1, errorMessagesC.size());
                                assertEquals("Unexpected error message.", "Ungltiger Parameter.",
                                        errorMessagesC.get(0));

                                userService.registerLocale("en", new AsyncFail<ActionResponse>(
                                        UserServiceGwtTest.this, "registerLocale:registerLocale-2") {
                                    @Override
                                    public void onSuccess(final ActionResponse result) {
                                        getLoggedInUser();
                                    }
                                });
                            }
                        });
            }
        });
        delayTestFinish(1000000);
    }

    /**
     * testing getLoggedInUser() method in UserService
     */
    private void getLoggedInUser() {
        userService.login("testUser", "password", new AsyncFail<DatumResponse<User>>(UserServiceGwtTest.this,
                "getLoggedInUser:login") {
            @Override
            public void onSuccess(final DatumResponse<User> result) {
                if (result.isError()) {
                    fail("Unexpected login error: " + result.getGlobalErrorMessages().get(0));
                }

                userService.getLoggedInUser(new AsyncFail<DatumResponse<User>>(UserServiceGwtTest.this,
                        "getLoggedInUser:getLoggedInUser") {
                    @Override
                    public void onSuccess(final DatumResponse<User> result) {
                        final User user = result.getDatum();
                        assertNotNull(user);
                        assertEquals("testUser", user.getHandle());
                        getUser();
                    }
                });
            }
        });
    }

    /**
     * testing getUser() method in UserService
     */
    private void getUser() {
        userService.login("testUser", "password", new AsyncFail<DatumResponse<User>>(UserServiceGwtTest.this,
                "getUser:login") {
            @Override
            public void onSuccess(final DatumResponse<User> result) {
                userService.getUser("testUser", new AsyncFail<DatumResponse<User>>(UserServiceGwtTest.this,
                        "getUser:getUser") {
                    @Override
                    public void onSuccess(final DatumResponse<User> result) {
                        if (result.isError()) {
                            fail("Error getting user: " + result.getGlobalErrorMessages().get(0));
                        }
                        final User user = result.getDatum();
                        assertNotNull(user);
                        updateAccountNullValue();
                    }
                });
            }
        });
    }

    /**
     * testing updateAccount() method with null user object in UserService
     */
    private void updateAccountNullValue() {
        final User nullUser = null;
        userService.updateAccount(nullUser, new AsyncFail<DatumResponse<User>>(UserServiceGwtTest.this,
                "updateAccountNullValue:login") {
            @Override
            public void onSuccess(final DatumResponse<User> result) {
                assertNotNull(result.getGlobalErrorMessages());
                assertEquals(1, result.getGlobalErrorMessages().size());
                assertEquals("Invalid parameters.", result.getGlobalErrorMessages().get(0));
                updateAccount();
            }
        });
    }

    /**
     * testing updateAccount() method in UserService
     */
    private void updateAccount() {
        userService.login("testUser", "password", new AsyncFail<DatumResponse<User>>(UserServiceGwtTest.this,
                "updateAccount:login") {
            @Override
            public void onSuccess(final DatumResponse<User> result) {
                userService.getUser("testUser", new AsyncFail<DatumResponse<User>>(UserServiceGwtTest.this,
                        "updateAccount:getUser") {
                    @Override
                    public void onSuccess(final DatumResponse<User> result) {
                        if (result.isError()) {
                            fail("Unexpected failure: " + result.getGlobalErrorMessages().get(0));
                        }

                        final User user = result.getDatum();

                        userService.updateAccount(user, new AsyncFail<DatumResponse<User>>(UserServiceGwtTest.this,
                                "updateAccount:updateAccount") {
                            @Override
                            public void onSuccess(final DatumResponse<User> result) {
                                assertEquals(0, result.getGlobalErrorMessages().size());
                                updateAccountRevert();
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * testing updateAccount() method reverting changes modified by
     * testUpdateAccount() in UserService
     */
    private void updateAccountRevert() {
        userService.login("testUser", "password", new AsyncFail<DatumResponse<User>>(UserServiceGwtTest.this,
                "updateAccountRevert:login") {
            @Override
            public void onSuccess(final DatumResponse<User> result) {
                final User user = result.getDatum();

                userService.updateAccount(user, new AsyncFail<DatumResponse<User>>(UserServiceGwtTest.this,
                        "updateAccount:updateAccount") {
                    @Override
                    public void onSuccess(final DatumResponse<User> result) {
                        if (result.getGlobalErrorMessages().size() > 0) {
                            assertEquals(0, result.getGlobalErrorMessages().size());
                        }
                        updatePasswordUserNotLogged();
                    }
                });
            }
        });
    }

    private void updatePasswordUserNotLogged() {
        userService.logout(new AsyncFail<ActionResponse>(UserServiceGwtTest.this, "updatePasswordUserNotLogged:login") {
            @Override
            public void onSuccess(ActionResponse result) {
                final CommonI18nErrorMessages messages = GWT.create(CommonI18nErrorMessages.class);
                userService.updatePassword("password", "newPassword", new AsyncFail<ActionResponse>(
                        UserServiceGwtTest.this, "updatePasswordUserNotLogged:updatePassword") {
                    @Override
                    public void onSuccess(final ActionResponse result) {
                        // the standard implementation
                        // redirects the user when not
                        // logged in; appearently that
                        // causes a problem with the unit
                        // test timing out so we override to
                        // avoid the redirect and
                        // check the error message directly
                        assertEquals(1, result.getGlobalErrorMessages().size());
                        assertEquals(messages.notLoggedIn(), result.getGlobalErrorMessages().get(0));
                        updatePasswordDefaultUser();
                    }
                });
            }
        });
    }

    private void updatePasswordDefaultUser() {
        userService.login("testUser", "password", new AsyncFail<DatumResponse<User>>(UserServiceGwtTest.this,
                "updatePasswordDefaultUser:login") {
            @Override
            public void onSuccess(final DatumResponse<User> result) {
                assertFalse("Unexpected error.", result.isError());
                userService.updatePassword("password", "newPassword", new AsyncFail<ActionResponse>(
                        UserServiceGwtTest.this, "updatePasswordUserNotLogged:updatePassword") {
                    @Override
                    public void onSuccess(final ActionResponse result) {
                        assertEquals(result.getGlobalErrorMessages().size() > 0 ? result.getGlobalErrorMessages()
                                .get(0) : "", 0, result.getGlobalErrorMessages().size());

                        userService.updatePassword("newPassword", "password", new AsyncFail<ActionResponse>(
                                UserServiceGwtTest.this, "updatePasswordUserNotLogged:updatePassword-2") {
                            @Override
                            public void onSuccess(final ActionResponse result) {
                                assertEquals(result.getGlobalErrorMessages().size() > 0 ? result
                                        .getGlobalErrorMessages().get(0) : "", 0, result.getGlobalErrorMessages()
                                        .size());
                                updatePasswordDefaultUserWrongPassword();
                            }
                        });
                    }
                });
            }
        });
    }

    private void updatePasswordDefaultUserWrongPassword() {
        final CommonI18nErrorMessages messages = GWT.create(CommonI18nErrorMessages.class);
        userService.login("testUser", "password", new AsyncFail<DatumResponse<User>>(UserServiceGwtTest.this,
                "updatePasswordDefaultUserWrongPassword:login") {
            @Override
            public void onSuccess(final DatumResponse<User> result) {
                if (result.getGlobalErrorMessages().size() == 0) {
                    userService.updatePassword("password01", "newPassword", new AsyncFail<ActionResponse>(
                            UserServiceGwtTest.this, "updatePasswordDefaultUserWrongPassword:updatePassword") {
                        @Override
                        public void onSuccess(final ActionResponse result) {
                            assertEquals(1, result.getGlobalErrorMessages().size());
                            assertEquals(messages.currentPasswordInvalid(), result.getGlobalErrorMessages().get(0));
                            finishTest();
                        }
                    });
                } else {
                    fail("There was a problem setting devmode property");
                    finishTest();
                }
            }
        });
    }
}
