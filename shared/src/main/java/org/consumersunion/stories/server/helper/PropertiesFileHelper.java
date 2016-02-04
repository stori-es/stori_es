package org.consumersunion.stories.server.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public final class PropertiesFileHelper {
    private static final PropertiesFileHelper instance = new PropertiesFileHelper();

    private PropertiesFileHelper() {
    }

    public static PropertiesFileHelper getInstance() {
        return instance;
    }

    /**
     * Get property file from a package.
     *
     * @param propFileName
     * @return Property File
     * @throws IOException
     */
    public static Properties getPropertiesFromClasspath(String propFileName) throws IOException {
        Properties props = new Properties();

        FileInputStream propsIn;
        URL url = Thread.currentThread().getContextClassLoader().getResource(propFileName);
        if (url == null) {
            throw new FileNotFoundException("File not found");
        }
        propsIn = new FileInputStream(url.getFile());
        props.load(propsIn);

        return props;
    }
}
