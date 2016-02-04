package org.consumersunion.stories.server.amazon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.UUID;

import org.consumersunion.stories.server.spring.AmazonConfigurator;
import org.junit.Test;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * This test aims to assert that the bucket can be accessed for put/get requests.
 */
public class S3UploadTest {
    @Test
    public void upload() throws IOException {
        AmazonConfigurator configurator = new AmazonConfigurator();
        String bucketName = configurator.amazonS3Bucket();
        AmazonS3 s3 = configurator.amazonS3(bucketName);

        // We make sure the bucket exists
        List<Bucket> buckets = s3.listBuckets();
        assertThat(buckets).extracting("name").contains(bucketName);

        // We create an object in the bucket
        String key = UUID.randomUUID().toString();

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, createSampleFile());
        s3.putObject(putObjectRequest);

        // We retrieve that object from the bucket
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);

        S3Object object = s3.getObject(getObjectRequest);
        assertThat(object).isNotNull();
        assertThat(object.getKey()).isEqualTo(key);

        // We delete the object from the bucket
        s3.deleteObject(bucketName, key);

        try {
            s3.getObject(getObjectRequest);
            // Failure, the object shouldn't exist anymore
            fail("Object with key " + key + " shouldn't exist in bucketName " + bucketName);
        } catch (AmazonS3Exception e) {
            // Success, the object doesn't exist anymore
        }
    }

    private File createSampleFile() throws IOException {
        File file = File.createTempFile("aws-java-sdk-", ".txt");
        file.deleteOnExit();

        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write("some data\n");
        writer.close();

        return file;
    }
}
