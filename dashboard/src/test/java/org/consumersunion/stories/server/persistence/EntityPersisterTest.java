package org.consumersunion.stories.server.persistence;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.consumersunion.stories.SpringTestCase;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.junit.Test;

public class EntityPersisterTest extends SpringTestCase {
    public void testRetrieveAllAddress() {
        @SuppressWarnings("unchecked")
        List<Address> address = (List<Address>) PersistenceUtil.process(new ContactPersister.RetrieveAddress(1));
        assertEquals("There must be 2 address for Entity #1", 2, address.size());
        assertEquals("Home address must be the first address", "Home", address.get(0).getRelation());
        assertEquals("Office address must be the second address", "Work", address.get(1).getRelation());
    }

    public void testRetiveAddress() {
        @SuppressWarnings("unchecked")
        List<Address> address = (List<Address>) PersistenceUtil.process(new ContactPersister.RetrieveAddress(1));

        assertEquals("Entity field does not match", 1, address.get(0).getEntity());
        assertEquals("Relation field does not match", "Home", address.get(0).getRelation());
        assertEquals("Address1 field does not match", "Home Address", address.get(0).getAddress1());
        assertEquals("Address2 field does not match", "Address #2", address.get(0).getAddress2());
        assertEquals("City field does not match", "Las Vegas", address.get(0).getCity());
        assertEquals("State field does not match", "NV", address.get(0).getState());
        assertEquals("Country field does not match", "US", address.get(0).getCountry());
        assertEquals("Postal Code field does not match", "89044", address.get(0).getPostalCode());
        assertEquals("Latitude field does not match", new BigDecimal("12.1230000"), address.get(0).getLatitude());
        assertEquals("Longitude field does not match", new BigDecimal("12.1230000"), address.get(0).getLongitude());
    }

    public void testDeleteAddress() {
        @SuppressWarnings("unchecked")
        List<Address> addresses = (List<Address>) PersistenceUtil.process(new ContactPersister.RetrieveAddress(53));
        Address addressToDelete = addresses.get(0);
        Address deletedAddress = (Address) PersistenceUtil.process(new ContactPersister.DeleteAddress(addressToDelete));
        assertEquals("Expected deleted address Relation", addressToDelete.getRelation(), deletedAddress.getRelation());
    }

    @Test
    public void testUpdateAddress() {
        List<Address> list = new ArrayList<Address>(2);

        Address myHomeAddress = new Address(2);
        myHomeAddress.setAddress1("Home Address updated");
        myHomeAddress.setAddress2("Address #2 updated");
        myHomeAddress.setCity("Las Vegas");
        myHomeAddress.setCountry("US");
        myHomeAddress.setLatitude(new BigDecimal("13.1230000"));
        myHomeAddress.setLongitude(new BigDecimal("13.1230000"));
        myHomeAddress.setPostalCode("852011");
        myHomeAddress.setRelation("Home");
        myHomeAddress.setState("NV");
        list.add(myHomeAddress);

        Address myOfficeAddress = new Address(2);
        myOfficeAddress.setAddress1("Office Address updated");
        myOfficeAddress.setAddress2("Address #2 updated");
        myOfficeAddress.setCity("Miami");
        myOfficeAddress.setCountry("US");
        myOfficeAddress.setLatitude(new BigDecimal("13.1230000"));
        myOfficeAddress.setLongitude(new BigDecimal("13.1230000"));
        myOfficeAddress.setPostalCode("852011");
        myOfficeAddress.setRelation("Work");
        myOfficeAddress.setState("FL");
        list.add(myOfficeAddress);

        PersistenceUtil.process(new ContactPersister.UpdateAddressForEntity(list));

        @SuppressWarnings("unchecked")
        List<Address> address = PersistenceUtil.process(new ContactPersister.RetrieveAddress(2));
        assertEquals("There must be 2 address for Entity #1", 2, address.size());
        assertEquals("Entity field does not match", myHomeAddress.getEntity(), address.get(0).getEntity());
        assertEquals("Relation field does not match", myHomeAddress.getRelation(), address.get(0).getRelation());
        assertEquals("Address1 field does not match", myHomeAddress.getAddress1(), address.get(0).getAddress1());
        assertEquals("Address2 field does not match", myHomeAddress.getAddress2(), address.get(0).getAddress2());
        assertEquals("City field does not match", myHomeAddress.getCity(), address.get(0).getCity());
        assertEquals("State field does not match", myHomeAddress.getState(), address.get(0).getState());
        assertEquals("Country field does not match", myHomeAddress.getCountry(), address.get(0).getCountry());
        assertEquals("Postal Code field does not match", myHomeAddress.getPostalCode(), address.get(0).getPostalCode());
        assertEquals("Latitude field does not match", myHomeAddress.getLatitude(), address.get(0).getLatitude());
        assertEquals("Longitude field does not match", myHomeAddress.getLongitude(), address.get(0).getLongitude());

        assertEquals("Entity field does not match", myOfficeAddress.getEntity(), address.get(1).getEntity());
        assertEquals("Relation field does not match", myOfficeAddress.getRelation(), address.get(1).getRelation());
        assertEquals("Address1 field does not match", myOfficeAddress.getAddress1(), address.get(1).getAddress1());
        assertEquals("Address2 field does not match", myOfficeAddress.getAddress2(), address.get(1).getAddress2());
        assertEquals("City field does not match", myOfficeAddress.getCity(), address.get(1).getCity());
        assertEquals("State field does not match", myOfficeAddress.getState(), address.get(1).getState());
        assertEquals("Country field does not match", myOfficeAddress.getCountry(), address.get(1).getCountry());
        assertEquals("Postal Code field does not match", myOfficeAddress.getPostalCode(), address.get(1)
                .getPostalCode());
        assertEquals("Latitude field does not match", myOfficeAddress.getLatitude(), address.get(1).getLatitude());
        assertEquals("Longitude field does not match", myOfficeAddress.getLongitude(), address.get(1).getLongitude());
    }

    public void testSaveContacts() throws SQLException {
        Connection conn = PersistenceUtil.getConnection();
        List<Contact> list = new ArrayList<Contact>(2);
        list.add(new Contact(35, Contact.MediumType.EMAIL.name(), Contact.TYPE_WORK, "acluser1@office.com"));
        list.add(new Contact(35, Contact.MediumType.PHONE.name(), Contact.TYPE_WORK, "(212) 289-7297"));

        PersistenceUtil.process(new ContactPersister.SaveContactsFunc(list));
        PreparedStatement select = conn.prepareStatement("SELECT COUNT(*) FROM contact WHERE entityId = 35");
        final ResultSet rs = select.executeQuery();
        if (!rs.next()) {
            fail("There must be at least 2 contacts for entity 35");
        }
        assertEquals("There must be 2 contacts for entity 35", 2, rs.getInt(1));
    }

    // public void testRetrieveContacts() {
    // EntityPersister persister = new EntityPersister();
    // @SuppressWarnings("unchecked")
    // Map<String, List< Contact>> map = (Map<String, List<
    // Contact>>)PersistenceService.process(persister.retrieveContactFunc(1));
    // assertEquals("There must be 2 keys for medium", 2, map.keySet().size());
    // assertEquals("There must be 4 phones inside contacts for entity 1", 4,
    // map.get(Contact.PHONE).size());
    // assertTrue("Phone must be one map key for entity 1",
    // map.containsKey(Contact.PHONE));
    // assertTrue("Email must be one map key for entity 1",
    // map.containsKey(Contact.EMAIL));
    // assertEquals("There must be 3 emails inside contacts for entity 1", 2,
    // map.get(Contact.EMAIL).size());
    // //test entityId
    // assertEquals("EntityId does not match with expected",1,map.get(Contact.PHONE).get(0).getEntityId());
    // assertEquals("EntityId does not match with expected",1,map.get(Contact.PHONE).get(1).getEntityId());
    // assertEquals("EntityId does not match with expected",1,map.get(Contact.PHONE).get(2).getEntityId());
    // assertEquals("EntityId does not match with expected",1,map.get(Contact.PHONE).get(3).getEntityId());
    // assertEquals("EntityId does not match with expected",1,map.get(Contact.EMAIL).get(0).getEntityId());
    // assertEquals("EntityId does not match with expected",1,map.get(Contact.EMAIL).get(1).getEntityId());
    // assertEquals("EntityId does not match with expected",1,map.get(Contact.EMAIL).get(2).getEntityId());
    // //test medium
    // assertEquals("Medium does not match with expected",Contact.PHONE,map.get(Contact.PHONE).get(0).getMedium());
    // assertEquals("Medium does not match with expected",Contact.PHONE,map.get(Contact.PHONE).get(1).getMedium());
    // assertEquals("Medium does not match with expected",Contact.PHONE,map.get(Contact.PHONE).get(2).getMedium());
    // assertEquals("Medium does not match with expected",Contact.PHONE,map.get(Contact.PHONE).get(3).getMedium());
    // assertEquals("Medium does not match with expected",Contact.EMAIL,map.get(Contact.EMAIL).get(0).getMedium());
    // assertEquals("Medium does not match with expected",Contact.EMAIL,map.get(Contact.EMAIL).get(1).getMedium());
    // assertEquals("Medium does not match with expected",Contact.EMAIL,map.get(Contact.EMAIL).get(2).getMedium());
    // //test type
    // assertTrue("Type does not match with expected",
    // Contact.TYPE_HOME.equals(map.get(Contact.PHONE).get(0).getType())
    // ||
    // Contact.TYPE_WORK.equals(map.get(Contact.PHONE).get(0).getType())
    // ||
    // Contact.TYPE_MOBILE.equals(map.get(Contact.PHONE).get(0).getType())
    // ||
    // Contact.TYPE_OTHER.equals(map.get(Contact.PHONE).get(0).getType()));
    // assertTrue("Type does not match with expected",
    // Contact.TYPE_HOME.equals(map.get(Contact.PHONE).get(1).getType())
    // ||
    // Contact.TYPE_WORK.equals(map.get(Contact.PHONE).get(1).getType())
    // ||
    // Contact.TYPE_MOBILE.equals(map.get(Contact.PHONE).get(1).getType())
    // ||
    // Contact.TYPE_OTHER.equals(map.get(Contact.PHONE).get(1).getType()));
    // assertTrue("Type does not match with expected",
    // Contact.TYPE_HOME.equals(map.get(Contact.PHONE).get(2).getType())
    // ||
    // Contact.TYPE_WORK.equals(map.get(Contact.PHONE).get(2).getType())
    // ||
    // Contact.TYPE_MOBILE.equals(map.get(Contact.PHONE).get(2).getType())
    // ||
    // Contact.TYPE_OTHER.equals(map.get(Contact.PHONE).get(2).getType()));
    // assertTrue("Type does not match with expected",
    // Contact.TYPE_HOME.equals(map.get(Contact.PHONE).get(3).getType())
    // ||
    // Contact.TYPE_WORK.equals(map.get(Contact.PHONE).get(3).getType())
    // ||
    // Contact.TYPE_MOBILE.equals(map.get(Contact.PHONE).get(3).getType())
    // ||
    // Contact.TYPE_OTHER.equals(map.get(Contact.PHONE).get(3).getType()));
    // assertTrue("Type does not match with expected",
    // Contact.TYPE_HOME.equals(map.get(Contact.EMAIL).get(0).getType())
    // ||
    // Contact.TYPE_OTHER.equals(map.get(Contact.EMAIL).get(0).getType())
    // ||
    // Contact.TYPE_WORK.equals(map.get(Contact.EMAIL).get(0).getType()));
    // assertTrue("Type does not match with expected",
    // Contact.TYPE_HOME.equals(map.get(Contact.EMAIL).get(1).getType())
    // ||
    // Contact.TYPE_OTHER.equals(map.get(Contact.EMAIL).get(1).getType())
    // ||
    // Contact.TYPE_WORK.equals(map.get(Contact.EMAIL).get(1).getType()));
    // //test value
    // assertTrue("Value does not match with expected",
    // "(212) 630-0319".equals(map.get(Contact.PHONE).get(0).getValue())
    // ||
    // "(212) 563-7440".equals(map.get(Contact.PHONE).get(0).getValue())
    // ||
    // "(212) 663-7440".equals(map.get(Contact.PHONE).get(0).getValue())
    // ||
    // "(212) 963-7440".equals(map.get(Contact.PHONE).get(0).getValue()));
    // assertTrue("Value does not match with expected",
    // "(212) 630-0319".equals(map.get(Contact.PHONE).get(1).getValue())
    // ||
    // "(212) 563-7440".equals(map.get(Contact.PHONE).get(1).getValue())
    // ||
    // "(212) 663-7440".equals(map.get(Contact.PHONE).get(1).getValue())
    // ||
    // "(212) 963-7440".equals(map.get(Contact.PHONE).get(1).getValue()));
    // assertTrue("Value does not match with expected",
    // "(212) 630-0319".equals(map.get(Contact.PHONE).get(2).getValue())
    // ||
    // "(212) 563-7440".equals(map.get(Contact.PHONE).get(2).getValue())
    // ||
    // "(212) 663-7440".equals(map.get(Contact.PHONE).get(2).getValue())
    // ||
    // "(212) 963-7440".equals(map.get(Contact.PHONE).get(2).getValue()));
    // assertTrue("Value does not match with expected",
    // "(212) 630-0319".equals(map.get(Contact.PHONE).get(3).getValue())
    // ||
    // "(212) 563-7440".equals(map.get(Contact.PHONE).get(3).getValue())
    // ||
    // "(212) 663-7440".equals(map.get(Contact.PHONE).get(3).getValue())
    // ||
    // "(212) 963-7440".equals(map.get(Contact.PHONE).get(3).getValue()));
    // assertTrue("Value does not match with expected",
    // "testuser@home.com".equals(map.get(Contact.EMAIL).get(0).getValue())
    // ||
    // "testuser@other.com".equals(map.get(Contact.EMAIL).get(0).getValue())
    // ||
    // "testuser@work.com".equals(map.get(Contact.EMAIL).get(0).getValue()));
    // assertTrue("Value does not match with expected",
    // "testuser@home.com".equals(map.get(Contact.EMAIL).get(1).getValue())
    // ||
    // "testuser@other.com".equals(map.get(Contact.EMAIL).get(1).getValue())
    // ||
    // "testuser@work.com".equals(map.get(Contact.EMAIL).get(1).getValue()));
    // }

    public void testDeleteContact() {
        PersistenceUtil.process(
                new ContactPersister.DeleteContactFunc(new Contact(1038, Contact.MediumType.EMAIL.name(),
                        Contact.TYPE_HOME, "acluser2@home.com")));
        PersistenceUtil.process(
                new ContactPersister.DeleteContactFunc(new Contact(1038, Contact.MediumType.PHONE.name(),
                        Contact.TYPE_HOME, "(212) 630-0319")));

        // Now we check that only one phone and email will remain after delete
        // and that match with the one non deleted

        @SuppressWarnings("unchecked")
        Map<String, List<Contact>> map = Contact.listToMediumMap((List<Contact>)
                PersistenceUtil.process(new ContactPersister.RetrieveContactFunc(1038)));
        assertEquals("There must be 2 keys for medium", 2, map.keySet().size());
        assertTrue("Phone must be one map key for entity 1", map.containsKey(Contact.MediumType.PHONE.name()));
        assertEquals("There must be 2 phones inside contacts for entity 1", 1,
                map.get(Contact.MediumType.PHONE.name()).size());
        assertTrue("Email must be one map key for entity 1", map.containsKey(Contact.MediumType.EMAIL.name()));
        assertEquals("There must be 2 emails inside contacts for entity 1", 1, map
                .get(Contact.MediumType.EMAIL.name()).size());
        // test entityId
        assertEquals("EntityId does not match with expected", 1038, map.get(Contact.MediumType.PHONE.name())
                .get(0).getEntityId());
        assertEquals("EntityId does not match with expected", 1038, map.get(Contact.MediumType.EMAIL.name())
                .get(0).getEntityId());
        // test medium
        assertEquals("Medium does not match with expected", Contact.MediumType.PHONE.name(),
                map.get(Contact.MediumType.PHONE.name()).get(0).getMedium());
        assertEquals("Medium does not match with expected", Contact.MediumType.EMAIL.name(),
                map.get(Contact.MediumType.EMAIL.name()).get(0).getMedium());
        // test type
        assertTrue("Type does not match with expected",
                Contact.TYPE_WORK.equals(map.get(Contact.MediumType.PHONE.name()).get(0).getType()));
        assertTrue("Type does not match with expected",
                Contact.TYPE_WORK.equals(map.get(Contact.MediumType.EMAIL.name()).get(0).getType()));
        // test value
        assertTrue("Value does not match with expected",
                "(212) 563-7440".equals(map.get(Contact.MediumType.PHONE.name()).get(0).getValue()));
        assertTrue("Value does not match with expected",
                "acluser2@work.com".equals(map.get(Contact.MediumType.EMAIL.name()).get(0).getValue()));
    }
}
