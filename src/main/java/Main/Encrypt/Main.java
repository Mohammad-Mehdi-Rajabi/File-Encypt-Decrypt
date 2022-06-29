package Main.Encrypt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.centerOnScreen();
        stage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/encrypt.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("File Encrypt");
        primaryStage.show();
    }
}
