package org.consumersunion.stories.server.api.rest.converters;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.dto.ApiBlock;
import org.consumersunion.stories.common.shared.dto.DocumentResourceLinks;
import org.consumersunion.stories.common.shared.dto.DocumentResponse;
import org.consumersunion.stories.common.shared.dto.ResourceLink;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.document.Block;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.model.questionnaire.Answer;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionBase;
import org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.server.api.rest.util.ResourceLinksHelper;
import org.consumersunion.stories.server.business_logic.SystemEntityService;
import org.consumersunion.stories.server.persistence.AnswerSetPersister;
import org.consumersunion.stories.server.persistence.QuestionnaireI15dPersister;
import org.springframework.stereotype.Component;

import com.google.common.base.Converter;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import static org.consumersunion.stories.common.shared.api.EndPoints.COLLECTIONS;
import static org.consumersunion.stories.common.shared.api.EndPoints.DOCUMENTS;
import static org.consumersunion.stories.common.shared.api.EndPoints.ORGANIZATIONS;
import static org.consumersunion.stories.common.shared.api.EndPoints.PROFILES;
import static org.consumersunion.stories.common.shared.api.EndPoints.QUESTIONNAIRES;
import static org.consumersunion.stories.common.shared.api.EndPoints.STORIES;
import static org.consumersunion.stories.common.shared.api.EndPoints.USERS;
import static org.consumersunion.stories.common.shared.api.EndPoints.endsWithId;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.ANSWER_SET;

@Component
public class DocumentResponseConverter extends Converter<DocumentResponse, Document> {
    private final DocumentTypeConverter documentTypeConverter;
    private final SystemEntityService systemEntityService;
    private final ResourceLinksHelper resourceLinksHelper;
    private final ApiBlockConverter apiBlockConverter;
    private final ApiLocaleConverter apiLocaleConverter;
    private final AnswerSetPersister answerSetPersister;
    private final QuestionnaireI15dPersister questionnaireI15dPersister;

    @Inject
    DocumentResponseConverter(
            DocumentTypeConverter documentTypeConverter,
            SystemEntityService systemEntityService,
            ResourceLinksHelper resourceLinksHelper,
            ApiBlockConverter apiBlockConverter,
            ApiLocaleConverter apiLocaleConverter,
            AnswerSetPersister answerSetPersister,
            QuestionnaireI15dPersister questionnaireI15dPersister) {
        this.documentTypeConverter = documentTypeConverter;
        this.systemEntityService = systemEntityService;
        this.resourceLinksHelper = resourceLinksHelper;
        this.apiBlockConverter = apiBlockConverter;
        this.apiLocaleConverter = apiLocaleConverter;
        this.answerSetPersister = answerSetPersister;
        this.questionnaireI15dPersister = questionnaireI15dPersister;
    }

    @Override
    protected Document doForward(DocumentResponse documentResponse) {
        // We actually don't have a need to convert DocumentResponse -> Document
        throw new UnsupportedOperationException();
    }

    @Override
    protected DocumentResponse doBackward(Document document) {
        List<ApiBlock> blocks;
        if (ANSWER_SET.equals(document.getSystemEntityRelation())) {
            blocks = createBlocks(document);
        } else {
            blocks = Lists.newArrayList(apiBlockConverter.reverse().convertAll(document.getBlocks()));
        }

        return new DocumentResponse.Builder()
                .withId(document.getId())
                .withCreatedOn(document.getCreated())
                .withUpdatedOn(document.getUpdated())
                .withTitle(document.getTitle())
                .withDocumentType(documentTypeConverter.doBackward(document.getSystemEntityRelation()))
                .withLocale(apiLocaleConverter.doBackward(document.getLocale()))
                .withLinks(generateLinks(document))
                .withBlocks(blocks)
                .build();
    }

    private List<ApiBlock> createBlocks(Document document) {
        AnswerSet answerSet = answerSetPersister.get(document.getId());
        QuestionnaireI15d questionnaire = questionnaireI15dPersister.get(answerSet.getQuestionnaire());

        List<ApiBlock> blocks = Lists.newArrayList();
        for (Block block : questionnaire.getBlocks()) {
            if (block instanceof QuestionBase) {
                Answer answer = answerSet.getAnswerByLabel(((QuestionBase) block).getLabel());
                if (answer != null) {
                    ApiBlock apiBlock = apiBlockConverter.doBackward(block);
                    apiBlock.setValue(Joiner.on(',').join(answer.getReportValues()));
                    blocks.add(apiBlock);
                }
            }
        }

        return blocks;
    }

    private DocumentResourceLinks generateLinks(Document input) {
        DocumentResourceLinks documentResourceLinks = new DocumentResourceLinks();

        if (SystemEntityRelation.ATTACHMENT.equals(input.getSystemEntityRelation())) {
            documentResourceLinks.setSource(new ResourceLink(input.getPermalink()));
        }

        addEntityLink(input, documentResourceLinks);

        documentResourceLinks.setOwner(
                resourceLinksHelper.replaceId(endsWithId(PROFILES), input.getOwner()));

        return documentResourceLinks;
    }

    private void addEntityLink(Document input, DocumentResourceLinks documentResourceLinks) {
        String endpoint = null;

        SystemEntity entity = systemEntityService.getSystemEntity(input.getSystemEntity());
        if (entity instanceof Story) {
            endpoint = endsWithId(STORIES);
        } else if (entity instanceof Questionnaire) {
            endpoint = endsWithId(QUESTIONNAIRES);
        } else if (entity instanceof Collection) {
            endpoint = endsWithId(COLLECTIONS);
        } else if (entity instanceof Document) {
            endpoint = endsWithId(DOCUMENTS);
        } else if (entity instanceof Organization) {
            endpoint = endsWithId(ORGANIZATIONS);
        } else if (entity instanceof Profile) {
            endpoint = endsWithId(PROFILES);
        } else if (entity instanceof User) {
            endpoint = endsWithId(USERS);
        }

        if (endpoint != null) {
            documentResourceLinks.setEntity(resourceLinksHelper.replaceId(endpoint, input.getSystemEntity()));
        }
    }
}
