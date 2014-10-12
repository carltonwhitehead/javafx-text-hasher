package texthasher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashDelegate {

    private String algorithm;

    public boolean isReady() {
        return algorithm != null;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String hash(String inputString) throws NoSuchAlgorithmException {
        if (inputString == null) {
            throw new IllegalArgumentException("inputString must not be null");
        }
        return hash(inputString.getBytes());
    }

    public String hash(byte[] inputBytes) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        messageDigest.update(inputBytes);
        StringBuilder hashBuilder = new StringBuilder();
        byte[] messageDigestBytes = messageDigest.digest();
        for (int i = 0; i < messageDigestBytes.length; i++) {
            hashBuilder.append(Integer.toHexString(0xFF & messageDigestBytes[i]));
        }
        return hashBuilder.toString();
    }
}
