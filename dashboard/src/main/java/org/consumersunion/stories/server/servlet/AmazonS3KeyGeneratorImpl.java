package org.consumersunion.stories.server.servlet;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class AmazonS3KeyGeneratorImpl implements AmazonS3KeyGenerator {
    @Override
    public String generateKey(String name) {
        String extension = name.substring(name.lastIndexOf('.'), name.length());
        return UUID.randomUUID().toString() + extension;
    }
}
