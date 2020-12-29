package Controller.ViewController;

import Controller.Speicher.SchreiberLeser;
import Model.NutzerdatenModel.Thema;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class EinstellungenViewController implements Initializable
{
    @FXML
    private TextField tfBenutzername;

    @FXML
    private PasswordField pfPasswort;

    @FXML
    private ChoiceBox cbStudiengang;

    @FXML
    private ChoiceBox cbSemester;

    @FXML
    private AnchorPane aP;

    @FXML
    private CheckBox cbDarkMode;

    public void initialize(URL location, ResourceBundle resources)
    {
        // Listener für Klick in leere Fläche -> defokussieren

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
                GrundViewController.setThema(Thema.HELL);
                SchreiberLeser.getNutzerdaten().setAktuellesThema(Thema.HELL);
            }
            if(newValue) {
                GrundViewController.setThema(Thema.DUNKEL);
                SchreiberLeser.getNutzerdaten().setAktuellesThema(Thema.DUNKEL);
            }
        });
    }
}
