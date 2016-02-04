package org.consumersunion.stories.server.business_logic;

import java.util.List;

import javax.inject.Inject;

import org.consumersunion.stories.common.shared.AuthConstants;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.model.document.Document;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.exception.NotAuthorizedException;
import org.consumersunion.stories.server.persistence.DocumentPersister;
import org.consumersunion.stories.server.persistence.DocumentPersister.EntityAndRelationParams;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.BODY;
import static org.consumersunion.stories.common.shared.model.document.SystemEntityRelation.DEFAULT_PERMISSIONS;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentPersister documentPersister;
    private final UserService userService;
    private final AuthorizationService authService;
    private final SystemEntityService systemEntityService;

    @Inject
    DocumentServiceImpl(
            DocumentPersister documentPersister,
            UserService userService,
            AuthorizationService authService,
            SystemEntityService systemEntityService) {
        this.documentPersister = documentPersister;
        this.userService = userService;
        this.authService = authService;
        this.systemEntityService = systemEntityService;
    }

    @Override
    public List<Document> getDocuments(SystemEntity systemEntity) {
        if (systemEntity instanceof Story) {
            return getStoryDocuments((Story) systemEntity);
        }

        List<Document> documents = Lists.newArrayList();
        try {
            List<Document> basicDocuments = documentPersister.retrieveDocumentsByEntity(systemEntity.getId());
            documents.addAll(basicDocuments);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return documents;
    }

    @Override
    public List<Document> getStoryDocuments(Story story) {
        return documentPersister.retrieveStoryDocuments(story);
    }

    @Override
    public Document getEntityDocument(int entityId, int documentId) {
        SystemEntity targetObj = systemEntityService.getSystemEntity(entityId);
        if (authService.isUserAuthorized(ROLE_READER, targetObj)) {
            Document document = documentPersister.get(documentId);

            if (document.getSystemEntity() != entityId) {
                throw new NotAuthorizedException();
            }

            return document;
        }

        throw new NotAuthorizedException();
    }

    @Override
    public Document getWaiverDocument(int organizationId) {
        EntityAndRelationParams params =
                new EntityAndRelationParams(organizationId, DEFAULT_PERMISSIONS, userService.getActiveProfileId());

        return documentPersister.retrieveLatestDocumentByRelation(params, Document.class);
    }

    @Override
    public Document createDocument(Document document) {
        User user = userService.getLoggedInUser(false);

        if (user != null) {
            SystemEntity targetObj = systemEntityService.getSystemEntity(document.getEntity());
            if (authService.isUserAuthorized(ROLE_CURATOR, targetObj)) {
                int profileId = userService.getActiveProfileId();
                if (document.getPrimaryAuthor() == 0) {
                    document.setPrimaryAuthor(profileId);
                } else if (document.getPrimaryAuthor() != profileId) {
                    throw new GeneralException("Document primary author must must current profile.");
                }
            } else {
                throw new NotAuthorizedException();
            }
        }

        return documentPersister.create(document);
    }

    @Override
    public Document updateDocument(Document document) {
        return documentPersister.updateDocument(document);
    }

    @Override
    public Document getDocument(int documentId) {
        return documentPersister.get(documentId);
    }

    @Override
    public Document getBodyDocument(int entityId) {
        EntityAndRelationParams params = new EntityAndRelationParams(entityId, BODY, userService.getEffectiveSubject());

        List<Document> documents = documentPersister.retrieveDocumentsByEntityAndRelation(params);
        if (documents.size() > 1) {
            throw new GeneralException("Unexpected found multiple body documents.");
        } else {
            return documents.get(0);
        }
    }

    @Override
    public Document getOriginalBodyDocument(int entityId) {
        EntityAndRelationParams params = new EntityAndRelationParams(entityId, BODY, userService.getEffectiveSubject());

        return documentPersister.retrieveOriginalByEntityAndRelation(params);
    }

    @Override
    public List<Document> getDocumentsByEntityAndRelation(EntityAndRelationParams params) {
        return documentPersister.retrieveDocumentsByEntityAndRelation(params);
    }

    @Override
    public void deleteDocument(int documentId) {
        Document document = getDocument(documentId);
        SystemEntity refObj = systemEntityService.getSystemEntity(document.getSystemEntity());

        if (!authService.isUserAuthorized(AuthConstants.ROLE_CURATOR, refObj)) {
            throw new NotAuthorizedException();
        }

        documentPersister.deleteDocument(document);
    }
}
