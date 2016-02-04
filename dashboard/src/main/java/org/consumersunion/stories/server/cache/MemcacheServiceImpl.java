package org.consumersunion.stories.server.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class MemcacheServiceImpl implements MemcacheService {
    // TODO: replace by external memcache
    private final Map<String, Boolean> map;

    public MemcacheServiceImpl() {
        map = Collections.synchronizedMap(new HashMap<String, Boolean>());
    }

    public boolean getBoolean(String key, boolean valueIfAbsent) {
        return map.containsKey(key) ? map.get(key) : valueIfAbsent;
    }

    @Override
    public void put(String key, boolean value) {
        map.put(key, value);
    }
}
