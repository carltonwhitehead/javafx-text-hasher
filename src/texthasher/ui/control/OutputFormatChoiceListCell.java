package texthasher.ui.control;

import javafx.scene.control.ListCell;
import texthasher.domain.OutputFormatChoice;

/**
* A ListCell for an OutputFormatChoice
*/
public class OutputFormatChoiceListCell extends ListCell<OutputFormatChoice> {
    @Override
    protected void updateItem(OutputFormatChoice item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText(item.getLabel());
        } else {
            setText("");
        }
    }
}
