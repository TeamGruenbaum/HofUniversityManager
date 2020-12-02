package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../View/GrundView.fxml"));
        primaryStage.setTitle("StudiPlaner");
        primaryStage.setScene(new Scene(root, 1000, 700));
        root.getStylesheets().add(getClass().getResource("../View/css/application.css").toExternalForm());
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
