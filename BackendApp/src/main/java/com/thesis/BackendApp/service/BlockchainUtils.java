package com.thesis.BackendApp.service;

import com.google.gson.GsonBuilder;
import java.security.MessageDigest;
import java.security.*;
import java.util.Base64;

public class BlockchainUtils {
    public static String generateSHA256(String data){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(data.getBytes("UTF-8"));
            StringBuffer hexHash = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexHash.append('0');
                hexHash.append(hex);
            }
            return hexHash.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getJson(Object o) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(o);
    }

    public static String getDificultyString(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }

    /**
     * Generates an ECDSA signature using the provided private key and data.
     * @param privateKey the private key used to generate the signature
     * @param data the data to be signed
     * @return the byte array representing the signature
     */
    public static byte[] generateECDSASignature(PrivateKey privateKey, String data) {
        Signature signature;
        byte[] signatureBytes;
        try {
            signature = Signature.getInstance("ECDSA", "BC");
            signature.initSign(privateKey);
            byte[] dataBytes = data.getBytes();
            signature.update(dataBytes);
            signatureBytes = signature.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return signatureBytes;
    }

    /**
     * Verifies the given ECDSA signature against the provided public key and data.
     * @param publicKey the public key used to verify the signature
     * @param data the data to be verified
     * @param signatureBytes the byte array representing the signature
     * @return true if the signature is verified, false otherwise
     */
    public static boolean verifyECDSASignature(PublicKey publicKey, String data,
                                               byte[] signatureBytes) {
        try {
            Signature signature = Signature.getInstance("ECDSA", "BC");
            signature.initVerify(publicKey);
            signature.update(data.getBytes());
            return signature.verify(signatureBytes);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

}
