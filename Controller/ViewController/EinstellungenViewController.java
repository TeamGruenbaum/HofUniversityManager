package Controller.ViewController;

import Controller.Speicher.SchreiberLeser;
import Model.DropdownModel.DropdownMenue;
import Model.DropdownModel.Studiengang;
import Model.DropdownModel.Studiensemester;
import Model.MensaplanModel.Gericht;
import Model.MensaplanModel.Tagesplan;
import Model.NutzerdatenModel.Thema;
import Model.StudiengangModel.StudiengangInformationen;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    private ChoiceBox<Studiengang> cbStudiengang;

    @FXML
    private ChoiceBox<Studiensemester> cbSemester;

    @FXML
    private AnchorPane aP;

    @FXML
    private CheckBox cbDarkMode;

    public void initialize(URL location, ResourceBundle resources)
    {
        // Listener für Klick in leere Fläche -> defokussieren

        // ACHTUNG TO-DO: Aktuell Abruf des Semesters bei jedem Rrefesh des Studiengangs -> in Binding eines Attributes überführen; zudem Datenaktualisierung im Model und Abruf der Auswahl zu Anwendungsbeginn

        // Items des Studiengangs setzen
        ObservableList<Studiengang> studiengangList = FXCollections.observableArrayList(SchreiberLeser.getDropdownMenue().getEintraege());
        cbStudiengang.setItems(studiengangList);

        // Converter für ChoiceBox setzen und richtigen Wert bei erstem Start wählen
        cbStudiengang.setConverter(stringConverterErzeugen());
        cbStudiengang.setValue(SchreiberLeser.getNutzerdaten().getStudiengang());

        // Erfassung der Benutzereingabe
        cbStudiengang.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            SchreiberLeser.getNutzerdaten().setStudiengang(newValue);
            List<List<Studiensemester>> listeListeSemester = SchreiberLeser.getDropdownMenue().getEintraege().stream()
                    .filter((eintrag) -> eintrag.equals(newValue))
                    .map(Studiengang::getStudiensemester)
                    .collect(Collectors.toList());

            List<Studiensemester> listeSemester = listeListeSemester.stream()
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

            cbSemester.setConverter(stringConverterErzeugenS());
            cbSemester.setItems(FXCollections.observableArrayList(listeSemester));
        });

        // Erster Abruf des Values
        tfBenutzername.setText(SchreiberLeser.getNutzerdaten().getSsoLogin().getName());
        pfPasswort.setText(SchreiberLeser.getNutzerdaten().getSsoLogin().getPasswort());

        // Listener für die Änderung und direkte Speicherung
        tfBenutzername.textProperty().addListener((observable, oldValue, newValue) -> {
            SchreiberLeser.getNutzerdaten().getSsoLogin().setName(newValue);
        });

        pfPasswort.textProperty().addListener((observable, oldValue, newValue) -> {
            SchreiberLeser.getNutzerdaten().getSsoLogin().setPasswort(newValue);
        });

        // Im ersten Durchlauf -> setze Checkbox auf richtigen Wert
        cbDarkMode.selectedProperty().setValue(SchreiberLeser.getNutzerdaten().getAktuellesThema().equals(Thema.DUNKEL));

        // Binding fuer den beistehenden Text
        cbDarkMode.textProperty().bind(Bindings
                .when(cbDarkMode.selectedProperty())
                .then("aktiv")
                .otherwise("inaktiv"));

        // Auf Userinteraktion hoeren und entsprechende Aktion durchfuehren
        cbDarkMode.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) {
                GrundViewController._setTheme(Thema.HELL);
                SchreiberLeser.getNutzerdaten().setAktuellesThema(Thema.HELL);
            }
            if(newValue) {
                GrundViewController._setTheme(Thema.DUNKEL);
                SchreiberLeser.getNutzerdaten().setAktuellesThema(Thema.DUNKEL);
            }
        });
    }

    private StringConverter<Studiengang> stringConverterErzeugen() {
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

    private StringConverter<Studiensemester> stringConverterErzeugenS() {
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
