package Controller.ViewController;

import Controller.Speicher.SchreiberLeser;
import Model.Datum;
import Model.MensaplanModel.Gericht;
import Model.MensaplanModel.Tagesplan;
import Model.Tag;
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
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MensaplanViewController implements Initializable
{
    @FXML
    private ChoiceBox cbWochentag;

    public void initialize(URL location, ResourceBundle resources)
    {
        ArrayList<String> listOriginal = new ArrayList<>();
        listOriginal.add("Montag");
        listOriginal.add("Dienstag");
        listOriginal.add("Mittwoch");
        listOriginal.add("Donnerstag");
        listOriginal.add("Freitag");
        listOriginal.add("Samstag");
        ObservableList<String> list = FXCollections.observableArrayList(listOriginal);
        cbWochentag.setItems(list);

        SchreiberLeser.getMensaplan().getWochenplan().forEach((obj) ->
        {
            System.out.println(_datumZuTag(obj.getDatum()));
            System.out.println(obj.getDatum().getTag());
            obj.getGerichte().forEach((ger) -> System.out.println(ger.getBeschreibung()));
        });

        switch(LocalDate.now().getDayOfWeek()) {
            case MONDAY -> cbWochentag.getSelectionModel().select(0);
            case TUESDAY -> cbWochentag.getSelectionModel().select(1);
            case WEDNESDAY -> cbWochentag.getSelectionModel().select(2);
            case THURSDAY -> cbWochentag.getSelectionModel().select(3);
            case FRIDAY -> cbWochentag.getSelectionModel().select(4);
            case SATURDAY -> cbWochentag.getSelectionModel().select(5);
            default -> cbWochentag.getSelectionModel().select(0);
        }
    }

    private Tag _datumZuTag(Datum datum)
    {
        Calendar c = GregorianCalendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"), new Locale("de", "DE"));
        System.out.println(c.get(Calendar.YEAR));
        System.out.println(c.get(Calendar.MONTH));
        System.out.println(c.get(Calendar.DATE));
        c.set(datum.getJahr(), datum.getMonat()-1, datum.getTag());
        return Tag.values()[c.get(Calendar.DAY_OF_WEEK)-1];
    }

}
