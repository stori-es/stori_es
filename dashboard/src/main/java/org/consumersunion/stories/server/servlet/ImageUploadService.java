package org.consumersunion.stories.server.servlet;

import java.io.InputStream;

public interface ImageUploadService {
    String upload(String filename, InputStream inputStream, long size);
}
