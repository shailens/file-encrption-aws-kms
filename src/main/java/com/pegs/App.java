package com.pegs;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * user: shailendra
 * Date: 21/02/20
 * Time: 6:28 PM
 */
public class App {
   private static final String BUCKET_NAME = SystemUtils.getValue("aws.s3.bucketName");
   private static final String KEY_PREFIX = SystemUtils.getValue("aws.s3.keyPrefix");
   private static final String REGION = SystemUtils.getValue("aws.region");


    public static void main(String [] args) throws Exception{
        File file = new File("/Users/shailendra/workspace/data/sample/hello.txt");
        S3Encryption s3 = new S3Encryption();
       // s3.upload(file,"hello.txt",BUCKET_NAME,KEY_PREFIX);
        s3.download("kms/hello.txt",BUCKET_NAME,"/Users/shailendra/workspace/data/sample/helloDec.txt");
    }


}
