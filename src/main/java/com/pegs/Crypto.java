package com.pegs;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CryptoResult;
import com.amazonaws.encryptionsdk.kms.KmsMasterKey;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Collections;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * user: shailendra
 * Date: 19/02/20
 * Time: 6:22 PM
 */
public class Crypto {

    private static String keyArn = SystemUtils.getValue("aws.kms.keyArn");
    private static String kmsContextKey = SystemUtils.getValue("aws.kms.context.key");
    private static String kmsContextValue = SystemUtils.getValue("aws.kms.context.value");
    private static KmsMasterKeyProvider provider;
    private Log LOGGER = LogFactory.getLog(Crypto.class);

    public Crypto(final String accessKey, final String accessSecret, final String region) throws  Exception{
       try {
           AWSCredentials credentials = new AWSCredentials() {
               public String getAWSAccessKeyId() {
                   return accessKey;
               }

               public String getAWSSecretKey() {
                   return accessSecret;
               }
           };
           provider = KmsMasterKeyProvider.builder().withDefaultRegion(region).withCredentials(credentials).withKeysForEncryption(keyArn).build();
       }catch (Exception e){
            LOGGER.error("Couldn't initialize Crypto. " + e.getMessage());
            throw e;
       }
    }

    public String encrypt(String data){
        final AwsCrypto crypto = new AwsCrypto();
        final Map<String, String> context = Collections.singletonMap(kmsContextKey,kmsContextValue);
        final String ciphertext = crypto.encryptString(provider, data, context).getResult();
        return ciphertext;
    }


    public String decrypt(String encryptedText){
        final AwsCrypto crypto = new AwsCrypto();
        final CryptoResult<String, KmsMasterKey> decryptResult = crypto.decryptString(provider, encryptedText);
        if (!decryptResult.getMasterKeyIds().get(0).equals(keyArn)) {
            throw new IllegalStateException("Wrong key ID!");
        }
        final Map<String, String> context = Collections.singletonMap(kmsContextKey,kmsContextValue);;
        for (final Map.Entry<String, String> e : context.entrySet()) {
            if (!e.getValue().equals(decryptResult.getEncryptionContext().get(e.getKey()))) {
                throw new IllegalStateException("Wrong Crypto Context!");
            }
        }
        return decryptResult.getResult();
    }
}
