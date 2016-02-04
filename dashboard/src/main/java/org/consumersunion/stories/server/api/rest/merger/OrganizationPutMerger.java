package org.consumersunion.stories.server.api.rest.merger;

import org.consumersunion.stories.common.shared.dto.post.OrganizationPut;
import org.consumersunion.stories.common.shared.model.Organization;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

@Component
public class OrganizationPutMerger extends AbstractMerger<Organization, OrganizationPut> {
    @Override
    public void merge(Organization entity, OrganizationPut dto) {
        entity.setName(dto.getName());

        if (!Strings.isNullOrEmpty(dto.getShortName())) {
            entity.setShortName(dto.getShortName());
        }
    }
}
