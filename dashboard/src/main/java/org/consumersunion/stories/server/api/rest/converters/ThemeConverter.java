package org.consumersunion.stories.server.api.rest.converters;

import java.util.Map;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.api.EndPoints;
import org.consumersunion.stories.common.shared.dto.ResourceLink;
import org.consumersunion.stories.common.shared.dto.ThemeResourceLinks;
import org.consumersunion.stories.common.shared.dto.ThemeResponse;
import org.consumersunion.stories.common.shared.model.Theme;
import org.consumersunion.stories.server.api.rest.util.ResourceLinksHelper;
import org.consumersunion.stories.server.persistence.ThemePersister;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

import static org.consumersunion.stories.common.shared.api.EndPoints.ORGANIZATIONS;
import static org.consumersunion.stories.common.shared.api.EndPoints.USERS;
import static org.consumersunion.stories.common.shared.api.EndPoints.endsWithId;

@Component
public class ThemeConverter extends AbstractConverter<Theme, ThemeResponse> {
    private final ResourceLinksHelper resourceLinksHelper;
    private final ThemePersister themePersister;

    @Inject
    ThemeConverter(
            ResourceLinksHelper resourceLinksHelper,
            ThemePersister themePersister) {
        this.resourceLinksHelper = resourceLinksHelper;
        this.themePersister = themePersister;
    }

    @Override
    public ThemeResponse convert(Theme input) {
        return ThemeResponse.builder()
                .withId(input.getId())
                .withCreatedOn(input.getCreated())
                .withUpdatedOn(input.getUpdated())
                .withTitle(input.getName())
                .isArchived(false)
                .withLinks(createLinks(input))
                .build();
    }

    private ThemeResourceLinks createLinks(Theme theme) {
        ThemeResourceLinks links = new ThemeResourceLinks();

        links.setOwner(resourceLinksHelper.replaceId(endsWithId(USERS), theme.getOwner()));
        links.setCreator(resourceLinksHelper.replaceId(endsWithId(USERS), theme.getOwner()));
        links.setOrganizations(
                resourceLinksHelper.replaceIntIds(endsWithId(ORGANIZATIONS),
                        themePersister.getOrganizationIds(theme.getId())));

        Map<Integer, Boolean> entities = themePersister.getEntities(theme.getId());
        links.setEntities(FluentIterable.from(entities.entrySet()).transform(
                new Function<Map.Entry<Integer, Boolean>, ResourceLink>() {
                    @Override
                    public ResourceLink apply(Map.Entry<Integer, Boolean> input) {
                        if (input.getValue()) {
                            return resourceLinksHelper.replaceId(endsWithId(EndPoints.QUESTIONNAIRES), input.getKey());
                        } else {
                            return resourceLinksHelper.replaceId(endsWithId(EndPoints.COLLECTIONS), input.getKey());
                        }
                    }
                }).toList());

        return links;
    }
}
