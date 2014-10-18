package texthasher.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import texthasher.application.TextHasherApplication;
import texthasher.control.AlgorithmChoiceListCell;
import texthasher.control.OutputFormatChoiceListCell;
import texthasher.domain.AlgorithmChoice;
import texthasher.domain.OutputFormatChoice;
import texthasher.model.TextHasherModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TextHasherController implements Initializable {

    @FXML private TextArea input;
    @FXML private Text inputLength;
    @FXML private ProgressIndicator progress;
    @FXML private ComboBox<AlgorithmChoice> algorithm;
    @FXML private ComboBox<OutputFormatChoice> outputFormat;
    @FXML private TextArea output;

    private final TextHasherModel model = new TextHasherModel();

    public static Scene newScene(TextHasherApplication application) throws IOException {
        GridPane root = FXMLLoader.load(
                application.getClass().getResource("/texthasher/fxml/TextHasher.fxml"),
                TextHasherApplication.getResourceBundle()
        );
        return new Scene(root, 600, 250);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model.input.bindBidirectional(input.textProperty());
        model.input.addListener((observable, oldValue, newValue) -> model.generateOutput());
        inputLength.textProperty().bind(new SimpleStringProperty(resources.getString("input_length_label")).concat(model.input.length().asString()));

        algorithm.setItems(model.algorithmChoices);
        algorithm.setButtonCell(new AlgorithmChoiceListCell());
        algorithm.setCellFactory(param -> new AlgorithmChoiceListCell());
        model.algorithmChoice.bind(algorithm.valueProperty());
        model.algorithmChoice.addListener((observable, oldValue, newValue) -> {
            model.hashDelegate.setAlgorithm(newValue.getAlgorithm());
            model.generateOutput();
        });

        outputFormat.setItems(model.outputFormatChoices);
        outputFormat.setButtonCell(new OutputFormatChoiceListCell());
        outputFormat.setCellFactory(param -> new OutputFormatChoiceListCell());
        model.outputFormatChoice.bind(outputFormat.valueProperty());
        model.outputFormatChoice.addListener((observable, oldValue, newValue) -> {
            model.hashDelegate.setOutputFormat(newValue.getOutputFormat());
            model.generateOutput();
        });

        output.textProperty().bind(model.output);

        progress.visibleProperty().bind(model.hashing);
    }

}
