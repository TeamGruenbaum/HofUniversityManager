package Controller.ViewController;

import Controller.Main;
import Controller.Speicher.SchreiberLeser;
import Model.DropdownModel.Studiengang;
import Model.DropdownModel.Studiensemester;
import Model.NutzerdatenModel.Thema;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EinstellungenViewController implements Initializable
{
    @FXML
    private TextField tfBenutzername;

    @FXML
    private PasswordField pfPasswort;

    @FXML
    private ComboBox<Studiengang> cbStudiengang;

    @FXML
    private ComboBox<Studiensemester> cbSemester;

    @FXML
    private AnchorPane aP;

    @FXML
    private CheckBox cbDarkMode;

    public void initialize(URL location, ResourceBundle resources)
    {
        // Listener für Klick in leere Fläche -> defokussieren
        aP.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> aP.requestFocus());

        // Textfeld fuer den Benutzernamen initialisieren, Listener für Änderungen und direkte Speicherung
        tfBenutzername.setText(SchreiberLeser.getNutzerdaten().getSsoLogin().getName());
        tfBenutzername.textProperty().addListener((observable, oldValue, newValue) -> {
            SchreiberLeser.getNutzerdaten().getSsoLogin().setName(newValue);
        });

        // Textfeld fuer das Passwort initialisieren, Listener für Änderungen und direkte Speicherung
        pfPasswort.setText(SchreiberLeser.getNutzerdaten().getSsoLogin().getPasswort());
        pfPasswort.textProperty().addListener((observable, oldValue, newValue) -> {
            SchreiberLeser.getNutzerdaten().getSsoLogin().setPasswort(newValue);
        });

        // ChoiceBox fuer den Studiengang initialisieren, Listener für Änderungen und direkte Speicherung
        cbStudiengang.setConverter(erzeugeStringConverterStudiengang());
        cbStudiengang.setPromptText("Studiengang auswählen");
        cbStudiengang.setItems(FXCollections.observableArrayList(SchreiberLeser.getDropdownMenue().getEintraege()));
        if(SchreiberLeser.getNutzerdaten().getStudiengang()!=null)
        {
            cbStudiengang.setValue(SchreiberLeser.getNutzerdaten().getStudiengang());
        }
        cbStudiengang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            SchreiberLeser.getNutzerdaten().setStudiengang(newValue);
            cbSemester.setItems(erzeugeSemesterListe(newValue));
            cbSemester.getSelectionModel().select(0); // Bestätigter Bug: PromptText wird nicht mehr angezeigt => deshalb PreSelection
        });

        // ChoiceBox fuer das Semester initialisieren, Listener für Änderungen und direkte Speicherung
        cbSemester.disableProperty().bind(cbStudiengang.getSelectionModel().selectedItemProperty().isNull());
        cbSemester.promptTextProperty().bind(Bindings
                .when(cbStudiengang.getSelectionModel().selectedItemProperty().isNull())
                .then("Bitte erst Studiengang wählen")
                .otherwise("Studiensemester wählen"));
        cbSemester.setConverter(erzeugeStringConverterStudiensemester());
        cbSemester.setItems(erzeugeSemesterListe(SchreiberLeser.getNutzerdaten().getStudiengang()));
        if(SchreiberLeser.getNutzerdaten().getStudiensemester()!=null) {
            cbSemester.setValue(SchreiberLeser.getNutzerdaten().getStudiensemester());
        }
        cbSemester.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            SchreiberLeser.getNutzerdaten().setStudiensemester(newValue);
        });

        // Checkbox fuer darkmode initialisieren, Listener für Änderungen und direkte Speicherung
        cbDarkMode.selectedProperty().setValue(SchreiberLeser.getNutzerdaten().getAktuellesThema().equals(Thema.DUNKEL));
        cbDarkMode.textProperty().bind(Bindings
                .when(cbDarkMode.selectedProperty())
                .then("aktiv")
                .otherwise("inaktiv"));
        cbDarkMode.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) {
                GrundViewController.setThema(Thema.HELL);
                SchreiberLeser.getNutzerdaten().setAktuellesThema(Thema.HELL);
            }
            if(newValue) {
                GrundViewController.setThema(Thema.DUNKEL);
                SchreiberLeser.getNutzerdaten().setAktuellesThema(Thema.DUNKEL);
            }
        });
    }

    private ObservableList<Studiensemester> erzeugeSemesterListe(Studiengang studiengang) {
        List<List<Studiensemester>> listeListeSemester = SchreiberLeser.getDropdownMenue().getEintraege().stream()
                .filter((obj) -> obj.equals(studiengang))
                .map(Studiengang::getStudiensemester)
                .collect(Collectors.toList());

        return FXCollections.observableList(listeListeSemester.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
    }

    private StringConverter<Studiengang> erzeugeStringConverterStudiengang() {
        return (new StringConverter<Studiengang>() {
            @Override
            public String toString(Studiengang object) {
                return object.getName();
            }

            @Override
            public Studiengang fromString(String string) {
                return null;
            }
        });
    }

    private StringConverter<Studiensemester> erzeugeStringConverterStudiensemester() {
        return (new StringConverter<Studiensemester>() {
            @Override
            public String toString(Studiensemester object) {
                return object.getName();
            }

            @Override
            public Studiensemester fromString(String string) {
                return null;
            }
        });
    }
}
