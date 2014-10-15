package texthasher.ui.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.util.converter.ByteStringConverter;
import texthasher.delegate.HashDelegate;
import texthasher.domain.AlgorithmChoice;
import texthasher.domain.OutputFormatChoice;
import texthasher.ui.control.AlgorithmChoiceListCell;
import texthasher.ui.control.OutputFormatChoiceListCell;

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

    private StringProperty hash = new SimpleStringProperty();

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
        algorithm.setButtonCell(new AlgorithmChoiceListCell());
        algorithm.setCellFactory(param -> new AlgorithmChoiceListCell());
        algorithm.valueProperty().addListener((observable, oldValue, newValue) -> {
            hashDelegate.setAlgorithm(newValue.getAlgorithm());
            updateOutputText();
        });

        outputFormat.setItems(FXCollections.observableArrayList(
                new OutputFormatChoice(HashDelegate.OutputFormat.HexString, "Hex String"),
                new OutputFormatChoice(HashDelegate.OutputFormat.Base64, "Base 64"),
                new OutputFormatChoice(HashDelegate.OutputFormat.JavaFxByteStringConverter, ByteStringConverter.class.getSimpleName())
        ));
        outputFormat.setButtonCell(new OutputFormatChoiceListCell());
        outputFormat.setCellFactory(param -> new OutputFormatChoiceListCell());
        outputFormat.valueProperty().addListener((observable, oldValue, newValue) -> {
            hashDelegate.setOutputFormat(newValue.getOutputFormat());
            updateOutputText();
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
