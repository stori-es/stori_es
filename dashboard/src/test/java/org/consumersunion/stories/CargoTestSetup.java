package org.consumersunion.stories;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.cargo.container.ContainerType;
import org.codehaus.cargo.container.InstalledLocalContainer;
import org.codehaus.cargo.container.configuration.ConfigurationType;
import org.codehaus.cargo.container.configuration.LocalConfiguration;
import org.codehaus.cargo.container.deployable.WAR;
import org.codehaus.cargo.container.installer.Installer;
import org.codehaus.cargo.container.installer.ZipURLInstaller;
import org.codehaus.cargo.container.property.ServletPropertySet;
import org.codehaus.cargo.generic.DefaultContainerFactory;
import org.codehaus.cargo.generic.configuration.DefaultConfigurationFactory;
import org.codehaus.cargo.util.log.FileLogger;

import com.jayway.restassured.RestAssured;

import junit.extensions.TestSetup;
import junit.framework.Test;

public class CargoTestSetup extends TestSetup {
    private InstalledLocalContainer container;

    public CargoTestSetup(Test test) {
        super(test);
    }

    @Override
    protected void setUp() throws Exception {
        Installer installer = new ZipURLInstaller(
                new URL("http://archive.apache.org/dist/tomcat/tomcat-7/v7.0.55/bin/apache-tomcat-7.0.55.zip"));
        installer.install();

        LocalConfiguration configuration = (LocalConfiguration) new DefaultConfigurationFactory()
                .createConfiguration("tomcat7x", ContainerType.INSTALLED, ConfigurationType.STANDALONE);
        configuration.setProperty(ServletPropertySet.PORT, RestAssured.port + "");
        configuration.setProperty("cargo.install.directory", "abcdefg");

        FileLogger fileLogger = new FileLogger(installer.getHome() + "/log.txt", false);

        container = (InstalledLocalContainer) new DefaultContainerFactory().createContainer(
                "tomcat7x", ContainerType.INSTALLED, configuration);
        // Load up the JDBC and Solar configurations
        final Map<String, String> systemProperties = new HashMap<String, String>();
        // Usually, these all go hand-in-hand, but might as well process robustly.
        for (String property : new String[]{"JDBC_CONNECTION_STRING", "PARAM1", "PARAM2", "aws.secretKey", "aws" +
                ".accessKeyId"}) {
            if (System.getProperty(property) != null) {
                systemProperties.put(property, System.getProperty(property));
            }
        }
        if (!systemProperties.isEmpty()) {
            container.setSystemProperties(systemProperties);
        }
        container.setHome(installer.getHome());
        container.setOutput(installer.getHome() + "/output.txt");
        container.setLogger(fileLogger);

        WAR war = new WAR("dashboard/target/war");
        war.setContext("");
        configuration.addDeployable(war);

        container.start();
    }

    @Override
    protected void tearDown() throws Exception {
        container.stop();
    }
}
