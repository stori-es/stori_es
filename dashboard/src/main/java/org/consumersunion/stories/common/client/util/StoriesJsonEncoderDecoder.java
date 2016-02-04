package org.consumersunion.stories.common.client.util;

import com.google.inject.ImplementedBy;

@ImplementedBy(StoriesJsonEncoderDecoderImpl.class)
public interface StoriesJsonEncoderDecoder {
    <T> String toJson(Class<T> clazz, T object);

    <T> T fromJson(Class<T> clazz, String json);

    <T> T clone(Class<T> clazz, T object);
}
