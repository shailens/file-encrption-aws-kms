package com.pegs;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * user: shailendra
 * Date: 21/02/20
 * Time: 6:14 PM
 */
public class S3Client {

    private static final Log LOGGER = LogFactory.getLog(S3Client.class);
    private AmazonS3 s3Client = null;

    public S3Client(final String accessKey, final String accessSecret, String region) {

         try {
            AWSCredentials credentials = new AWSCredentials() {
                public String getAWSAccessKeyId() {
                    return accessKey;
                }

                public String getAWSSecretKey() {
                    return accessSecret;
                }
            };
            s3Client =  AmazonS3ClientBuilder.standard()
                    .withRegion(region)
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .build();
        } catch (Exception e) {
            LOGGER.error("Unable to create S3 Client: " + e.getMessage(), e);
        }
    }

    public PutObjectResult upload(String key, String bucketName, byte[] bytes, String keyPrefix, String contentType) throws Exception {
        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setServerSideEncryption(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
        metaData.setContentLength(bytes.length);
        metaData.setContentType(contentType);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        PutObjectResult obj = null;
        try {
            if (!s3Client.doesBucketExist(bucketName)) {
                s3Client.createBucket(bucketName);
            }
            obj = s3Client.putObject(bucketName, keyPrefix + "/" + key, byteArrayInputStream, metaData);
        } catch (Exception e) {
            LOGGER.error("Could not upload object to S3: " + e.getMessage(), e);
            throw e;
        }
        return obj;
    }

    public InputStream download(String fileName, String bucketName){
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        return ((s3Object != null) ? s3Object.getObjectContent() : null);
    }

}
