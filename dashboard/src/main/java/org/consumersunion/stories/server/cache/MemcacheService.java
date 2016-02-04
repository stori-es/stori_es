package org.consumersunion.stories.server.cache;

public interface MemcacheService {
    boolean getBoolean(String key, boolean valueIfAbsent);

    void put(String key, boolean value);
}
