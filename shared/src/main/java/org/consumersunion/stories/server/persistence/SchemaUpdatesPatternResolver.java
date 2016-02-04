package org.consumersunion.stories.server.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

/**
 * Get the scripts of the war file and executing them in the data base.
 *
 * @author Machin
 */
public class SchemaUpdatesPatternResolver {
    private static final String schemaFolder = "/schemaUpdates";

    private final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    public SchemaUpdatesPatternResolver(final List<String> resourceLocations) throws IOException,
            ClassNotFoundException {
        if ("true".equals(System.getProperty("skipDbUpdate"))) {
            System.out.println("Skipping DB updates based on 'skipDbUpdate' property.");
        } else {
            ScriptRunner runner = new ScriptRunner(false, true);
            String deployPath = resourceLocations.get(0);
            deployPath = deployPath.replaceFirst("deployPath", getAppPath() + schemaFolder);
            if (ResourcePatternUtils.isUrl(deployPath)) {
                Resource[] resources = resourcePatternResolver.getResources(deployPath);
                System.out.println("Automatic update of the database... Starts!!! (" + deployPath + " - "
                        + resources.length + ")");

                Arrays.sort(resources, new ResourceComparator());
                for (Resource resource : resources) {
                    BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()));
                    try {
                        runner.runScript(reader, resource.getFilename());
                    } catch (SQLException e) {
                        System.out.println("Error runnig sql file: " + resource.getFile().getPath() + e.getMessage());
                    }
                }
                try {
                    // removes all data on accessKey table if the data base isnot Z
                    runner.deleteAccessKeyData();
                } catch (SQLException e) {
                    System.out.println("Error removing data on accessKey table");
                }
                System.out.println("Automatic update of the database... Finished!!!");
            }

            runner.close();
        }
    }

    public String getAppPath() {
        System.out.println("Looking for schema udpates... ");
        URL r = getClass().getClassLoader().getResource("org/consumersunion/stories/");

        if (r != null) {
            return new File(r.getFile()).getParentFile().getParentFile().getParentFile().getParent();
        } else { // for the dev environment
            System.out.println("Falling back to dev environment update locale.");
            return "/home/user/main/site/WEB-INF";
        }
    }

    class ResourceComparator implements Comparator<Resource> {
        @Override
        public int compare(Resource o1, Resource o2) {
            return o1.getFilename().compareTo(o2.getFilename());
        }
    }
}
