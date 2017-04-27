package org.consumersunion.stories.server.api.gwt_rpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.consumersunion.stories.common.client.i18n.CommonI18nErrorMessages;
import org.consumersunion.stories.common.client.service.RpcDocumentService;
import org.consumersunion.stories.common.client.service.datatransferobject.ProfileSummary;
import org.consumersunion.stories.common.client.service.response.ActionResponse;
import org.consumersunion.stories.common.client.service.response.DataResponse;
import org.consumersunion.stories.common.client.service.response.DatumResponse;
import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.StorySortField;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.document.BlockType;
import org.consumersunion.stories.common.shared.model.document.Content;
import org.consumersunion.stories.common.shared.model.document.Content.TextType;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.model.document.Document.DocumentContributorRole;
import org.consumersunion.stories.common.shared.model.document.DocumentContributor;
import org.consumersunion.stories.common.shared.model.document.SystemEntityRelation;
import org.consumersunion.stories.common.shared.model.questionnaire.AnswerSet;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.common.shared.service.datatransferobject.AnswerSetSummary;
import org.consumersunion.stories.common.shared.service.datatransferobject.StorySummary;
import org.consumersunion.stories.server.business_logic.DocumentService;
import org.consumersunion.stories.server.business_logic.SystemEntityService;
import org.consumersunion.stories.server.exception.NotAuthorizedException;
import org.consumersunion.stories.server.exception.NotFoundException;
import org.consumersunion.stories.server.exception.NotLoggedInException;
import org.consumersunion.stories.server.index.collection.UpdatedCollectionIndexer;
import org.consumersunion.stories.server.index.story.UpdatedAuthorNotesIndexer;
import org.consumersunion.stories.server.index.story.UpdatedStoryDocumentIndexer;
import org.consumersunion.stories.server.index.story.UpdatedStoryNotesIndexer;
import org.consumersunion.stories.server.persistence.AnswerSetPersister;
import org.consumersunion.stories.server.persistence.DocumentPersister;
import org.consumersunion.stories.server.persistence.DocumentPersister.EntityAndRelationParams;
import org.consumersunion.stories.server.persistence.ProfilePersister;
import org.consumersunion.stories.server.persistence.QuestionnaireI15dPersister;
import org.consumersunion.stories.server.persistence.StoryPersister;
import org.consumersunion.stories.server.persistence.StoryPersister.StoryPagedRetrieveParams;
import org.consumersunion.stories.server.util.StringUtil;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import net.lightoze.gwt.i18n.server.LocaleFactory;

import static org.consumersunion.stories.common.shared.AuthConstants.ACCESS_MODE_EXPLICIT;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;

@Service("documentService")
public class RpcDocumentServiceImpl extends RpcBaseServiceImpl implements RpcDocumentService {
    private static long serialVersionUID = 7373717148023557197L;

    @Inject
    private StoryPersister storyPersister;
    @Inject
    private QuestionnaireI15dPersister questionnaireI15dPersister;
    @Inject
    private AnswerSetPersister answerSetPersister;
    @Inject
    private SystemEntityService systemEntityService;
    @Inject
    private DocumentService documentService;
    @Inject
    private UpdatedStoryDocumentIndexer updatedStoryDocumentIndexer;
    @Inject
    private UpdatedCollectionIndexer updatedCollectionIndexer;
    @Inject
    private UpdatedAuthorNotesIndexer updatedAuthorNotesIndexer;
    @Inject
    private UpdatedStoryNotesIndexer updatedStoryNotesIndexer;

    @Override
    public ActionResponse createNotes(String text, Set<Integer> entityIds) {
        for (Integer entityId : entityIds) {
            Document note = new Document();
            note.addBlock(new Content(BlockType.CONTENT, text, TextType.PLAIN));
            note.setEntity(entityId);
            note.setSystemEntityRelation(SystemEntityRelation.NOTE);
            createUntitledDocument(note);
        }

        return new ActionResponse();
    }

    @Override
    public DatumResponse<Document> createUntitledDocument(Document document) {
        DatumResponse<Document> response = new DatumResponse<Document>();
        try {
            document.setPrimaryAuthor(userService.getActiveProfileId());
            document.setTitle("");
            document.setPermalink("");
            document.setPublic(false);
            document.setSystemEntityRelation(document.getSystemEntityRelation());

            document.setContributors(new ArrayList<DocumentContributor>());
            DocumentContributor contributor = new DocumentContributor();
            contributor.setContributor(userService.getActiveProfileId());
            contributor.setRole(DocumentContributorRole.AUTHOR);
            document.addContributor(contributor);

            response = saveDocument(document);
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }
        return response;
    }

    @Override
    public DatumResponse<Document> createDocument(Document document) {
        DatumResponse<Document> response = new DatumResponse<Document>();
        try {
            document.setPrimaryAuthor(userService.getActiveProfileId());
            document.setPermalink("");
            document.setPublic(false);
            document.setContributors(Lists.<DocumentContributor>newArrayList());

            DocumentContributor contributor = new DocumentContributor();
            contributor.setContributor(userService.getActiveProfileId());
            contributor.setRole(DocumentContributorRole.AUTHOR);
            document.addContributor(contributor);

            response = saveDocument(document);
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }
        return response;
    }

    @Override
    public DatumResponse<Document> createAttachment(Document document) {
        DatumResponse<Document> response = new DatumResponse<Document>();
        try {
            document.setPrimaryAuthor(userService.getActiveProfileId());
            document.setPublic(false);
            document.setSystemEntityRelation(SystemEntityRelation.ATTACHMENT);

            document.setContributors(new ArrayList<DocumentContributor>());
            DocumentContributor contributor = new DocumentContributor();
            contributor.setContributor(userService.getActiveProfileId());
            contributor.setRole(DocumentContributorRole.AUTHOR);
            document.addContributor(contributor);

            response = saveDocument(document);
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }
        return response;
    }

    @Override
    public DatumResponse<Document> saveDocument(Document document) {
        DatumResponse<Document> response = new DatumResponse<Document>();
        try {
            if (Strings.isNullOrEmpty(document.getPermalink())) {
                document.setPermalink(StringUtil.generateRandomPassword());
            }

            if (document.isNew()) {
                document = documentService.createDocument(document);
            } else {
                document = documentService.updateDocument(document);
                // See TASK-1500 for why this bit of code, why it's OK, and how to avoid it.
                if (document instanceof AnswerSet && !(document instanceof AnswerSetSummary)) {
                    document = getDocument(document.getId()).getDatum();
                }
            }
            response.setDatum(document);

            SystemEntity targetObj = systemEntityService.getSystemEntity(document.getEntity());
            if (targetObj instanceof Story
                    && (SystemEntityRelation.BODY.equals(document.getSystemEntityRelation())
                    || SystemEntityRelation.ANSWER_SET.equals(document.getSystemEntityRelation()))) {
                updatedStoryDocumentIndexer.index(document.getSystemEntity(), document);
            } else if (targetObj instanceof Collection
                    && SystemEntityRelation.BODY.equals(document.getSystemEntityRelation())) {
                Collection collection = (Collection) targetObj;
                collection.setBodyDocument(document);
                updatedCollectionIndexer.index(collection);
            } else if (document.getSystemEntityRelation() == SystemEntityRelation.NOTE) {
                saveNoteDocument(document);
            }
        } catch (NotAuthorizedException e) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        } catch (GeneralException e) {
            response.addGlobalErrorMessage(e.getLocalizedMessage());
            throw e;
        }

        return response;
    }

    private void saveNoteDocument(Document document) {
        int entityId = document.getSystemEntity();
        ProfileSummary author = loadPerson(document.getPrimaryAuthor());

        SystemEntity entity = systemEntityService.getSystemEntity(entityId);
        if (entity instanceof Story) {
            if (author == null || author.getProfile() == null) {
                String noteText = document.getOnlyContent();
                updatedStoryNotesIndexer.index(entityId, noteText);
            } else {
                int start = 0;
                int length = 100;

                boolean maybeMore = true;
                while (maybeMore) {
                    StoryPagedRetrieveParams params = new StoryPagedRetrieveParams(start, length,
                            StorySortField.CREATED_OLD, false, null, null, null,
                            author.getProfile().getId(), null, null, null, null, ACCESS_MODE_EXPLICIT,
                            getEffectiveSubject());
                    List<StorySummary> stories = storyPersister.getStoriesPaged(params, false);

                    for (StorySummary storySummary : stories) {
                        String noteText = document.getOnlyContent();
                        updatedAuthorNotesIndexer.index(storySummary.getStoryId(), noteText);
                    }

                    maybeMore = !stories.isEmpty();
                    start = start + length;
                }
            }
        }
    }

    private ProfileSummary loadPerson(int personId) {
        try {
            return persistenceService.process(new ProfilePersister.RetrieveProfileSummaryFunc(personId));
        } catch (GeneralException ignored) {
            return null;
        }
    }

    @Override
    public DatumResponse<Document> getDocument(int documentId) {
        DatumResponse<Document> response = new DatumResponse<Document>();
        try {
            SystemEntityRelation relation =
                    persistenceService.process(new DocumentPersister.RetrieveDocumentKind(documentId));

            Document document;
            if (SystemEntityRelation.ANSWER_SET.equals(relation)) {
                AnswerSet answerSet = answerSetPersister.get(documentId);
                QuestionnaireI15d questionnaire = questionnaireI15dPersister.get(answerSet.getQuestionnaire());

                document = new AnswerSetSummary(answerSet, questionnaire.getTitle());
            } else {
                document = documentService.getDocument(documentId);
            }

            SystemEntity targetObj = systemEntityService.getSystemEntity(document.getSystemEntity());
            if (authService.isUserAuthorized(ROLE_READER, targetObj)) {
                response.setDatum(document);
            } else {
                response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
            }
        } catch (GeneralException e) {
            response.addGlobalErrorMessage(e.getLocalizedMessage());
            throw e;
        } catch (NotFoundException e) {
            response.addGlobalErrorMessage(e.getLocalizedMessage());
            throw e;
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }
        return response;
    }

    @Override
    public DatumResponse<Document> getDocument(int entityId, int documentId) {
        DatumResponse<Document> response = new DatumResponse<Document>();
        try {
            response.setDatum(documentService.getEntityDocument(entityId, documentId));
        } catch (GeneralException e) {
            response.addGlobalErrorMessage(e.getLocalizedMessage());
        } catch (NotAuthorizedException e) {
            response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }

        return response;
    }

    @Override
    public DataResponse<Document> getByEntityAndRelation(int entityId, SystemEntityRelation relation) {
        DataResponse<Document> response = new DataResponse<Document>();
        try {
            User user = userService.getLoggedInUser(true);
            if (authService.isUserAuthorized(ROLE_READER, entityId)) {
                try {
                    EntityAndRelationParams params =
                            new EntityAndRelationParams(entityId, relation, getEffectiveSubject(user));
                    List<Document> documents = documentService.getDocumentsByEntityAndRelation(params);
                    response.setData(documents);
                } catch (GeneralException e) {
                    response.addGlobalErrorMessage(e.getLocalizedMessage());
                    throw e;
                }
            } else {
                response.addGlobalErrorMessage(LocaleFactory.get(CommonI18nErrorMessages.class).invalidParameters());
            }
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }
        return response;
    }

    @Override
    public ActionResponse deleteDocument(int documentId) {
        ActionResponse response = new ActionResponse();
        try {
            documentService.deleteDocument(documentId);
        } catch (GeneralException e) {
            response.addGlobalErrorMessage(e.getLocalizedMessage());
        } catch (NotAuthorizedException e) {
            response.addGlobalErrorMessage(e.getMessage());
        } catch (NotLoggedInException e) {
            response.addGlobalErrorMessage(e.getMessage());
            response.setLoggedIn(false);
        }
        return response;
    }
}
