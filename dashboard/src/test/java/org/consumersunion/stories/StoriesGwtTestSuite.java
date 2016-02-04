package org.consumersunion.stories;

import org.consumersunion.stories.common.client.ui.form.FormGwtTest;
import org.consumersunion.stories.service.client.AuthorizationServiceGwtTest;
import org.consumersunion.stories.service.client.CollectionServiceGwtTest2;
import org.consumersunion.stories.service.client.DocumentServiceGwtTest;
import org.consumersunion.stories.service.client.EntityServiceGwtTest;
import org.consumersunion.stories.service.client.InternalAuthorizationServiceGwtTest;
import org.consumersunion.stories.service.client.OrganizationServiceGwtTest;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.gwt.junit.tools.GWTTestSuite;

import junit.framework.Test;
import junit.framework.TestSuite;

public class StoriesGwtTestSuite extends GWTTestSuite {
    public static Test suite() {
        // DB updates can cause unexpected changes to the data, and in general
        // the test DB should be itself current
        System.setProperty("skipDbUpdate", "true");

        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_GLOBAL);

        final TestSuite suite = new TestSuite("Suite of test for the stories module.");
        // The 'InternalAuth...' test verifies auth assumptions based on the original DB. The other tests may 
        // add / modify the auths.
        suite.addTestSuite(InternalAuthorizationServiceGwtTest.class);
        suite.addTestSuite(FormGwtTest.class);
        suite.addTestSuite(CollectionServiceGwtTest2.class);
        suite.addTestSuite(OrganizationServiceGwtTest.class);
        suite.addTestSuite(DocumentServiceGwtTest.class);
        suite.addTestSuite(EntityServiceGwtTest.class);
        suite.addTestSuite(AuthorizationServiceGwtTest.class);
        // TODO : TASK-1384
//        suite.addTestSuite(UserServiceTest.class);

        return suite;
    }
}
