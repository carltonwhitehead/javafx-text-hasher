package texthasher.control;

import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import texthasher.application.TextHasherApplication;
import texthasher.domain.AlgorithmChoice;

/**
* A ListCell for an AlgorithmChoice
*/
public class AlgorithmChoiceListCell extends ListCell<AlgorithmChoice> {

    @Override
    protected void updateItem(AlgorithmChoice item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            if (item.isSecure()) {
                setText(item.getAlgorithm());
                setTextFill(Color.BLACK);
            } else {
                setText(formatInsecureAlgorithm(item));
                setTextFill(Color.FIREBRICK);
            }
        } else {
            setText("");
        }
    }

    private String formatInsecureAlgorithm(AlgorithmChoice item) {
        return String.format(
                TextHasherApplication.getResourceBundle().getString("algorithm_choice_listcell_insecure_format"),
                item.getAlgorithm()
        );
    }
}
