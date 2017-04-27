package org.consumersunion.stories.server.index;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.model.Address;

public interface ScriptFactory {
    Script createUpdateAddressScript(Address address);

    Script createUpdateAuthorScript(ProfileSummary profileSummary);
}
