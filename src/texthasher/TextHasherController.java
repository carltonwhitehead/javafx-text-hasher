package texthasher;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class TextHasherController implements Initializable {

    private final HashDelegate hashDelegate = new HashDelegate();

    @FXML private Label inputLabel;
    @FXML private TextArea input;
    @FXML private ComboBox<AlgorithmChoice> algorithm;
    @FXML private Label outputLabel;
    @FXML private TextArea output;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inputLabel.setLabelFor(input);
        outputLabel.setLabelFor(output);
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
        input.textProperty().addListener((observable, oldValue, newValue) -> updateOutputText());
        algorithm.valueProperty().addListener((observable, oldValue, newValue) -> {
            hashDelegate.setAlgorithm(newValue.algorithm);
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
        String hash;
        try {
            hash = hashDelegate.hash(input.getText());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            hash = "";
        }
        output.setText(hash);
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
}
