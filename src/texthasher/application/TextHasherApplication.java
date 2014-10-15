package texthasher.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class TextHasherApplication extends Application {

    public static ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("texthasher.bundle.TextHasher");
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        GridPane root = FXMLLoader.load(
                getClass().getResource("/texthasher/ui/fxml/TextHasher.fxml"),
                getResourceBundle()
        );
        primaryStage.setTitle("Text Hasher");
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(root, 600, 250));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
