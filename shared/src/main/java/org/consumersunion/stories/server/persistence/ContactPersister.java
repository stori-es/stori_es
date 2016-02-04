package org.consumersunion.stories.server.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.common.shared.model.entity.ContactStatus;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class ContactPersister {
    private final PersistenceService persistenceService;

    @Inject
    ContactPersister(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public Contact retrieveEmailContact(int profileId, final String email) {
        return persistenceService.process(new ProcessFunc<Integer, Contact>(profileId) {
            @Override
            public Contact process() {
                try {
                    PreparedStatement retrieve = conn
                            .prepareStatement("SELECT entityId, medium, type, value, status FROM contact "
                                    + "WHERE entityId = ? AND value = ?");
                    retrieve.setInt(1, input);
                    retrieve.setString(2, email);

                    ResultSet rs = retrieve.executeQuery();
                    if (rs.next()) {
                        return new Contact(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                                ContactStatus.valueOf(rs.getString(5)));
                    }

                    return null;
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    public List<Contact> retrieveEmails(User user) {
        return persistenceService.process(new ProcessFunc<User, List<Contact>>(user) {
            @Override
            public List<Contact> process() {
                try {
                    PreparedStatement select = conn.prepareStatement(
                            "SELECT c.entityId, c.medium, c.type, c.value, c.status FROM contact c " +
                                    "JOIN profile p ON p.id = c.entityId " +
                                    "JOIN user u ON p.user = u.id " +
                                    "WHERE c.medium = 'EMAIL' AND u.id = ? " +
                                    "GROUP BY c.value;");

                    select.setInt(1, input.getId());
                    ResultSet rs = select.executeQuery();

                    List<Contact> emails = Lists.newArrayList();
                    while (rs.next()) {
                        Contact contact = new Contact(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                                ContactStatus.valueOf(rs.getString(5)));
                        emails.add(contact);
                    }

                    return emails;
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    public void setContactStatus(int profileId, final String email, final ContactStatus status) {
        persistenceService.process(new ProcessFunc<Integer, Void>(profileId) {
            @Override
            public Void process() {
                try {
                    PreparedStatement update = conn.prepareStatement("UPDATE contact " +
                            "SET status='" + status.name()
                            + "' WHERE entityId = ? AND value = ?");
                    update.setInt(1, input);
                    update.setString(2, email);

                    update.executeUpdate();

                    return null;
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    public List<Contact> retrieveContacts(int entityId) {
        return persistenceService.process(new ContactPersister.RetrieveContactFunc(entityId));
    }

    public List<Contact> retrieveSocialContacts(int entityId) {
        return persistenceService.process(new ContactPersister.RetrieveSocialContactFunc(entityId));
    }

    public List<Contact> saveContacts(List<Contact> contacts, int entityId) {
        return persistenceService.process(new ContactPersister.SaveContactsFunc(contacts, entityId));
    }

    public List<Contact> saveContacts(List<Contact> contacts, Integer entityId, Connection conn) {
        return persistenceService.process(conn, new ContactPersister.SaveContactsFunc(contacts, entityId));
    }

    public List<Address> retrieveAddress(int entityId) {
        return persistenceService.process(new ContactPersister.RetrieveAddress(entityId));
    }

    public List<Address> retrieveAddress(Integer entityId, Connection conn) {
        return persistenceService.process(conn, new ContactPersister.RetrieveAddress(entityId));
    }

    public List<Address> saveAddresses(List<Address> addresses, int entityId) {
        return persistenceService.process(new UpdateAddressForEntity(addresses, entityId));
    }

    public void saveContact(
            final int entityId,
            final String originalValue,
            Contact contact) {
        contact.setEntityId(entityId);
        persistenceService.process(new ProcessFunc<Contact, Contact>(contact) {
            @Override
            public Contact process() {
                try {
                    boolean isNew = originalValue == null;
                    PreparedStatement statement;

                    if (isNew) {
                        List<Integer> usedIndexes = ContactPersister.getUsedIdx(conn, entityId);

                        statement = conn.prepareStatement(
                                "INSERT INTO contact (type, medium, value, status, entityId, idx) VALUES(?,?,?,?,?,?)");
                        statement.setString(4, input.getStatus().name());
                        statement.setInt(6, ContactPersister.getAvailableIndex(0, usedIndexes));
                    } else {
                        statement = conn.prepareStatement("UPDATE contact " +
                                "SET type=?, medium=?, value=?, status=? " +
                                "WHERE entityId = ? AND value = ?");
                        statement.setString(4, ContactStatus.UNVERIFIED.name());
                        statement.setString(6, originalValue);
                    }

                    statement.setString(1, input.getType());
                    statement.setString(2, input.getMedium());
                    statement.setString(3, input.getValue());
                    statement.setInt(5, input.getEntityId());

                    statement.executeUpdate();

                    input.setStatus(ContactStatus.UNVERIFIED);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return input;
            }
        });
    }

    public String retrievePrimaryEmail(int profile) {
        return persistenceService.process(new ProcessFunc<Integer, String>(profile) {
            @Override
            public String process() {
                try {
                    PreparedStatement select =
                            conn.prepareStatement("SELECT value AS email FROM contact c WHERE medium='EMAIL' " +
                                    "AND status LIKE '%VERIFIED' " +
                                    "AND c.entityId=? " +
                                    "ORDER BY FIELD(status, 'VERIFIED', 'UNVERIFIED'), FIELD(type, 'Home', 'Mobile', " +
                                    "'Other')");

                    select.setInt(1, input);

                    ResultSet resultSet = select.executeQuery();
                    if (resultSet.next()) {
                        return resultSet.getString(1);
                    }

                    return null;
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    public void updateEmailContactStatus(Map<String, ContactStatus> contactStatus) {
        persistenceService.process(new ProcessFunc<Map<String, ContactStatus>, Void>(contactStatus) {
            @Override
            public Void process() {
                try {
                    PreparedStatement update = conn.prepareStatement("UPDATE contact " +
                            "SET status=? WHERE value=?");
                    for (Map.Entry<String, ContactStatus> entry : input.entrySet()) {
                        update.setString(1, entry.getValue().name());
                        update.setString(2, entry.getKey());

                        update.addBatch();
                    }

                    update.executeBatch();

                    return null;
                } catch (SQLException e) {
                    throw new GeneralException(e);
                }
            }
        });
    }

    private static int getAvailableIndex(int index, List<Integer> usedIndexes) {
        while (usedIndexes.contains(index)) {
            index++;
        }

        return index;
    }

    private static List<Integer> getUsedIdx(Connection conn, int entityId) throws SQLException {
        PreparedStatement usedIndexesStatement = conn.prepareStatement(
                "SELECT idx FROM contact WHERE entityId = ? ORDER BY idx");
        usedIndexesStatement.setInt(1, entityId);
        ResultSet indexesResultSet = usedIndexesStatement.executeQuery();

        List<Integer> usedIndexes = Lists.newArrayList();
        if (indexesResultSet.next()) {
            do {
                usedIndexes.add(indexesResultSet.getInt(1));
            } while (indexesResultSet.next());
        }
        return usedIndexes;
    }

    public static class UpdateAddress extends ProcessFunc<List<Address>, List<Address>> {
        public UpdateAddress(List<Address> address) {
            super(address);
        }

        @Override
        public List<Address> process() {
            try {
                if (!input.isEmpty()) {
                    final PreparedStatement update = conn
                            .prepareStatement("UPDATE address " +
                                    "SET address1=?, address2=?, city=?, state=?, country=?, postalCode=?, " +
                                    "latitude=?, longitude=?, geoCodeStatus=?, geoCodeProvider=?, geoCodeDate=? " +
                                    "WHERE entity=? AND idx=?");
                    for (Address address : input) {
                        if (address.getAddress1() != null) {
                            update.setString(1, address.getAddress1());
                        } else {
                            update.setNull(1, Types.VARCHAR);
                        }

                        if (address.getAddress2() != null) {
                            update.setString(2, address.getAddress2());
                        } else {
                            update.setNull(2, Types.VARCHAR);
                        }

                        if (address.getCity() != null) {
                            update.setString(3, address.getCity());
                        } else {
                            update.setNull(3, Types.VARCHAR);
                        }

                        if (address.getState() != null) {
                            update.setString(4, address.getState());
                        } else {
                            update.setNull(4, Types.VARCHAR);
                        }

                        if (address.getCountry() != null) {
                            update.setString(5, address.getCountry());
                        } else {
                            update.setNull(5, Types.VARCHAR);
                        }

                        if (address.getCountry() != null) {
                            update.setString(6, address.getPostalCode());
                        } else {
                            update.setNull(6, Types.VARCHAR);
                        }

                        if (address.getLatitude() != null) {
                            update.setBigDecimal(7, address.getLatitude());
                        } else {
                            update.setNull(7, Types.BIGINT);
                        }

                        if (address.getLongitude() != null) {
                            update.setBigDecimal(8, address.getLongitude());
                        } else {
                            update.setNull(8, Types.BIGINT);
                        }

                        if (address.getGeoCodeStatus() != null) {
                            update.setString(9, address.getGeoCodeStatus());
                        } else {
                            update.setNull(9, Types.VARCHAR);
                        }

                        if (address.getGeoCodeProvider() != null) {
                            update.setString(10, address.getGeoCodeProvider());
                        } else {
                            update.setNull(10, Types.VARCHAR);
                        }

                        if (address.getGeoCodeDate() != null) {
                            update.setTimestamp(11, new Timestamp(address.getGeoCodeDate().getTime()));
                        } else {
                            update.setNull(11, Types.DATE);
                        }

                        int idx = address.getIdx();
                        update.setInt(12, address.getEntity());
                        update.setInt(13, idx);

                        update.addBatch();
                    }

                    int[] updateCount = update.executeBatch();
                    if (updateCount.length != input.size()) {
                        throw new GeneralException("Unexpected update count on update Address: " + updateCount.length);
                    }
                }
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
            return input;
        }
    }

    public static class DeleteAddress extends ProcessFunc<Address, Address> {
        public DeleteAddress(final Address input) {
            super(input);
        }

        @Override
        public Address process() {
            try {
                PreparedStatement delete = conn.prepareStatement(
                        "DELETE FROM address WHERE entity = ? AND relation = ?");
                delete.setInt(1, input.getEntity());
                delete.setString(2, input.getRelation());

                int deleteCount = delete.executeUpdate();
                if (deleteCount != 1) {
                    throw new GeneralException("Unexpected delete address: " + deleteCount);
                }
                return input;
            } catch (final SQLException ex) {
                throw new GeneralException(ex);
            }
        }
    }

    public static class RetrieveAddress extends ProcessFunc<Integer, List<Address>> {
        public RetrieveAddress(Integer entityId) {
            super(entityId);
        }

        @Override
        public List<Address> process() {
            List<Address> list = new ArrayList<Address>();
            try {
                PreparedStatement select = conn
                        .prepareStatement("SELECT entity, relation, address1, address2, city, state, country,  " +
                                "postalCode, latitude, longitude, geoCodeStatus, geoCodeProvider, geoCodeDate "
                                + "FROM address WHERE entity = ? ORDER BY entity, relation, idx");
                select.setInt(1, input);
                ResultSet rs = select.executeQuery();

                if (rs.next()) {
                    do {
                        Address address = new Address(rs.getInt(1));
                        address.setRelation(rs.getString(2));
                        address.setRelation(rs.getString(2));
                        address.setAddress1(rs.getString(3));
                        address.setAddress2(rs.getString(4));
                        address.setCity(rs.getString(5));
                        address.setState(rs.getString(6));
                        address.setCountry(rs.getString(7));
                        address.setPostalCode(rs.getString(8));
                        address.setLatitude(rs.getBigDecimal(9));
                        address.setLongitude(rs.getBigDecimal(10));
                        address.setGeoCodeStatus(rs.getString(11));
                        address.setGeoCodeProvider(rs.getString(12));
                        address.setGeoCodeDate(rs.getDate(13));
                        list.add(address);
                    } while (rs.next());
                }
            } catch (SQLException ex) {
                throw new GeneralException(ex);
            }
            return list;
        }
    }

    public static class RetrieveRefreshAddresses extends ProcessFunc<Integer, List<Address>> {
        private final Integer nbDays;
        private final Integer maxResults;

        public RetrieveRefreshAddresses(Integer nbDays, Integer maxResults) {
            super(nbDays);

            this.nbDays = nbDays;
            this.maxResults = maxResults;
        }

        @Override
        public List<Address> process() {
            List<Address> list = Lists.newArrayList();

            try {
                PreparedStatement select = conn
                        .prepareStatement("SELECT entity, relation, address1, address2, city, state, country, " +
                                "postalCode, latitude, longitude, geoCodeStatus, geoCodeProvider, geoCodeDate, idx " +
                                "FROM address WHERE geoCodeDate < DATE_ADD(NOW(), INTERVAL -? DAY) " +
                                "ORDER BY geoCodeDate ASC LIMIT ?");
                select.setInt(1, nbDays);
                select.setInt(2, maxResults);
                ResultSet rs = select.executeQuery();

                while (rs.next()) {
                    final Address address = new Address(rs.getInt(1));
                    address.setRelation(rs.getString(2));
                    address.setRelation(rs.getString(2));
                    address.setAddress1(rs.getString(3));
                    address.setAddress2(rs.getString(4));
                    address.setCity(rs.getString(5));
                    address.setState(rs.getString(6));
                    address.setCountry(rs.getString(7));
                    address.setPostalCode(rs.getString(8));
                    address.setLatitude(rs.getBigDecimal(9));
                    address.setLongitude(rs.getBigDecimal(10));
                    address.setGeoCodeStatus(rs.getString(11));
                    address.setGeoCodeProvider(rs.getString(12));
                    address.setGeoCodeDate(rs.getDate(13));
                    address.setIdx(rs.getInt(14));
                    list.add(address);
                }
            } catch (SQLException ex) {
                throw new GeneralException(ex);
            }

            return list;
        }
    }

    public static class SaveContactsFunc extends ProcessFunc<List<Contact>, List<Contact>> {
        private final Integer entityId;

        public SaveContactsFunc(List<Contact> input) {
            this(input, input.get(0).getEntityId());
        }

        public SaveContactsFunc(List<Contact> input, Integer entityId) {
            super(input);

            this.entityId = entityId;
        }

        @Override
        public List<Contact> process() {
            List<Contact> savedContacts = PersistenceUtil.process(conn, new RetrieveContactFunc(entityId));
            for (Contact contact : input) {
                boolean exists = false;
                for (Contact savedContact : savedContacts) {
                    if (!Contact.SOCIAL.equals(savedContact.getType()) && contact.getValue().equals(
                            savedContact.getValue())) {
                        exists = true;
                        contact.setStatus(savedContact.getStatus());
                        break;
                    }
                }

                if (!exists) {
                    contact.setStatus(ContactStatus.UNVERIFIED);
                }
            }

            try {
                PreparedStatement delete = conn.prepareStatement(
                        "DELETE FROM contact WHERE entityId = ? AND type <> ?");

                delete.setInt(1, entityId);
                delete.setString(2, Contact.SOCIAL);
                delete.executeUpdate();
                conn.commit();

                if (!input.isEmpty()) {
                    PreparedStatement usedIndexesStatement =
                            conn.prepareStatement("SELECT idx FROM contact WHERE entityId = ? ORDER BY idx");
                    usedIndexesStatement.setInt(1, entityId);
                    ResultSet indexesResultSet = usedIndexesStatement.executeQuery();

                    List<Integer> usedIndexes = Lists.newArrayList();
                    if (indexesResultSet.next()) {
                        do {
                            usedIndexes.add(indexesResultSet.getInt(1));
                        } while (indexesResultSet.next());
                    }

                    PreparedStatement insert = conn.prepareStatement(
                            "INSERT INTO contact (entityId, idx, type, medium, value, status) VALUES(?,?,?,?,?,?)");

                    int index = 0;
                    for (Contact contact : input) {
                        index = getAvailableIndex(index, usedIndexes);
                        insert.setInt(1, entityId);
                        insert.setInt(2, index++);
                        insert.setString(3, contact.getType());
                        insert.setString(4, contact.getMedium());
                        insert.setString(5, contact.getValue());
                        insert.setString(6, contact.getStatus().name());
                        insert.addBatch();
                    }

                    insert.executeBatch();
                }
                return input;
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public static class SaveSocialContactFunc extends ProcessFunc<List<Contact>, List<Contact>> {
        private final int entityId;

        public SaveSocialContactFunc(List<Contact> input, int entityId) {
            super(input);

            this.entityId = entityId;
        }

        @Override
        public List<Contact> process() {
            try {
                PreparedStatement delete = conn.prepareStatement(
                        "DELETE FROM contact WHERE entityId = ? AND type = ?");
                delete.setInt(1, entityId);
                delete.setString(2, Contact.SOCIAL);
                delete.executeUpdate();
                conn.commit();

                if (input != null && !input.isEmpty()) {
                    List<Integer> usedIndexes = getUsedIdx(conn, entityId);

                    PreparedStatement insert = conn.prepareStatement(
                            "INSERT INTO contact (entityId, idx, type, medium, value) VALUES(?,?,?,?,?)");

                    int index = 0;
                    for (Contact contact : input) {
                        index = getAvailableIndex(index, usedIndexes);
                        insert.setInt(1, entityId);
                        insert.setInt(2, index++);
                        insert.setString(3, contact.getType());
                        insert.setString(4, contact.getMedium());
                        insert.setString(5, contact.getValue());
                        insert.addBatch();
                    }
                    insert.executeBatch();
                }

                return input;
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public static class RetrieveContactFunc extends ProcessFunc<Integer, List<Contact>> {
        public RetrieveContactFunc(Integer input) {
            super(input);
        }

        @Override
        public List<Contact> process() {
            try {
                PreparedStatement retrieve = conn
                        .prepareStatement("SELECT entityId, medium, type, value, status FROM contact "
                                + "WHERE entityId = ? ORDER BY idx");
                retrieve.setInt(1, input);

                ResultSet rs = retrieve.executeQuery();
                List<Contact> contacts = new ArrayList<Contact>();
                if (rs.next()) {
                    do {
                        contacts.add(new Contact(rs.getInt(1), rs.getString(2),
                                rs.getString(3), rs.getString(4), ContactStatus.valueOf(rs.getString(5))));
                    } while (rs.next());
                }
                return contacts;
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public static class RetrieveSocialContactFunc extends ProcessFunc<Integer, List<Contact>> {
        public RetrieveSocialContactFunc(Integer input) {
            super(input);
        }

        @Override
        public List<Contact> process() {
            try {
                PreparedStatement retrieve = conn.prepareStatement(
                        "SELECT entityId, medium, type, value FROM contact "
                                + "WHERE entityId = ? AND type = ? ORDER BY idx");
                retrieve.setInt(1, input);
                retrieve.setString(2, Contact.SOCIAL);

                ResultSet rs = retrieve.executeQuery();
                List<Contact> contacts = new ArrayList<Contact>();

                if (rs.next()) {
                    do {
                        contacts.add(new Contact(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
                    } while (rs.next());
                }

                return contacts;
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public static class DeleteContactFunc extends ProcessFunc<Contact, Contact> {
        public DeleteContactFunc(Contact input) {
            super(input);
        }

        @Override
        public Contact process() {
            try {
                PreparedStatement delete = conn
                        .prepareStatement("DELETE FROM contact WHERE entityId = ? AND type =? "
                                + "AND medium = ? AND value = ?");
                delete.setInt(1, input.getEntityId());
                delete.setString(2, input.getType());
                delete.setString(3, input.getMedium());
                delete.setString(4, input.getValue());

                int deleteCount = delete.executeUpdate();
                if (deleteCount != 1) {
                    throw new GeneralException("Unexpected delete count: " + deleteCount);
                }

                return input;
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
        }
    }

    public static class UpdateAddressForEntity extends ProcessFunc<List<Address>, List<Address>> {
        private final Integer entityId;

        public UpdateAddressForEntity(List<Address> address) {
            this(address, address.get(0).getEntity());
        }

        public UpdateAddressForEntity(List<Address> addresses, Integer entityId) {
            super(addresses);

            this.entityId = entityId;
        }

        @Override
        public List<Address> process() {
            try {
                PreparedStatement delete = conn.prepareStatement("DELETE FROM address WHERE entity = ?");
                delete.setInt(1, entityId);
                delete.executeUpdate();

                if (!input.isEmpty()) {
                    PreparedStatement insert = conn
                            .prepareStatement("INSERT INTO address (entity, relation, idx, address1, address2, city, " +
                                    "state, country,  postalCode, latitude, longitude, geoCodeStatus, " +
                                    "geoCodeProvider, geoCodeDate) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    for (int i = 0; i < input.size(); i++) {
                        insert.setInt(1, entityId);
                        insert.setString(2, input.get(i).getRelation());
                        insert.setInt(3, i);

                        if (input.get(i).getAddress1() != null) {
                            insert.setString(4, input.get(i).getAddress1());
                        } else {
                            insert.setNull(4, Types.VARCHAR);
                        }

                        if (input.get(i).getAddress2() != null) {
                            insert.setString(5, input.get(i).getAddress2());
                        } else {
                            insert.setNull(5, Types.VARCHAR);
                        }

                        if (input.get(i).getCity() != null) {
                            insert.setString(6, input.get(i).getCity());
                        } else {
                            insert.setNull(6, Types.VARCHAR);
                        }

                        if (input.get(i).getState() != null) {
                            insert.setString(7, input.get(i).getState());
                        } else {
                            insert.setNull(7, Types.VARCHAR);
                        }

                        if (input.get(i).getCountry() != null) {
                            insert.setString(8, input.get(i).getCountry());
                        } else {
                            insert.setNull(8, Types.VARCHAR);
                        }

                        if (input.get(i).getCountry() != null) {
                            insert.setString(9, input.get(i).getPostalCode());
                        } else {
                            insert.setNull(9, Types.VARCHAR);
                        }

                        if (input.get(i).getLatitude() != null) {
                            insert.setBigDecimal(10, input.get(i).getLatitude());
                        } else {
                            insert.setNull(10, Types.BIGINT);
                        }

                        if (input.get(i).getLongitude() != null) {
                            insert.setBigDecimal(11, input.get(i).getLongitude());
                        } else {
                            insert.setNull(11, Types.BIGINT);
                        }

                        if (input.get(i).getGeoCodeStatus() != null) {
                            insert.setString(12, input.get(i).getGeoCodeStatus());
                        } else {
                            insert.setNull(12, Types.VARCHAR);
                        }

                        if (input.get(i).getGeoCodeProvider() != null) {
                            insert.setString(13, input.get(i).getGeoCodeProvider());
                        } else {
                            insert.setNull(13, Types.VARCHAR);
                        }

                        if (input.get(i).getGeoCodeDate() != null) {
                            insert.setTimestamp(14, new Timestamp(input.get(i).getGeoCodeDate().getTime()));
                        } else {
                            insert.setNull(14, Types.DATE);
                        }

                        insert.addBatch();
                    }

                    int[] insertCount = insert.executeBatch();
                    if (insertCount.length != input.size()) {
                        throw new GeneralException("Unexpected insert count on update Address: " + insertCount.length);
                    }
                }
            } catch (SQLException e) {
                throw new GeneralException(e);
            }
            return input;
        }
    }
}
