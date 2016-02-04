package org.consumersunion.stories.server.helper.geo;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.consumersunion.stories.server.util.Base64;

public class UrlSigner {
    private static byte[] key;

    public UrlSigner(String keyString) throws IOException {
        keyString = keyString.replace('-', '+');
        keyString = keyString.replace('_', '/');
        key = Base64.decode(keyString);
    }

    public String signRequest(String path, String query) throws NoSuchAlgorithmException, InvalidKeyException {

        // Retrieve the proper URL components to sign
        String resource = path + '?' + query;

        // Get an HMAC-SHA1 signing key from the raw key bytes
        SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");

        // Get an HMAC-SHA1 Mac instance and initialize it with the HMAC-SHA1 key
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(sha1Key);

        // compute the binary signature for the request
        byte[] sigBytes = mac.doFinal(resource.getBytes());

        // base 64 encode the binary signature
        String signature = Base64.encodeBytes(sigBytes);

        // convert the signature to 'web safe' base 64
        signature = signature.replace('+', '-');
        signature = signature.replace('/', '_');

        return signature;
    }
}
