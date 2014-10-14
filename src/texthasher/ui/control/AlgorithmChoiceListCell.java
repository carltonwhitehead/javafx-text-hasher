package texthasher.ui.control;

import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
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
                setText(item.getAlgorithm() + " (INSECURE)");
                setTextFill(Color.FIREBRICK);
            }
        } else {
            setText("");
        }
    }
}
