package org.consumersunion.stories.server.business_logic;

import java.sql.Connection;
import java.util.List;

import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.entity.Contact;

public interface ContactService {
    /**
     * @see #getAllContacts(SystemEntity)
     */
    List<Contact> getAllContacts(int entityId);

    /**
     * Retrieves contacts associated to an {@link SystemEntity}. Effective operator must have READ privileges over the
     * target Entity. Contact data is not accessible via PUBLIC access.
     * <p/>
     */
    List<Contact> getAllContacts(SystemEntity systemEntity);

    /**
     * Saves contacts associated to an {@link SystemEntity}. Effective operator must have WRITE privileges oven the
     * target Entity.
     */
    List<Contact> saveContacts(int entityId, List<Contact> contacts);

    /**
     * Saves contacts associated to an {@link SystemEntity}. Effective operator must have WRITE privileges oven the
     * target Entity.
     */
    List<Contact> saveContacts(SystemEntity systemEntity, List<Contact> contacts);

    void saveContact(int entityId, String originalValue, Contact contact);

    /**
     * @see #getSocialContacts(SystemEntity)
     */
    List<Contact> getSocialContacts(int entityId);

    /**
     * Retrieves contacts associated to an {@link SystemEntity}. Effective operator must have READ privileges over the
     * target Entity. Contact data is not accessible via PUBLIC access.
     */
    List<Contact> getSocialContacts(SystemEntity systemEntity);

    /**
     * @see #getAddresses(SystemEntity)
     */
    List<Address> getAddresses(int entityId);

    /**
     * Retrieves the addresses associated with a {@link SystemEntity}. The target entity must be PUBLIC, owned by the
     * effective operator, or the effective operator must have READ privileges over the target.
     */
    List<Address> getAddresses(SystemEntity systemEntity);

    /**
     * @see #saveAddresses(SystemEntity, List)
     */
    List<Address> saveAddresses(int entityId, List<Address> addresses);

    /**
     * Updates addresses associated to an {@link SystemEntity}. Effective operator must have WRITE privileges over the
     * target Entity.
     */
    List<Address> saveAddresses(SystemEntity systemEntity, List<Address> addresses);

    String getPrimaryEmail(User user);

    List<Contact> retrieveEmails(User user);

    void saveContacts(List<Contact> existingContacts, Integer profileId, Connection conn);
}
