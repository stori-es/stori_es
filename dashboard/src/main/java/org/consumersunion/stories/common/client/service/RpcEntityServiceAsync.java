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

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of {@link RpcEntityService}.
 *
 * @author Machin
 */
public interface RpcEntityServiceAsync {
    void setTags(SystemEntity entity, Set<String> tags, AsyncCallback<ActionResponse> callback);

    void getTags(SystemEntity entity, AsyncCallback<DatumResponse<Set<String>>> callback);

    void getTags(AsyncCallback<DatumResponse<Set<String>>> callback);

    void getAddress(int entityId, AsyncCallback<AddressResponse> callback);

    void updateAddress(int idx, List<Address> address, int entityId, AsyncCallback<AddressResponse> callback);

    void updateAddress(List<Address> addresses, int entityId, AsyncCallback<AddressResponse> callback);

    void saveContact(int entityId, String originalValue, Contact contact, AsyncCallback<ContactResponse> callback);

    void saveContacts(int entityId, List<Contact> contacts, AsyncCallback<ContactResponse> callback);

    void saveSocialContacts(int entityId, List<Contact> contacts, AsyncCallback<ContactResponse> callback);

    void retrieveContacts(int entityId, AsyncCallback<ContactResponse> callback);

    void retrieveSocialContacts(int entityId, AsyncCallback<ContactResponse> callback);

    void addTags(Set<Integer> entityIds, Set<String> tags, AsyncCallback<ActionResponse> callback);

    void updateAutoTags(SystemEntity entity, Set<String> tags, AsyncCallback<ActionResponse> async);

    void getAutoTags(final SystemEntity entity, AsyncCallback<DatumResponse<Set<String>>> async);

    void startWatching(int entityId, NotificationTrigger trigger, AsyncCallback<ActionResponse> callback);

    void stopWatching(int entityId, NotificationTrigger trigger, AsyncCallback<ActionResponse> callback);
}
