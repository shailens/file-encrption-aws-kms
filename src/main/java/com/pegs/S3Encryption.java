package com.pegs;

import java.io.File;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * user: shailendra
 * Date: 21/02/20
 * Time: 6:56 PM
 */
public class S3Encryption {

    private static final String BUCKET_NAME = SystemUtils.getValue("aws.s3.bucketName");
    private static final String KEY_PREFIX = SystemUtils.getValue("aws.s3.keyPrefix");
    private static final String REGION = SystemUtils.getValue("aws.region");

    /**
     * Encrypts the content of the file and upload to S3.
     * @param file
     */
    public void upload(File file,String fileName,String bucketName,String keyPrefix) throws Exception{
        String accessKey = SystemUtils.getValue("aws.accessKey");
        String accessSecret = SystemUtils.getValue("aws.accessSecret");
        S3Client awsS3Service = new S3Client(accessKey,accessSecret,REGION);
        String fileContent = SystemUtils.readFile(file);
        Crypto encryption = new Crypto(accessKey,accessSecret,REGION);
        String cipherText = encryption.encrypt(fileContent);
        byte[] bytes = cipherText.getBytes();
        awsS3Service.upload(fileName,bucketName,bytes,keyPrefix,"text/html; charset=UTF-8");
    }

    public void download(String fileName, String bucketName,String toLocation) throws Exception{
        String accessKey = SystemUtils.getValue("aws.accessKey");
        String accessSecret = SystemUtils.getValue("aws.accessSecret");
        S3Client s3Client = new S3Client(accessKey,accessSecret,REGION);
        InputStream is =  s3Client.download(fileName,bucketName);
        String cipherText = SystemUtils.convertInputStreamToString(is);
        Crypto decrypt = new Crypto(accessKey,accessSecret,REGION);
        String plainText = decrypt.decrypt(cipherText);
        SystemUtils.write(plainText,toLocation);
    }

}
