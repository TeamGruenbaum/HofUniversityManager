package Controller.ViewController;

import Controller.Speicher.SchreiberLeser;
import Model.TreffpunktModel.Freizeitaktivitaet;
import Model.TreffpunktModel.Restaurant;
import Model.TreffpunktModel.Treffpunkt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TreffpunktViewController implements Initializable
{
    @FXML
    private ChoiceBox cbTreffpunktTyp;

    @FXML
    private VBox contentVBox;

    public void initialize(URL location, ResourceBundle resources)
    {
        // Erzeuge Einträge in ChoiceBox und wähle 1. Element
        ArrayList<String> listOriginal = new ArrayList<>();
        listOriginal.add("Restaurants");
        listOriginal.add("Freizeitaktivitäten");
        ObservableList<String> list = FXCollections.observableArrayList(listOriginal);
        cbTreffpunktTyp.setItems(list);
        cbTreffpunktTyp.getSelectionModel().selectFirst();

        // Erstelle Accordion und füge es der VBox hinzu
        Accordion contentAccordion = new Accordion();
        contentAccordion.setPrefWidth(500);
        contentVBox.getChildren().add(contentAccordion);

        // Erhalte alle Treffpunkte als ArrayList
        ArrayList<Treffpunkt> alleTreffpunkte = SchreiberLeser.getTreffpunkte().getTreffpunkte();

        // Erstmaliger Aufruf: Ausführung der Abfrage für Restaurants
        zeigeRestaurants(contentAccordion, alleTreffpunkte);

        // Listener User-Änderung im Filter
        cbTreffpunktTyp.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(0)) {
                zeigeRestaurants(contentAccordion, alleTreffpunkte);
            } else {
                zeigeFreizeitaktivitaeten(contentAccordion, alleTreffpunkte);
            }
        });

    }

    public void zeigeRestaurants(Accordion accordion, ArrayList<Treffpunkt> arrayList)
    {
        accordion.getPanes().clear();
        arrayList.stream().filter((obj) -> obj instanceof Restaurant).forEach((obj) -> {

            VBox vB = new VBox();

            TitledPane tP = new TitledPane(obj.getName(), vB);
            accordion.getPanes().add(tP);

            ArrayList<Label> aL = new ArrayList<>();
            aL.add(new Label("Ort: " + obj.getOrt()));
            aL.add(new Label("Information: " + obj.getInformation()));
            aL.add(new Label("Art: " + ((Restaurant) obj).getArt()));
            aL.add(new Label("Nationalität: " + ((Restaurant) obj).getNationalitaet()));
            aL.add(new Label((obj.getWetterunabhaengig())?"Wetterabhängig: nein":"Wetterabhängig: ja"));
            aL.add(new Label((((Restaurant) obj).getLieferdienst())?"Liefert: ja":"Liefert: nein"));
            ObservableList<Label> oaL = FXCollections.observableArrayList(aL);

            vB.getChildren().addAll(oaL);
        });
    }

    public void zeigeFreizeitaktivitaeten(Accordion accordion, ArrayList<Treffpunkt> arrayList)
    {
        accordion.getPanes().clear();
        arrayList.stream().filter((obj) -> obj instanceof Freizeitaktivitaet).forEach((obj) -> {

            VBox vB = new VBox();

            TitledPane tP = new TitledPane(obj.getName(), vB);
            accordion.getPanes().add(tP);

            ArrayList<Label> aL = new ArrayList<>();
            aL.add(new Label("Ort: " + obj.getOrt()));
            aL.add(new Label("Information: " + obj.getInformation()));
            aL.add(new Label("Ambiente: " + ((Freizeitaktivitaet) obj).getAmbiente()));
            aL.add(new Label((obj.getWetterunabhaengig())?"Wetterabhängig: nein":"Wetterabhängig: ja"));
            ObservableList<Label> oaL = FXCollections.observableArrayList(aL);

            vB.getChildren().addAll(oaL);
        });
    }
}
