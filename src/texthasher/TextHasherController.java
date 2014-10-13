package texthasher;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.converter.ByteStringConverter;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class TextHasherController implements Initializable {

    private final HashDelegate hashDelegate = new HashDelegate();

    @FXML private Label inputLabel;
    @FXML private TextArea input;
    @FXML private ProgressIndicator progress;
    @FXML private ComboBox<AlgorithmChoice> algorithm;
    @FXML private ComboBox<OutputFormatChoice> outputFormat;
    @FXML private Label outputLabel;
    @FXML private TextArea output;

    private GenerateHashTask generateHashTask;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inputLabel.setLabelFor(input);
        outputLabel.setLabelFor(output);
        input.textProperty().addListener((observable, oldValue, newValue) -> updateOutputText());

        algorithm.setItems(FXCollections.observableArrayList(
                AlgorithmChoice.secure("SHA-512"),
                AlgorithmChoice.secure("SHA-384"),
                AlgorithmChoice.secure("SHA-256"),
                AlgorithmChoice.secure("SHA-224"),
                AlgorithmChoice.insecure("SHA-1"),
                AlgorithmChoice.insecure("MD5")
        ));
        algorithm.setButtonCell(new AlgorithmListCell());
        algorithm.setCellFactory(param -> new AlgorithmListCell());
        algorithm.valueProperty().addListener((observable, oldValue, newValue) -> {
            hashDelegate.setAlgorithm(newValue.algorithm);
            updateOutputText();
            input.requestFocus();
        });

        outputFormat.setItems(FXCollections.observableArrayList(
                new OutputFormatChoice(HashDelegate.OutputFormat.HexString, "Hex String"),
                new OutputFormatChoice(HashDelegate.OutputFormat.Base64, "Base 64"),
                new OutputFormatChoice(HashDelegate.OutputFormat.JavaFxByteStringConverter, ByteStringConverter.class.getSimpleName())
        ));
        outputFormat.setButtonCell(new OutputFormatListCell());
        outputFormat.setCellFactory(param -> new OutputFormatListCell());
        outputFormat.valueProperty().addListener((observable, oldValue, newValue) -> {
            hashDelegate.setOutputFormat(newValue.outputFormat);
            updateOutputText();
            input.requestFocus();
        });

        output.setFont(Font.font(java.awt.Font.MONOSPACED));
        output.setOnMouseClicked(event -> output.selectAll());
    }

    private void updateOutputText() {
        boolean shouldUpdateOutputText = input.getText() != null && hashDelegate.isReady();
        if (!shouldUpdateOutputText) {
            return;
        }
        if (generateHashTask != null) {
            generateHashTask.cancel();
        }
        generateHashTask = new GenerateHashTask(input.getText());
        generateHashTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            output.setText(newValue);
            progress.setVisible(false);
            generateHashTask = null;
        });
        progress.setVisible(true);
        new Thread(generateHashTask).start();
    }

    private static class AlgorithmListCell extends ListCell<AlgorithmChoice> {
        @Override
        protected void updateItem(AlgorithmChoice item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                if (item.secure) {
                    setText(item.algorithm);
                    setTextFill(Color.BLACK);
                } else {
                    setText(item.algorithm + " (INSECURE)");
                    setTextFill(Color.FIREBRICK);
                }
            } else {
                setText("");
            }
        }
    }

    private static class OutputFormatListCell extends ListCell<OutputFormatChoice> {
        @Override
        protected void updateItem(OutputFormatChoice item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item.label);
            } else {
                setText("");
            }
        }
    }

    private static class AlgorithmChoice {

        private final String algorithm;
        private final boolean secure;

        private AlgorithmChoice(String algorithm, boolean secure) {
            this.algorithm = algorithm;
            this.secure = secure;
        }

        private static AlgorithmChoice secure(String algorithm) {
            return new AlgorithmChoice(algorithm, true);
        }

        private static AlgorithmChoice insecure(String algorithm) {
            return new AlgorithmChoice(algorithm, false);
        }
    }

    private static class OutputFormatChoice {

        private final HashDelegate.OutputFormat outputFormat;
        private final String label;

        private OutputFormatChoice(HashDelegate.OutputFormat outputFormat, String label) {
            this.outputFormat = outputFormat;
            this.label = label;
        }
    }

    private class GenerateHashTask extends Task<String> {

        private final String input;

        private GenerateHashTask(String input) {
            this.input = input;
        }

        @Override
        protected String call() throws Exception {
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
