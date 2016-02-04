package org.consumersunion.stories.server.api.rest.converters;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.dto.PermissionResponse;
import org.consumersunion.stories.common.shared.dto.PermissionRessourceLinks;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireBase;
import org.consumersunion.stories.server.api.rest.util.ResourceLinksHelper;
import org.consumersunion.stories.server.business_logic.SystemEntityService;
import org.springframework.stereotype.Component;

import static org.consumersunion.stories.common.shared.api.EndPoints.DOCUMENTS;
import static org.consumersunion.stories.common.shared.api.EndPoints.ORGANIZATIONS;
import static org.consumersunion.stories.common.shared.api.EndPoints.PROFILES;
import static org.consumersunion.stories.common.shared.api.EndPoints.endsWithId;

@Component
public class PermissionsConverter extends AbstractConverter<Document, PermissionResponse> {
    private final ResourceLinksHelper resourceLinksHelper;
    private final SystemEntityService systemEntityService;

    @Inject
    PermissionsConverter(
            ResourceLinksHelper resourceLinksHelper,
            SystemEntityService systemEntityService) {
        this.resourceLinksHelper = resourceLinksHelper;
        this.systemEntityService = systemEntityService;
    }

    @Override
    public PermissionResponse convert(Document input) {
        return PermissionResponse.builder()
                .withId(input.getId())
                .withTitle(input.getTitle())
                .withCreatedOn(input.getCreated())
                .withUpdatedOn(input.getUpdated())
                .withLinks(createLinks(input))
                .isArchived(false)
                .build();
    }

    private PermissionRessourceLinks createLinks(Document input) {
        PermissionRessourceLinks links = new PermissionRessourceLinks();

        links.setOwner(resourceLinksHelper.replaceId(endsWithId(PROFILES), input.getOwner()));
        links.setCreator(resourceLinksHelper.replaceId(endsWithId(PROFILES), input.getPrimaryAuthor()));
        links.setContent(resourceLinksHelper.replaceId(endsWithId(DOCUMENTS), input.getId()));

        SystemEntity systemEntity = systemEntityService.getSystemEntity(input.getSystemEntity());
        if (systemEntity instanceof QuestionnaireBase) {
            links.setOrganization(resourceLinksHelper.replaceId(endsWithId(ORGANIZATIONS), systemEntity.getOwner()));
        } else if (systemEntity instanceof Organization) {
            links.setOrganization(resourceLinksHelper.replaceId(endsWithId(ORGANIZATIONS), systemEntity.getId()));
        }

        return links;
    }
}
