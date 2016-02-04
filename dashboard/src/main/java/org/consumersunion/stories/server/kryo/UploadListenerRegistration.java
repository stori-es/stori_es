package org.consumersunion.stories.server.kryo;

import com.esotericsoftware.kryo.Kryo;

import de.javakaffee.web.msm.serializer.kryo.KryoCustomization;
import gwtupload.server.UploadListener;

public class UploadListenerRegistration implements KryoCustomization {
    public UploadListenerRegistration() {
    }

    @Override
    public void customize(Kryo kryo) {
        kryo.register(UploadListener.class, new UploadListenerSerializer());
    }
}
