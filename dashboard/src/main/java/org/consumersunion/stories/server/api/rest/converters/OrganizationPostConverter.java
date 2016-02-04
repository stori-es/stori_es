package org.consumersunion.stories.server.api.rest.converters;

import org.consumersunion.stories.common.shared.dto.post.OrganizationPost;
import org.consumersunion.stories.common.shared.model.Organization;
import org.springframework.stereotype.Component;

import com.google.common.base.Converter;

@Component
public class OrganizationPostConverter extends Converter<OrganizationPost, Organization> {
    @Override
    protected Organization doForward(OrganizationPost organizationPost) {
        Organization organization = new Organization();

        organization.setName(organizationPost.getName());
        organization.setShortName(organizationPost.getShortName());
        organization.setDefaultTheme(organizationPost.getDefaultThemeId());

        return organization;
    }

    @Override
    protected OrganizationPost doBackward(Organization organization) {
        throw new UnsupportedOperationException();
    }
}
