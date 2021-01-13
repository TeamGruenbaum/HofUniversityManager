package Controller.ViewController;

import Controller.Sonstiges.TextHelfer;
import Controller.Speicher.SchreiberLeser;
import Model.DropdownModel.Studiengang;
import Model.MensaplanModel.Gericht;
import Model.MensaplanModel.Tagesplan;
import Model.Tag;
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
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import javafx.util.StringConverter;



public class MensaplanViewController implements Initializable
{
    @FXML
    private ChoiceBox<Tag> cbWochentag;

    @FXML
    private Label mpTitel;

    @FXML
    private VBox contentVBox;

    public void initialize(URL location, ResourceBundle resources)
    {
        mpTitel.setText("Mensaplan für die Kalenderwoche " + _getRichtigeKalenderwoche());

        cbWochentag.setItems(FXCollections.observableArrayList(Tag.values()));
        cbWochentag.setConverter(new StringConverter<Tag>() {
            @Override
            public String toString(Tag object) {
                return TextHelfer.grossschreiben(object.toString());
            }

            @Override
            public Tag fromString(String string) {
                return null;
            }
        });
        cbWochentag.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue)->generiereSpeiseplan(Tag.values()[newValue.intValue()]));
        cbWochentag.getSelectionModel().select(LocalDate.now().getDayOfWeek().getValue()<6?LocalDate.now().getDayOfWeek().getValue()-1:0);
    }

    private void generiereSpeiseplan(Tag tag) {
        List<Gericht> gerichte = _getGerichteListe(tag);
        List<String> kategorien = _getKategorienListe(tag);

        // Erstelle ContentAccordion
        Accordion contentAccordion = new Accordion();
        contentAccordion.setPrefWidth(500);
        contentVBox.getChildren().add(1, contentAccordion);

        // Entferne das überschüssige Accordion, aber nur, wenn nicht 1. Aufruf
        if(contentVBox.getChildren().size() > 2) contentVBox.getChildren().remove(2);

        if(!gerichte.isEmpty()) {
            kategorien.forEach((objName) -> {
                VBox vB = new VBox();
                vB.setSpacing(10);
                TitledPane tP = new TitledPane(objName, vB);
                contentAccordion.getPanes().add(tP);

                gerichte.stream().filter((ger) -> ger.getGang() == objName).forEach((ger) -> {
                    ArrayList<Label> aL = new ArrayList<>();
                    VBox vbGericht = new VBox();
                    aL.add(new Label(ger.getName()));
                    aL.add(new Label(ger.getBeschreibung()));
                    aL.add(new Label(_formatierePreis(ger.getPreis())));
                    aL.forEach((lbl) -> lbl.setWrapText(true));

                    ObservableList<Label> oaL = FXCollections.observableArrayList(aL);
                    vbGericht.getChildren().addAll(oaL);
                    vB.getChildren().add(vbGericht);
                });
            });
        } else {
            Label hinweisLeer = new Label("An diesem Tag stehen leider keine Gerichte zur Verfügung.");
            hinweisLeer.getStyleClass().add("warnhinweis");
            contentVBox.getChildren().add(1, hinweisLeer);
        }
    }

    private String _formatierePreis(float number) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        return format.format(number);
    }

    private int _getRichtigeKalenderwoche(){
        Date datum = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datum);
        int wochenNummer = calendar.get(Calendar.WEEK_OF_YEAR);

        if((LocalDate.now().getDayOfWeek() == DayOfWeek.SATURDAY) || (LocalDate.now().getDayOfWeek() == DayOfWeek.SUNDAY)) {
            return wochenNummer+1;
        }
        return wochenNummer;
    }

    private List<Gericht> _getGerichteListe(Tag tag) {
        // Liste mit den Tagen (einer, und zwar der heutige!), die Gericht-Liste beinhaltet erzeugen
        List<List<Gericht>> listeListeGerichte = SchreiberLeser.getMensaplan().getTagesplaene().stream()
                .filter((obj) -> obj.getTag().equals(tag))
                .map(Tagesplan::getGerichte)
                .collect(Collectors.toList());

        // Liste nur noch mit den Gerichten erzeugen
        return listeListeGerichte.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<String> _getKategorienListe(Tag tag) {
        // Liste mit den Gericht-Kategorien
        return _getGerichteListe(tag).stream()
                .map(Gericht::getGang)
                .distinct()
                .collect(Collectors.toList());
    }
}