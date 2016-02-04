package org.consumersunion.stories.server.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class UuidUniqueIdGenerator implements UniqueIdGenerator {
    @Override
    public String get() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
