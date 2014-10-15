package texthasher.domain;

import texthasher.delegate.HashDelegate;

/**
* Represents a hash output format choice available to the user
*/
public class OutputFormatChoice {

    private final HashDelegate.OutputFormat outputFormat;
    private final String label;

    public OutputFormatChoice(HashDelegate.OutputFormat outputFormat, String label) {
        this.outputFormat = outputFormat;
        this.label = label;
    }

    public HashDelegate.OutputFormat getOutputFormat() {
        return outputFormat;
    }

    public String getLabel() {
        return label;
    }
}
