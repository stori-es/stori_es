package org.consumersunion.stories.server.index.elasticsearch;

import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.shared.model.Address;
import org.consumersunion.stories.server.index.Script;
import org.consumersunion.stories.server.index.ScriptFactory;
import org.consumersunion.stories.server.index.elasticsearch.scripts.UpdateAddressScript;
import org.consumersunion.stories.server.index.elasticsearch.scripts.UpdateAuthorScript;
import org.springframework.stereotype.Component;

@Component
public class ElasticsearchScriptFactory implements ScriptFactory {
    @Override
    public Script createUpdateAddressScript(Address address) {
        return new UpdateAddressScript(address);
    }

    @Override
    public Script createUpdateAuthorScript(ProfileSummary profileSummary) {
        return new UpdateAuthorScript(profileSummary);
    }
}
