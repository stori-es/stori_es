package org.consumersunion.stories.tools;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.consumersunion.stories.common.shared.model.Collection;
import org.consumersunion.stories.common.shared.model.Story;
import org.consumersunion.stories.common.shared.model.questionnaire.QuestionnaireI15d;
import org.consumersunion.stories.server.business_logic.AuthorizationService;
import org.consumersunion.stories.server.persistence.AuthorizationPersistenceHelper;
import org.consumersunion.stories.server.persistence.CollectionPersister;
import org.consumersunion.stories.server.persistence.PersistenceUtil;
import org.consumersunion.stories.server.persistence.QuestionnaireI15dPersister;
import org.consumersunion.stories.server.persistence.StoryPersister;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_ADMIN;
import static org.consumersunion.stories.common.shared.AuthConstants.ROLE_CURATOR;

public class CustomIndexer {
    private static final Logger LOGGER = Logger.getLogger(CustomIndexer.class.getName());

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            LOGGER.severe("No arguments. Nothing to do.");
        } else {
            ApplicationContext context = new ClassPathXmlApplicationContext("classpath:META-INF/customIndexer.xml");

            Connection conn = null;
            try {
                conn = PersistenceUtil.getConnection();
                AuthorizationService authService = context.getBean(AuthorizationService.class).withConnection(conn);

                AuthorizationPersistenceHelper authorizationHelper = context.getBean(
                        AuthorizationPersistenceHelper.class);

                String type = args[0];
                if ("test".equals(type)) {
                    testConnections(context);
                } else if ("fixProfile".equals(type)) {
                    fixProfile(args, conn, authService, authorizationHelper);
                }
            } catch (Exception e) {
                e.printStackTrace();
                rollback(conn);
                closeConnection(conn);
            } finally {
                closeConnection(conn);
            }

            LOGGER.info("Done.");
        }

        System.exit(0);
    }

    private static void testConnections(ApplicationContext context) {
        LOGGER.info("--- Testing access ---");

        CollectionPersister collectionPersister = context.getBean(CollectionPersister.class);
        Collection collection = collectionPersister.get(757440);
        LOGGER.info(String.format("Collection %s --- OK", collection.getId()));

        QuestionnaireI15dPersister questionnairePersister = context.getBean(QuestionnaireI15dPersister.class);
        QuestionnaireI15d questionnaireI15d = questionnairePersister.get(757440);
        LOGGER.info(String.format("Questionnaire %s --- OK", questionnaireI15d.getId()));

        StoryPersister storyPersister = context.getBean(StoryPersister.class);
        Story story = storyPersister.get(761323);
        LOGGER.info(String.format("Story %s --- OK", story.getId()));
    }

    private static void fixProfile(String[] args, Connection conn, AuthorizationService authService,
            AuthorizationPersistenceHelper authorizationHelper) throws SQLException {
        int profileId = Integer.parseInt(args[1]);
        int orgId = Integer.parseInt(args[2]);

        LOGGER.info("--- Fix Profile ---");
        LOGGER.info(String.format("Organization: %d, Profile: %d", orgId, profileId));

        authService.grantAtLeast(profileId, ROLE_CURATOR, orgId);
        authService.grantAtLeast(orgId, ROLE_ADMIN, profileId);
        conn.commit();

        LOGGER.info("grantToCollectionsInOrganization");
        authorizationHelper.grantToCollectionsInOrganization(conn, ROLE_ADMIN, orgId, profileId);
        conn.commit();
    }

    private static void rollback(Connection conn) throws SQLException {
        if (conn != null) {
            conn.rollback();
        }
    }

    private static void closeConnection(Connection conn) throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }
}
