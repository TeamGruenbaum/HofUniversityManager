package Controller;

import Controller.InformationsVermittlung.Datenabrufer;
import Controller.Speicher.SchreiberLeser;
import Controller.ViewController.GrundViewController;
import Model.NutzerdatenModel.Thema;
import Model.TreffpunktModel.Freizeitaktivitaet;
import Model.TreffpunktModel.Restaurant;
import Model.TreffpunktModel.Treffpunkt;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    public static Parent root;
    private static HostServices hostServices;

    @Override
    public void start(Stage primaryStage) throws Exception{
        root = FXMLLoader.load(getClass().getResource("../View/GrundView.fxml"));
        primaryStage.setTitle("Studentenverwaltungsanwendung");
        primaryStage.setScene(new Scene(root, 1000, 700));
        root.getStylesheets().add(getClass().getResource("../View/CSS/Application.css").toExternalForm());
        root.getStylesheets().add("https://fonts.googleapis.com/css2?family=Rubik&display=swap");

        primaryStage.show();

        SchreiberLeser.datenZuruecksetzen();

        // Testroutine für TreffpunkteView
        /*SchreiberLeser.getTreffpunkte().getTreffpunkte().add(new Freizeitaktivitaet("Testname01F", "Testort", true, "Info", "Ambiente"));
        SchreiberLeser.getTreffpunkte().getTreffpunkte().add(new Restaurant("Testname02R", "Testort", true, "Info", "Art", "Italienisch", true));
        SchreiberLeser.getTreffpunkte().getTreffpunkte().add(new Freizeitaktivitaet("Testname03F", "Testort", true, "Info", "Ambiente"));
        SchreiberLeser.getTreffpunkte().getTreffpunkte().add(new Restaurant("Testname04R", "Testort", true, "Info", "Art", "Italienisch", true));*/

        Datenabrufer.treffpunkteAbrufen();
        Datenabrufer.mensaplanAbrufen();
        //Datenabrufer.stundenplanAbrufen();

        // Testroutine für instellungenView
        /*SchreiberLeser.getNutzerdaten().getSsoLogin().setName("Hans-Dieter");
        SchreiberLeser.getNutzerdaten().getSsoLogin().setPasswort("1234");
        SchreiberLeser.getNutzerdaten().setAktuellesThema(Thema.DUNKEL);*/

        GrundViewController._setTheme(SchreiberLeser.getNutzerdaten().getAktuellesThema());

        hostServices=getHostServices();
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
