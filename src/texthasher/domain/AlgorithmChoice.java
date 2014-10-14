package texthasher.domain;

/**
* Represents an algorithm choice available to the user
*/
public class AlgorithmChoice {

    private final String algorithm;
    private final boolean secure;

    public AlgorithmChoice(String algorithm, boolean secure) {
        this.algorithm = algorithm;
        this.secure = secure;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public boolean isSecure() {
        return secure;
    }

    public static AlgorithmChoice secure(String algorithm) {
        return new AlgorithmChoice(algorithm, true);
    }

    public static AlgorithmChoice insecure(String algorithm) {
        return new AlgorithmChoice(algorithm, false);
    }
}
