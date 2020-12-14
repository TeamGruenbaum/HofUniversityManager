package Controller;

import Controller.Speicher.SchreiberLeser;
import Model.TreffpunktModel.Freizeitaktivitaet;
import Model.TreffpunktModel.Restaurant;
import Model.TreffpunktModel.Treffpunkt;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Parent root;

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
        SchreiberLeser.getTreffpunkte().getTreffpunkte().add(new Freizeitaktivitaet("Testname01F", "Testort", true, "Info", "Ambiente"));
        SchreiberLeser.getTreffpunkte().getTreffpunkte().add(new Restaurant("Testname02R", "Testort", true, "Info", "Art", "Italienisch", true));
        SchreiberLeser.getTreffpunkte().getTreffpunkte().add(new Freizeitaktivitaet("Testname03F", "Testort", true, "Info", "Ambiente"));
        SchreiberLeser.getTreffpunkte().getTreffpunkte().add(new Restaurant("Testname04R", "Testort", true, "Info", "Art", "Italienisch", true));
    }

    public static Parent getRoot() {
        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
