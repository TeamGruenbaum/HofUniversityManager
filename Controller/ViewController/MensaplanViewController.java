package Controller.ViewController;

import Controller.Speicher.SchreiberLeser;
import Model.Datum;
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

public class MensaplanViewController implements Initializable
{
    @FXML
    private ChoiceBox cbWochentag;

    @FXML
    private Label mpTitel;

    @FXML
    private VBox contentVBox;

    public void initialize(URL location, ResourceBundle resources)
    {
        ArrayList<String> listOriginal = new ArrayList<>();
        listOriginal.add("Montag");
        listOriginal.add("Dienstag");
        listOriginal.add("Mittwoch");
        listOriginal.add("Donnerstag");
        listOriginal.add("Freitag");
        ObservableList<String> list = FXCollections.observableArrayList(listOriginal);
        cbWochentag.setItems(list);

        mpTitel.setText("Mensaplan für die KW " + _getRichtigeKalenderwoche());

        cbWochentag.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            switch((int) newValue) {
                case 0 -> generiereSpeiseplan(Tag.MONTAG);
                case 1 -> generiereSpeiseplan(Tag.DIENSTAG);
                case 2 -> generiereSpeiseplan(Tag.MITTWOCH);
                case 3 -> generiereSpeiseplan(Tag.DONNERSTAG);
                case 4 -> generiereSpeiseplan(Tag.FREITAG);
                default -> System.out.println("Alert");
            }
        });

        switch(LocalDate.now().getDayOfWeek()) {
            case MONDAY -> cbWochentag.getSelectionModel().select(0);
            case TUESDAY -> cbWochentag.getSelectionModel().select(1);
            case WEDNESDAY -> cbWochentag.getSelectionModel().select(2);
            case THURSDAY -> cbWochentag.getSelectionModel().select(3);
            case FRIDAY -> cbWochentag.getSelectionModel().select(4);
            default -> cbWochentag.getSelectionModel().select(0);
        }

        /* //Testroutine
        Tag aktuellerTag = _datumZuTag(new Datum((LocalDate.now().getDayOfMonth()-1), LocalDate.now().getMonthValue(), LocalDate.now().getYear()));
        Tag testtag = _datumZuTag(new Datum(17-1, 12, 2020)); // Testroutine, wenn ich die gerichte des 17.12. haben möchte
        generiereSpeiseplan(Tag.MONTAG); */
    }

    /*private Tag _datumZuTag(Datum datum)
    {
        Calendar c = GregorianCalendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"), new Locale("de", "DE"));
        c.set(datum.getJahr(), datum.getMonat()-1, datum.getTag());
        return Tag.values()[c.get(Calendar.DAY_OF_WEEK)-1];
    }*/

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

                gerichte.stream().filter((ger) -> ger.getKategorie() == objName).forEach((ger) -> {
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
        List<List<Gericht>> listeListeGerichte = SchreiberLeser.getMensaplan().getWochenplan().stream()
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
                .map(Gericht::getKategorie)
                .distinct()
                .collect(Collectors.toList());
    }
}