package org.consumersunion.stories.server.kryo;

import java.nio.ByteBuffer;

import com.esotericsoftware.kryo.serialize.LongSerializer;
import com.esotericsoftware.kryo.serialize.SimpleSerializer;

import gwtupload.server.UploadListener;

public class UploadListenerSerializer extends SimpleSerializer<UploadListener> {
    @Override
    public UploadListener read(ByteBuffer byteBuffer) {
        long contentLength = LongSerializer.get(byteBuffer, true);
        long bytesRead = LongSerializer.get(byteBuffer, true);

        UploadListener uploadListener = new UploadListener(0, contentLength);

        uploadListener.update(bytesRead, contentLength, 0);

        return uploadListener;
    }

    @Override
    public void write(ByteBuffer byteBuffer, UploadListener uploadListener) {
        LongSerializer.put(byteBuffer, uploadListener.getContentLength(), true);
        LongSerializer.put(byteBuffer, uploadListener.getBytesRead(), true);
    }
}
