package org.consumersunion.stories.server.business_logic;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.consumersunion.stories.common.shared.AuthConstants;
import org.consumersunion.stories.common.shared.model.Profile;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.SystemEntity;
import org.consumersunion.stories.common.shared.model.User;
import org.consumersunion.stories.common.shared.service.GeneralException;
import org.consumersunion.stories.server.exception.NotLoggedInException;
import org.consumersunion.stories.server.persistence.AuthorizationPersistenceHelper;
import org.consumersunion.stories.server.persistence.PersistenceService;
import org.consumersunion.stories.server.persistence.funcs.ProcessFunc;
import org.consumersunion.stories.server.solr.SolrServer;
import org.consumersunion.stories.server.solr.story.documents.IndexedStoryDocument;
import org.springframework.security.acls.domain.EhCacheBasedAclCache;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_ADMIN;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_READER;
import static org.consumersunion.stories.server.persistence.AuthorizationPersistenceHelper.AuthOperation;
import static org.consumersunion.stories.server.persistence.AuthorizationPersistenceHelper.CheckGrant;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    private static final boolean THROW_IF_NULL = true;

    private final UserService userService;
    private final SystemEntityService systemEntityService;
    private final PersistenceService persistenceService;
    private final EhCacheBasedAclCache cache;
    private final SolrServer storySolrServer;
    private final SolrServer collectionSolrServer;

    private Connection connection;

    @Inject
    AuthorizationServiceImpl(
            UserService userService,
            SystemEntityService systemEntityService,
            PersistenceService persistenceService,
            EhCacheBasedAclCache cache,
            @Named("solrStoryServer") SolrServer storySolrServer,
            @Named("solrCollectionServer") SolrServer collectionSolrServer) {
        this.userService = userService;
        this.systemEntityService = systemEntityService;
        this.persistenceService = persistenceService;
        this.cache = cache;
        this.storySolrServer = storySolrServer;
        this.collectionSolrServer = collectionSolrServer;
    }

    @Override
    public AuthorizationService withConnection(Connection connection) {
        AuthorizationServiceImpl service = new AuthorizationServiceImpl(userService, systemEntityService,
                persistenceService, cache, storySolrServer, collectionSolrServer);

        service.setConnection(connection);

        return service;
    }

    @Override
    public boolean isUserAuthorized(int minRole, SystemEntity target) throws NotLoggedInException {
        if (target == null) {
            return false;
        }

        if (minRole == ROLE_READER && target.isPublic()) {
            return true;
        }

        User user = userService.getLoggedInUser(THROW_IF_NULL);
        if (isSuperUser(user) || target.getId() == user.getId()) {
            return true;
        } else if (target instanceof Profile) {
            Profile profile = (Profile) target;
            if (profile.getUserId() != null && profile.getUserId() == user.getId()) {
                return true;
            } else {
                return hasMinRoleOnProfile(userService.getActiveProfileId(), minRole, profile)
                        || hasMinRoleOnProfile(user.getId(), minRole, profile)
                        || hasMinRoleOnProfile(userService.getContextOrganizationId(), minRole, profile);
            }
        }

        return isEntityAuthorized(userService.getActiveProfileId(), minRole, target);
    }

    @Override
    public boolean isUserAuthorized(int minRole, int target) throws NotLoggedInException {
        SystemEntity targetObj = systemEntityService.getSystemEntity(target);

        return isUserAuthorized(minRole, targetObj);
    }

    @Override
    public boolean isEntityAuthorized(int subject, int minRole, SystemEntity target) {
        if (target == null) {
            return false;
        }

        if (target.getOwner() != null && target.getOwner() == subject) {
            return true;
        }

        if (hasMinRole(subject, minRole, target.getId())) {
            return true;
        }

        if (target instanceof Story) {
            return hasMinRoleOnStory(subject, minRole, target.getId());
        }

        User user = userService.getLoggedInUser();
        if (target instanceof User && target.getId() == user.getId()) {
            return true;
        } else if (target instanceof Profile) {
            Profile profile = (Profile) target;
            if (profile.getUserId() == user.getId()) {
                return true;
            } else {
                return hasMinRoleOnProfile(userService.getContextOrganizationId(), minRole, profile);
            }
        }

        return false;
    }

    @Override
    public boolean isSuperUser(User user) {
        return user != null && user.getId() == AuthConstants.ROOT_ID;
    }

    @Override
    public boolean isSuperUser() {
        return isSuperUser(userService.getLoggedInUser());
    }

    @Override
    public boolean hasMinRole(int subject, int minRole, int target) {
        return maybeProcessWithConnection(new CheckGrant(new AuthOperation(subject, minRole, target)));
    }

    @Override
    public void grant(int subject, int role, int target) {
        AuthOperation authOperation = new AuthOperation(subject, role, target);

        maybeProcessWithConnection(new AuthorizationPersistenceHelper.GrantFunc(authOperation));

        cache.clearCache();
    }

    @Override
    public void grantAtLeast(int subject, int role, int target) {
        AuthOperation authOperation = new AuthOperation(subject, role, target);

        maybeProcessWithConnection(new AuthorizationPersistenceHelper.GrantAtLeastFunc(authOperation));

        cache.clearCache();
    }

    @Override
    public void deny(Integer subject, Integer target) {
        // We use the AuthOperation for convenience, though the role doesn't matter; we're denying everything.
        AuthOperation authOperation = new AuthOperation(subject, 0, target);

        maybeProcessWithConnection(new AuthorizationPersistenceHelper.DenyFunc(authOperation));

        cache.clearCache();
    }

    @Override
    public List<SystemEntity> getValidTargets(int subject, int minRole, String joinTable) {
        AuthOperation authOperation = new AuthOperation(subject, minRole, joinTable);

        return maybeProcessWithConnection(new AuthorizationPersistenceHelper.GetValidTargets(authOperation));
    }

    @Override
    public List<SystemEntity> getValidPrincipals(int minRole, int target, String joinTable) {
        AuthOperation authOperation = new AuthOperation(target, minRole, joinTable);

        return maybeProcessWithConnection(new AuthorizationPersistenceHelper.GetValidPrincipals(authOperation));
    }

    @Override
    public boolean canRead(SystemEntity target) throws NotLoggedInException {
        return isUserAuthorized(ROLE_READER, target);
    }

    @Override
    public boolean canWrite(SystemEntity target) throws NotLoggedInException {
        return isUserAuthorized(ROLE_CURATOR, target);
    }

    @Override
    public boolean canWrite(Integer entityId) {
        return isUserAuthorized(ROLE_CURATOR, entityId);
    }

    @Override
    public boolean hasMinRoleOnStory(int profileId, int minRole, int targetId) {
        try {
            SolrQuery sQuery = new SolrQuery("id:" + targetId);
            sQuery.setRows(1);
            sQuery.setStart(0);

            QueryResponse result = storySolrServer.query(sQuery);

            Iterator<SolrDocument> iterator = result.getResults().iterator();
            if (iterator.hasNext()) {
                IndexedStoryDocument doc = new IndexedStoryDocument(iterator.next());
                if (!doc.getCollectionsId().isEmpty()) {
                    String collectionQueryString = "id:(" + Joiner.on(" OR ").join(doc.getCollectionsId()) + ") AND ";

                    if (minRole == ROLE_READER) {
                        collectionQueryString += "readAuths:" + profileId;
                    } else if (minRole == ROLE_CURATOR) {
                        collectionQueryString += "writeAuths:" + profileId;
                    } else if (minRole == ROLE_ADMIN) {
                        collectionQueryString += "adminAuths:" + profileId;
                    } else {
                        return false;
                    }

                    sQuery = new SolrQuery(collectionQueryString);
                    result = collectionSolrServer.query(sQuery);

                    return result.getResults().getNumFound() > 0;
                }

                return false;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public boolean hasMinRoleOnProfile(int organizationId, int minRole, Profile profile) {
        return hasMinRole(organizationId, minRole, profile.getId());
    }

    private <I, O> O maybeProcessWithConnection(ProcessFunc<I, O> func) {
        if (connection == null) {
            return persistenceService.process(func);
        }

        return persistenceService.process(connection, func);
    }

    private void setConnection(Connection connection) {
        this.connection = connection;
    }
}
