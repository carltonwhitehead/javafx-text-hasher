package texthasher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TextHasherApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        GridPane root = FXMLLoader.load(getClass().getResource("TextHasher.fxml"));
        primaryStage.setTitle("Text Hasher");
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(root, 600, 250));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
