package Controller;

import Controller.Speicher.SchreiberLeser;
import Controller.ViewController.GrundViewController;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application
{
    private static Parent root;
    private static HostServices hostServices;
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        if(SchreiberLeser.isErsterStart())
        {
            SchreiberLeser.alleZuruecksetzen();
            SchreiberLeser.alleSpeichern();
        }
        else
        {
            SchreiberLeser.alleLaden();
        }

        hostServices=getHostServices();

        root = FXMLLoader.load(getClass().getResource("../View/GrundView.fxml"));
        primaryStage.setTitle("HofUniversityManager");
        primaryStage.setScene(new Scene(root, 1000, 700));
        root.getStylesheets().add(getClass().getResource("../View/CSS/Application.css").toExternalForm());
        root.getStylesheets().add("https://fonts.googleapis.com/css2?family=Rubik&display=swap");

        GrundViewController.setThema(SchreiberLeser.getNutzerdaten().getAktuellesThema());

        primaryStage.setMinHeight(900);
        primaryStage.setMinWidth(1300);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t)
            {
                SchreiberLeser.alleSpeichern();

                Platform.exit();
                System.exit(0);
            }
        });
        Main.primaryStage=primaryStage;
        primaryStage.show();
    }

    public static Stage getPrimaryStage()
    {
        return primaryStage;
    }

    public static Parent getRoot()
    {
        return root;
    }

    public static void oeffneLinkInBrowser(String url)
    {
        hostServices.showDocument(url);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
