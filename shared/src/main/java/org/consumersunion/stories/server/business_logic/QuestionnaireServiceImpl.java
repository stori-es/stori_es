package org.consumersunion.stories.server.business_logic;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.model.Organization;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.SubmitBlock;
import org.consumersunion.stories.common.shared.model.document.TextImageBlock;
import org.consumersunion.stories.common.shared.model.questionnaire.Questionnaire;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.i18n.CommonI18nMessages;
import org.consumersunion.stories.server.exception.NotAuthorizedException;
import org.consumersunion.stories.server.exception.NotFoundException;
import org.consumersunion.stories.server.exception.NotLoggedInException;
import org.consumersunion.stories.server.persistence.AuthPersister;
import org.consumersunion.stories.server.persistence.QuestionnaireI15dPersister;
import org.consumersunion.stories.server.persistence.QuestionnaireI15dPersister.SearchByCollectionPagedParams;
import org.consumersunion.stories.server.solr.collection.NewCollectionIndexer;
import org.consumersunion.stories.server.util.StringUtil;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.lightoze.gwt.i18n.server.LocaleFactory;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;
import static org.consumersunion.stories.common.shared.model.document.Content.TextType.HTML;
import static org.consumersunion.stories.server.util.StringUtil.generateSlug;

@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {
    private final AuthorizationService authService;
    private final UserService userService;
    private final CollectionService collectionService;
    private final DocumentService documentService;
    private final OrganizationService organizationService;
    private final IndexerService indexerService;
    private final AuthPersister authPersister;
    private final QuestionnaireI15dPersister questionnaireI15dPersister;

    @Inject
    QuestionnaireServiceImpl(
            AuthorizationService authService,
            UserService userService,
            CollectionService collectionService,
            DocumentService documentService,
            OrganizationService organizationService,
            IndexerService indexerService,
            AuthPersister authPersister,
            QuestionnaireI15dPersister questionnaireI15dPersister) {
        this.authService = authService;
        this.userService = userService;
        this.collectionService = collectionService;
        this.documentService = documentService;
        this.organizationService = organizationService;
        this.indexerService = indexerService;
        this.authPersister = authPersister;
        this.questionnaireI15dPersister = questionnaireI15dPersister;
    }

    @Override
    public Questionnaire getQuestionnaire(int id) {
        QuestionnaireI15d questionnaire = questionnaireI15dPersister.get(id);

        if (questionnaire == null) {
            throw new NotFoundException("No Questionnaire with ID " + id + " found.");
        }

        return checkQuestionnaireAuth(questionnaire);
    }

    @Override
    public Questionnaire getQuestionnaire(String permalink) {
        permalink = StringUtil.cleanPermalink(permalink);

        return questionnaireI15dPersister.getByPermalink(permalink).toQuestionnaire();
    }

    @Override
    public void deleteQuestionnaire(int id) {
        collectionService.deleteCollection(id);
    }

    @Override
    public QuestionnaireI15d getQuestionnaireI15d(int id) {
        if (!authService.isUserAuthorized(ROLE_READER, id)) {
            throw new NotAuthorizedException();
        }

        return questionnaireI15dPersister.get(id);
    }

    @Override
    public QuestionnaireI15d createQuestionnaire(QuestionnaireI15d questionnaire) {
        Integer contextOrganizationId = userService.getContextOrganizationId();

        int activeProfileId = userService.getActiveProfileId();
        if (authService.hasMinRole(activeProfileId, ROLE_CURATOR, contextOrganizationId)) {
            questionnaire.getBodyDocument().setPrimaryAuthor(activeProfileId);
            questionnaire.getConfirmationDocument().setPrimaryAuthor(activeProfileId);
            questionnaire.setOwner(contextOrganizationId);

            if (questionnaire.getTheme() == 0) {
                collectionService.setTheme(questionnaire);
            }
            setDefaultBlocks(questionnaire);

            String availablePermalink = questionnaireI15dPersister.getAvailablePermalink(
                    generateSlug(questionnaire.getTitle()));
            questionnaire.setPermalink(availablePermalink);

            QuestionnaireI15d dbQuestionnaire = questionnaireI15dPersister.createQuestionnaire(questionnaire);

            Organization org = organizationService.get(questionnaire.getOwner());
            Set<String> admins = Sets.newLinkedHashSet();
            admins.add(org.getName());
            Set<Integer> auths = authPersister.getNonStoryAuths(dbQuestionnaire.getId(), ROLE_READER);

            NewCollectionIndexer newCollectionIndexer =
                    new NewCollectionIndexer(dbQuestionnaire, auths, auths, auths, null, admins);
            indexerService.process(newCollectionIndexer);

            return dbQuestionnaire;
        }

        throw new NotAuthorizedException();
    }

    @Override
    public List<QuestionnaireI15d> searchByCollectionsPaged(SearchByCollectionPagedParams params) {
        return questionnaireI15dPersister.searchByCollectionsPaged(params);
    }

    @Override
    public QuestionnaireI15d updateQuestionnaire(QuestionnaireI15d questionnaire) {
        if (!authService.isUserAuthorized(ROLE_CURATOR, questionnaire.getId())) {
            throw new NotAuthorizedException();
        }

        return questionnaireI15dPersister.updateQuestionnaire(questionnaire);
    }

    private Questionnaire checkQuestionnaireAuth(QuestionnaireI15d questionnaire) {
        if (questionnaire.isPublic() && questionnaire.isPublished()
                || authService.isEntityAuthorized(userService.getActiveProfileId(), ROLE_READER, questionnaire)) {
            return questionnaire.toQuestionnaire();
        }

        throw new NotAuthorizedException();
    }

    private void setDefaultBlocks(QuestionnaireI15d questionnaire) {
        SubmitBlock submitBlock;

        Document survey = questionnaire.getSurvey();
        if (survey.getBlocks().isEmpty()) {
            CommonI18nMessages messages = LocaleFactory.get(CommonI18nMessages.class);

            TextImageBlock defaultTextContentBlock = new TextImageBlock(messages.defaultTextBlock());
            defaultTextContentBlock.setDocument(questionnaire.getId());

            Content defaultWaiver = createDefaultWaiver(questionnaire);

            submitBlock = new SubmitBlock();
            submitBlock.setPrompt(messages.submit());

            survey.setBlocks(
                    Lists.newArrayList(defaultTextContentBlock, defaultWaiver, submitBlock));
        } else {
            submitBlock = questionnaire.getSubmitBlock();
        }

        submitBlock.setDocument(questionnaire.getId());
    }

    private Content createDefaultWaiver(QuestionnaireI15d questionnaire) throws NotLoggedInException {
        Document waiverDocument = documentService.getWaiverDocument(questionnaire.getOwner());
        if (waiverDocument != null) {
            return createDefaultWaiver(waiverDocument.getOnlyContent(), questionnaire);
        } else {
            return createDefaultWaiver("", questionnaire);
        }
    }

    private Content createDefaultWaiver(String waiver, QuestionnaireI15d questionnaire) {
        Content waiverElement = new Content();
        waiverElement.setBlockType(BlockType.CUSTOM_PERMISSIONS);
        waiverElement.setDocument(questionnaire.getId());
        waiverElement.setTextType(HTML);
        waiverElement.setContent(waiver);

        return waiverElement;
    }
}
