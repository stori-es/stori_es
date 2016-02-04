package org.consumersunion.stories.common.client.util;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageMap;

public class CachingService {
    private final StoriesJsonEncoderDecoder jsonCodec;

    private StorageMap dataMap;
    private Map<String, String> cache;

    @Inject
    CachingService(StoriesJsonEncoderDecoder jsonCodec) {
        this.jsonCodec = jsonCodec;

        Storage stockStore = Storage.getSessionStorageIfSupported();
        if (stockStore != null) {
            dataMap = new StorageMap(stockStore);
        } else {
            cache = new HashMap<String, String>();
        }
    }

    public void putCachedData(String key, String value) {
        final Map<String, String> cacheStore = getCacheStorage();
        cacheStore.put(key, value);
    }

    public <T> void putCachedObject(Class<T> kind, String key, T value) {
        final Map<String, String> cacheStore = getCacheStorage();
        cacheStore.put(key, jsonCodec.toJson(kind, value));
    }

    public <T> T getCachedObject(Class<T> kind, String key) {
        final Map<String, String> cacheStore = getCacheStorage();
        return jsonCodec.fromJson(kind, cacheStore.get(key));
    }

    public String getCachedData(String key) {
        final Map<String, String> cacheStore = getCacheStorage();
        return cacheStore.get(key);
    }

    public void remove(String key) {
        getCacheStorage().remove(key);
    }

    public void clearObjects() {
        getCacheStorage().remove(CachedObjectKeys.OPENED_STORY);
        getCacheStorage().remove(CachedObjectKeys.OPENED_CONTENT);
        getCacheStorage().remove(CachedObjectKeys.OPENED_COLLECTION);
    }

    public void clear() {
        getCacheStorage().clear();
    }

    private Map<String, String> getCacheStorage() {
        if (dataMap != null) {
            return dataMap;
        }
        return cache;
    }
}
