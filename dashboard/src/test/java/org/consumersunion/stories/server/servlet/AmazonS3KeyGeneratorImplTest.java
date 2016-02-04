package org.consumersunion.stories.server.servlet;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AmazonS3KeyGeneratorImplTest {
    AmazonS3KeyGeneratorImpl generator = new AmazonS3KeyGeneratorImpl();
    final static String UUID_REGEX = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";

    @Test
    public void generateKey_simpleCase() {
        String key = generator.generateKey("blah.png");

        assertThat(key).matches(UUID_REGEX + ".png");
    }

    @Test
    public void generateKey_fileWithSpaces() {
        String key = generator.generateKey("Screen Shot 2014-11-11 at 1.38.34 PM.png");

        assertThat(key).matches(UUID_REGEX + ".png");
    }
}
