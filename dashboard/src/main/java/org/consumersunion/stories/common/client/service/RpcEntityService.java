package org.consumersunion.stories.common.client.service;

import java.util.List;
import java.util.Set;

import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.AddressResponse;
import org.consumersunion.stories.common.client.service.response.ContactResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.common.shared.model.NotificationTrigger;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.entity.Contact;
import org.consumersunion.stories.common.shared.model.entity.Entity;
import org.consumersunion.stories.server.business_logic.ContactService;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Service for entity
 *
 * @author Machin
 */
@RemoteServiceRelativePath("service/entity")
public interface RpcEntityService extends RemoteService {
    /**
     * Updates tags associated to an {@link Entity}. Effective operator must
     * have WRITE authorizations over the target Entity.
     */
    ActionResponse setTags(final SystemEntity entity, final Set<String> tags);

    /**
     * Updates autotags associated to an {@link Entity}. Effective operator must
     * have WRITE authorizations over the target Entity.
     */
    ActionResponse updateAutoTags(SystemEntity entity, Set<String> tags);

    /**
     * Retrieves the tags associated with an {@link Entity}. The target entity
     * must be PUBLIC, owned by the effective operator, or the effective
     * operator must have READ privileges over the target.
     */
    DatumResponse<Set<String>> getTags(final SystemEntity entity);

    /**
     * Retrieves the autotags associated with an {@link SystemEntity}. The target entity
     * must be PUBLIC or the effective operator must have READ privileges over the
     * target. Notice that ownership does not figure in with the {@link SystemEntity}
     * like it does with {@link #getTags(SystemEntity)}.
     */
    DatumResponse<Set<String>> getAutoTags(final SystemEntity entity);

    /**
     * Retrieves the tags associated with an {@link Entity}. The target entity
     * must be PUBLIC, owned by the effective operator, or the effective
     * operator must have READ privileges over the target.
     */
    DatumResponse<Set<String>> getTags();

    /**
     * @see ContactService#getAddresses(int)
     */
    AddressResponse getAddress(int entityId);

    /**
     * Updates addresses associated to an {@link Entity}. Effective operator
     * must have WRITE privileges over the target Entity.
     * <p/>
     * This version is used, and required, when updating a Person associated
     * address. This allows the the index to be kept in sync.
     */
    AddressResponse updateAddress(int idx, List<Address> address, int entityId);

    /**
     * @see ContactService#saveAddresses(int, List)
     */
    AddressResponse updateAddress(List<Address> addresses, int entityId);

    /**
     * @see ContactService#saveContacts(int, List)
     */
    ContactResponse saveContacts(int entityId, List<Contact> contacts);

    /**
     * @see ContactService#saveContact(int, String, Contact)
     */
    ContactResponse saveContact(int entityId, String originalValue, Contact contact);

    /**
     * @see ContactService#saveContacts(int, List)
     */
    ContactResponse saveSocialContacts(int entityId, List<Contact> contacts);

    /**
     * @see ContactService#getAllContacts(int)
     */
    ContactResponse retrieveContacts(int entityId);

    /**
     * @see ContactService#getSocialContacts(int)
     */
    ContactResponse retrieveSocialContacts(int entityId);

    ActionResponse addTags(Set<Integer> entityIds, Set<String> tags);

    ActionResponse startWatching(int entityId, NotificationTrigger trigger);

    ActionResponse stopWatching(int entityId, NotificationTrigger trigger);
}
