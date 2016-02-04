package org.consumersunion.stories.server.export;

import java.io.InputStream;

import javax.inject.Inject;

import org.consumersunion.stories.server.annotations.Amazon;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Component
public class AmazonS3ExportService {
    private final AmazonS3 amazonS3;
    private final String amazonS3Bucket;

    @Inject
    AmazonS3ExportService(
            AmazonS3 amazonS3,
            @Amazon String amazonS3Bucket) {
        this.amazonS3 = amazonS3;
        this.amazonS3Bucket = amazonS3Bucket;
    }

    public String upload(InputStream inputStream, String fileName) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("text/csv");

        PutObjectRequest putObjectRequest = new PutObjectRequest(amazonS3Bucket, fileName, inputStream, metadata);
        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3.putObject(putObjectRequest);

        return "https://" + amazonS3Bucket + ".s3.amazonaws.com/" + fileName;
    }
}
