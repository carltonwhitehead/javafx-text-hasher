package texthasher;

import javafx.util.converter.ByteStringConverter;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashDelegate {

    private OutputFormat outputFormat;
    private String algorithm;

    public boolean isReady() {
        return algorithm != null && outputFormat != null;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setOutputFormat(OutputFormat outputFormat) {
        this.outputFormat = outputFormat;
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
        byte[] messageDigestBytes = messageDigest.digest();
        switch (outputFormat) {
            case HexString:
                return formatDigestAsHexString(messageDigestBytes);
            case Base64:
                return formatDigestAsBase64(messageDigestBytes);
            case JavaFxByteStringConverter:
            default:
                return formatDigestAsString(messageDigestBytes);
        }
    }

    private String formatDigestAsHexString(byte[] messageDigestBytes) {
        StringBuilder hexStringBuilder = new StringBuilder();
        for (int i = 0; i < messageDigestBytes.length; i++) {
            hexStringBuilder.append(Integer.toHexString(0xFF & messageDigestBytes[i]));
        }
        return hexStringBuilder.toString();
    }

    private String formatDigestAsBase64(byte[] messageDigestBytes) {
        return new BASE64Encoder().encode(messageDigestBytes);
    }

    private String formatDigestAsString(byte[] messageDigestBytes) {
        StringBuilder byteArrayStringBuilder = new StringBuilder();
        ByteStringConverter byteStringConverter = new ByteStringConverter();
        for (int i = 0; i < messageDigestBytes.length; i++) {
            byteArrayStringBuilder.append(byteStringConverter.toString(messageDigestBytes[i]));
        }
        return byteArrayStringBuilder.toString();
    }

    public enum OutputFormat {
        HexString,
        Base64,
        JavaFxByteStringConverter
    }
}
