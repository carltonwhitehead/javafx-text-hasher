package texthasher.application;

import javafx.application.Application;
import javafx.stage.Stage;
import texthasher.controller.TextHasherController;

import java.util.ResourceBundle;

public class TextHasherApplication extends Application {

    public static ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("texthasher.bundle.TextHasher");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Text Hasher");
        primaryStage.setScene(TextHasherController.newScene(this));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
