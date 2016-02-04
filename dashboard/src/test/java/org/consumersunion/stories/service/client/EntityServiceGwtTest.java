package org.consumersunion.stories.service.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.consumersunion.stories.common.client.service.RpcEntityService;
import org.consumersunion.stories.common.client.service.RpcEntityServiceAsync;
import org.consumersunion.stories.common.client.service.RpcProfileService;
import org.consumersunion.stories.common.client.service.RpcProfileServiceAsync;
import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.client.service.response.AddressResponse;
import org.consumersunion.stories.common.client.service.response.ContactResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.entity.Contact;

import com.google.common.base.Joiner;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class EntityServiceGwtTest extends GWTTestCaseExposed {
    @Override
    public String getModuleName() {
        return "org.consumersunion.stories.storiesJUnit";
    }

    public void testGetAddress() {
        userService.login("aclUser1", "password",
                new AsyncFail<DatumResponse<User>>(EntityServiceGwtTest.this, "Failed to login") {

                    @Override
                    public void onSuccess(DatumResponse<User> result) {
                        final RpcEntityServiceAsync service = GWT.create(RpcEntityService.class);
                        service.getAddress(53, new AsyncCallback<AddressResponse>() {

                            @Override
                            public void onFailure(final Throwable t) {
                                fail("getAddress() fails with message: " + t.getMessage());
                                finishTest();
                            }

                            @Override
                            public void onSuccess(final AddressResponse result) {
                                assertNotNull("Address response must not be null", result);
                                assertNotNull("Address List must not be null", result.getData());
                                assertEquals("There must be two address for User #2", 2, result.getData().size());
                                finishTest();
                            }
                        });
                    }
                });

        delayTestFinish(10003);
    }

    public void testUpdateAddress() {
        final List<Address> list = new ArrayList<Address>(2);
        final Address myHomeAddress = new Address(35);
        myHomeAddress.setAddress1("Home Address");
        myHomeAddress.setAddress2("Home Address #2");
        myHomeAddress.setCity("Miami");
        myHomeAddress.setCountry("US");
        myHomeAddress.setLatitude(new BigDecimal("45.789"));
        myHomeAddress.setLongitude(new BigDecimal("45.789"));
        myHomeAddress.setPostalCode("33010");
        myHomeAddress.setRelation("Home");
        myHomeAddress.setState("FL");
        list.add(myHomeAddress);

        final Address myOfficeAddress = new Address(35);
        myOfficeAddress.setAddress1("Office Address");
        myOfficeAddress.setAddress2("Office Address #2");
        myOfficeAddress.setCity("Las Vegas");
        myOfficeAddress.setCountry("US");
        myOfficeAddress.setLatitude(new BigDecimal("45.789"));
        myOfficeAddress.setLongitude(new BigDecimal("45.789"));
        myOfficeAddress.setPostalCode("89044");
        myOfficeAddress.setRelation("Office");
        myOfficeAddress.setState("NV");
        list.add(myOfficeAddress);

        userService.login("aclUser1", "password",
                new AsyncFail<DatumResponse<User>>(EntityServiceGwtTest.this, "Failed to login") {

                    @Override
                    public void onSuccess(DatumResponse<User> result) {
                        final RpcEntityServiceAsync service = GWT.create(RpcEntityService.class);
                        service.updateAddress(list, 35, new AsyncCallback<AddressResponse>() {
                            @Override
                            public void onFailure(final Throwable t) {
                                fail("updateAddress() fails with message: " + t.getMessage());
                                finishTest();
                            }

                            @Override
                            public void onSuccess(final AddressResponse result) {
                                if (result.getGlobalErrorMessages().size() > 0) {
                                    fail("Unexpected error updating address; " + result.getGlobalErrorMessages().get(
                                            0));
                                }
                                assertNotNull("Address response must not be null", result);
                                assertNotNull("Address List must not be null", result.getData());
                                assertEquals("There must be two address for User #1", 2, result.getData().size());
                                finishTest();
                            }
                        });
                    }
                });
        delayTestFinish(10004);
    }

    // TODO: this test is in the wrong place, but there's no PersonServiceTest
    // at the moment, so we leave it
    public void testUpdatePerson() {
        final Profile profile = new Profile(1038, 2,
                10); // Person 38 is set in the DB to have version 10, which we need to match to avoid the update
                // method from complaining about version mismatch.
        profile.setGivenName("ACL User ");
        profile.setSurname("Test#2 Updated");

        userService.login("aclUser2", "password",
                new AsyncFail<DatumResponse<User>>(EntityServiceGwtTest.this, "Failed to login") {

                    @Override
                    public void onSuccess(DatumResponse<User> result) {
                        final RpcProfileServiceAsync service = GWT.create(RpcProfileService.class);
                        service.update(profile, new AsyncCallback<DatumResponse<ProfileSummary>>() {

                            @Override
                            public void onFailure(final Throwable t) {
                                fail("update() fails with message: " + t.getMessage());
                                finishTest();
                            }

                            @Override
                            public void onSuccess(final DatumResponse<ProfileSummary> result) {
                                if (result.getGlobalErrorMessages().size() > 0) {
                                    fail("There must not be a error updating Person; " + result
                                            .getGlobalErrorMessages().get(
                                            0));
                                }
                                assertNotNull("There must be a result after update call", result.getDatum());
                                finishTest();
                            }
                        });
                    }
                });
        delayTestFinish(10005);
    }

    public void testRetrieveContacts() {
        userService.login("testUser", "password",
                new AsyncFail<DatumResponse<User>>(EntityServiceGwtTest.this, "Failed to login") {
                    @Override
                    public void onSuccess(DatumResponse<User> result) {
                        final RpcEntityServiceAsync service = GWT.create(RpcEntityService.class);
                        service.retrieveContacts(1001, new AsyncCallback<ContactResponse>() {

                            @Override
                            public void onFailure(final Throwable t) {
                                fail("retrieveContacts() fails with message: " + t.getMessage());
                                finishTest();
                            }

                            @Override
                            public void onSuccess(final ContactResponse result) {
                                if (!result.getGlobalErrorMessages().isEmpty()) {
                                    fail("There must not be a error retrieving contacts; " + result
                                            .getGlobalErrorMessages().get(
                                            0));
                                }

                                assertNotNull("No contact mediums retrieved.", result.getContactMediums());
                                assertEquals("getContactMediums does not match; got " + Joiner.on(", ").join(
                                        result.getContactMediums()), 2, result.getContactMediums().size());
                                // test getContactMediums()
                                assertTrue(
                                        "getContactMediums does not match",
                                        Contact.MediumType.EMAIL.name().equals(result.getContactMediums().get(0))
                                                || Contact.MediumType.PHONE.name().equals(
                                                result.getContactMediums().get(0))
                                );
                                assertTrue(
                                        "getContactMediums does not match",
                                        Contact.MediumType.EMAIL.name().equals(result.getContactMediums().get(1))
                                                || Contact.MediumType.PHONE.name().equals(
                                                result.getContactMediums().get(1))
                                );
                                assertTrue("There must be more than 1 results for medium email",
                                        (result.getContacts(Contact.MediumType.EMAIL.name()).size() > 1));
                                assertEquals("There must be 4 results for medium phone", 4,
                                        result.getContacts(Contact.MediumType.PHONE.name()).size());
                                assertEquals("First Contact must be type Mobile", Contact.TYPE_MOBILE,
                                        result.getContacts(Contact.MediumType.PHONE.name()).get(0).getType());
                                assertEquals("Second Conctact must be type Home", Contact.TYPE_HOME,
                                        result.getContacts(Contact.MediumType.PHONE.name()).get(1).getType());
                                assertEquals("Third Contact must be type Work ", Contact.TYPE_WORK,
                                        result.getContacts(Contact.MediumType.PHONE.name()).get(2).getType());
                                assertEquals("Last Contact must be type Other", Contact.TYPE_OTHER,
                                        result.getContacts(Contact.MediumType.PHONE.name()).get(3).getType());
                                finishTest();
                            }
                        });
                        delayTestFinish(10009);
                    }
                }
        );
    }
}
