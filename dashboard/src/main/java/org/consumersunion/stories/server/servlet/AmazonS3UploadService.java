package org.consumersunion.stories.server.servlet;

import java.io.InputStream;

import javax.inject.Inject;

import org.consumersunion.stories.server.annotations.Amazon;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Component
public class AmazonS3UploadService implements ImageUploadService {
    private final AmazonS3 s3;
    private final String bucketName;
    private final AmazonS3KeyGenerator amazonS3KeyGenerator;

    @Inject
    AmazonS3UploadService(
            AmazonS3 amazonS3,
            @Amazon String amazonS3Bucket,
            AmazonS3KeyGenerator amazonS3KeyGenerator) {
        this.s3 = amazonS3;
        this.bucketName = amazonS3Bucket;
        this.amazonS3KeyGenerator = amazonS3KeyGenerator;
    }

    @Override
    public String upload(String name, InputStream inputStream, long size) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(size);
        metadata.setContentType(Mimetypes.getInstance().getMimetype(name));

        String key = amazonS3KeyGenerator.generateKey(name);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, metadata);
        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        s3.putObject(putObjectRequest);

        return "http://" + bucketName + ".s3.amazonaws.com/" + key;
    }
}
