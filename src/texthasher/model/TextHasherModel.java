package texthasher.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.util.converter.ByteStringConverter;
import texthasher.delegate.HashDelegate;
import texthasher.domain.AlgorithmChoice;
import texthasher.domain.OutputFormatChoice;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

public class TextHasherModel {
    public final StringProperty input = new SimpleStringProperty();
    public final ObjectProperty<AlgorithmChoice> algorithmChoice = new SimpleObjectProperty<>();
    public final ObjectProperty<OutputFormatChoice> outputFormatChoice = new SimpleObjectProperty<>();
    public final ObservableList<AlgorithmChoice> algorithmChoices = FXCollections.observableArrayList(
            AlgorithmChoice.secure("SHA-512"),
            AlgorithmChoice.secure("SHA-384"),
            AlgorithmChoice.secure("SHA-256"),
            AlgorithmChoice.secure("SHA-224"),
            AlgorithmChoice.insecure("SHA-1"),
            AlgorithmChoice.insecure("MD5")
    );
    public final ObservableList<OutputFormatChoice> outputFormatChoices = FXCollections.observableArrayList(
            new OutputFormatChoice(HashDelegate.OutputFormat.HexString, "Hex String"),
            new OutputFormatChoice(HashDelegate.OutputFormat.Base64, "Base 64"),
            new OutputFormatChoice(HashDelegate.OutputFormat.JavaFxByteStringConverter, ByteStringConverter.class.getSimpleName())
    );
    public final StringProperty output = new SimpleStringProperty();

    public final HashDelegate hashDelegate = new HashDelegate();
    public final BooleanProperty hashing = new SimpleBooleanProperty(false);

    public void generateOutput() {
        String inputText = input.get();
        boolean shouldUpdateOutputText = inputText != null && hashDelegate.isReady();
        if (!shouldUpdateOutputText) {
            return;
        }
        if (generateHashTask != null) {
            generateHashTask.cancel();
        }
        generateHashTask = new GenerateHashTask(inputText);
        generateHashTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            generateHashTask = null;
            output.setValue(newValue);
            hashing.setValue(false);
        });
        new Thread(generateHashTask).start();
        hashing.set(true);
    }

    private GenerateHashTask generateHashTask;

    private class GenerateHashTask extends Task<String> {

        private final String input;

        private GenerateHashTask(String input) {
            this.input = input;
        }

        @Override
        protected String call() throws Exception {

            // A bit of artificial delay for dramatic effect
            Thread.sleep(TimeUnit.SECONDS.toMillis(1) / 3);
            if (isCancelled()) {
                return null;
            }

            String hash;
            try {
                hash = hashDelegate.hash(input);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                hash = "";
            }
            return hash;
        }
    }
}
