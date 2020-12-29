package Controller;

import Controller.InformationsVermittlung.Datenabrufer;
import Controller.Speicher.SchreiberLeser;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application {

    public static Parent root;
    private static HostServices hostServices;

    public Main()
    {
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        if(SchreiberLeser.isErsterStart())
        {
            SchreiberLeser.datenZuruecksetzen();
        }
        else
        {
            SchreiberLeser.alleLaden();
        }

        /*Task mensaplanTask=new Task<>()
        {
            @Override
            protected Object call() throws Exception
            {
                Datenabrufer.mensaplanAbrufen();
                SchreiberLeser.mensaplanSpeichern();

                return null;
            }
        };

        Task treffpunkteTask=new Task<>()
        {
            @Override
            protected Object call() throws Exception
            {
                Datenabrufer.treffpunkteAbrufen();
                SchreiberLeser.treffpunkteSpeichern();

                return null;
            }
        };

        new Thread(mensaplanTask).start();
        new Thread(treffpunkteTask).start();

        BooleanBinding allTasksFinished= mensaplanTask.stateProperty().isEqualTo(Worker.State.SUCCEEDED)
                .and(treffpunkteTask.stateProperty().isEqualTo(Worker.State.SUCCEEDED));

        allTasksFinished.addListener(((observable, oldValue, newValue) ->
        {
            System.out.println(newValue);
        }));*/

        root = FXMLLoader.load(getClass().getResource("../View/GrundView.fxml"));
        primaryStage.setTitle("Studentenverwaltungsanwendung");
        primaryStage.setScene(new Scene(root, 1000, 700));
        root.getStylesheets().add(getClass().getResource("../View/CSS/Application.css").toExternalForm());
        root.getStylesheets().add("https://fonts.googleapis.com/css2?family=Rubik&display=swap");

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t)
            {
                SchreiberLeser.alleSpeichern();

                Platform.exit();
                System.exit(0);
            }
        });

        hostServices=getHostServices();

        primaryStage.show();

        // Testroutine für instellungenView
        /*SchreiberLeser.getNutzerdaten().getSsoLogin().setName("Hans-Dieter");
        SchreiberLeser.getNutzerdaten().getSsoLogin().setPasswort("1234");
        SchreiberLeser.getNutzerdaten().setAktuellesThema(Thema.DUNKEL);*/

        //GrundViewController._setTheme(SchreiberLeser.getNutzerdaten().getAktuellesThema());
    }

    public static void oeffneLinkInBrowser(String url)
    {
        hostServices.showDocument(url);
    }

    public static Parent getRoot() {
        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
