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

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Taskbar;
import java.awt.Toolkit;
import java.net.URL;

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
            SchreiberLeser.alleDatenLoeschen();
            SchreiberLeser.alleZuruecksetzen();
            SchreiberLeser.alleSpeichern();
        }
        else
        {
            try
            {
                SchreiberLeser.alleLaden();
            }
            catch(Exception keineGefahrException)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Daten korrupt");
                alert.setContentText("Die gespeicherten Daten wurden manipuliert. Deshalb wir das Programm nach dem Schließen des Fensters zurückgesetzt.");
                alert.showAndWait().ifPresentOrElse(
                    (item)->
                    {
                        SchreiberLeser.alleDatenLoeschen();
                        Platform.exit();
                        System.exit(0);
                    },
                    ()->
                    {
                        SchreiberLeser.alleDatenLoeschen();
                        Platform.exit();
                        System.exit(0);
                    }
                );
            }
        }

        hostServices=getHostServices();

        root = FXMLLoader.load(getClass().getResource("../View/GrundView.fxml"));
        primaryStage.setTitle("HofUniversityManager");
        primaryStage.setScene(new Scene(root, 1000, 700));
        root.getStylesheets().add(getClass().getResource("../View/CSS/Application.css").toExternalForm());
        Font.loadFont(Main.class.getResource("../Ressourcen/Schriften/Rubik-Regular.ttf").toExternalForm(), 10);
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream( "../Ressourcen/Grafiken/HUM_Icon_Short.png" )));

        /*final URL imageResource = Main.class.getClassLoader().getResource("../Ressourcen/Grafiken/HUM_Icon_Short.png");
        final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        final java.awt.Image image = defaultToolkit.getImage(imageResource);
        final Taskbar taskbar = Taskbar.getTaskbar();
        taskbar.setIconImage(image);*/

        GrundViewController.setThema(SchreiberLeser.getNutzerdaten().getAktuellesThema());

        primaryStage.setMinHeight(900);
        primaryStage.setMinWidth(1300);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t)
            {
                SchreiberLeser.alleSpeichern();

                if(SchreiberLeser.isErsterStart())
                {
                    SchreiberLeser.isErsterStartSetzen();
                }

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
