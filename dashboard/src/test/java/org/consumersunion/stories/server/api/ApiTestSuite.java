package org.consumersunion.stories.server.api;

import org.consumersunion.stories.CargoTestSetup;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ApiTestSuite extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite();

        suite.addTestSuite(StoriesApiTest.class);
        suite.addTestSuite(DocumentsApiTest.class);
        suite.addTestSuite(OrganizationsApiTest.class);
        suite.addTestSuite(CollectionsApiTest.class);
        suite.addTestSuite(QuestionnairesApiTest.class);
        suite.addTestSuite(ProfilesApiTest.class);
        suite.addTestSuite(UsersApiTest.class);
        suite.addTestSuite(ConnectionApiTest.class);

        return new CargoTestSetup(suite);
    }
}
