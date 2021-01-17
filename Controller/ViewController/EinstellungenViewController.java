package Controller.ViewController;



import Controller.Speicher.SchreiberLeser;
import Model.DropdownModel.Studiengang;
import Model.DropdownModel.Studiensemester;
import Model.NutzerdatenModel.Thema;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;



public class EinstellungenViewController implements Initializable
{
    @FXML private BorderPane viewBorderPane;

    @FXML private TextField benutzernameTextField;
    @FXML private PasswordField passwortPasswortField;

    @FXML private ComboBox<Studiengang> studiengangChoicebox;
    @FXML private ComboBox<Studiensemester> semesterChoicebox;

    @FXML private CheckBox darkmodeCheckBox;



    public void initialize(URL location, ResourceBundle resources)
    {
        viewBorderPane.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> viewBorderPane.requestFocus());

        benutzernameTextField.setText(SchreiberLeser.getNutzerdaten().getSsoLogin().getName());
        benutzernameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            SchreiberLeser.getNutzerdaten().getSsoLogin().setName(newValue);
        });

        passwortPasswortField.setText(SchreiberLeser.getNutzerdaten().getSsoLogin().getPasswort());
        passwortPasswortField.textProperty().addListener((observable, oldValue, newValue) -> {
            SchreiberLeser.getNutzerdaten().getSsoLogin().setPasswort(newValue);
        });

        studiengangChoicebox.setConverter(new StringConverter<Studiengang>() {
            @Override
            public String toString(Studiengang object) {
                return object.getName();
            }

            @Override
            public Studiengang fromString(String string) {
                return null;
            }
        });
        studiengangChoicebox.setPromptText("Studiengang auswählen");
        studiengangChoicebox.setItems(FXCollections.observableArrayList(SchreiberLeser.getDropdownMenue().getStudiengaenge()));
        if(SchreiberLeser.getNutzerdaten().getAusgewaehlterStudiengang()!=null)
        {
            studiengangChoicebox.setValue(SchreiberLeser.getNutzerdaten().getAusgewaehlterStudiengang());
        }
        studiengangChoicebox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            SchreiberLeser.getNutzerdaten().setAusgewaehlterStudiengang(newValue);
            semesterChoicebox.setItems(erzeugeSemesterListe(newValue));
            semesterChoicebox.getSelectionModel().select(0);
        });

        semesterChoicebox.disableProperty().bind(studiengangChoicebox.getSelectionModel().selectedItemProperty().isNull());
        semesterChoicebox.promptTextProperty().bind(Bindings
                .when(studiengangChoicebox.getSelectionModel().selectedItemProperty().isNull())
                .then("Bitte erst Studiengang wählen")
                .otherwise("Studiensemester wählen"));
        semesterChoicebox.setConverter(new StringConverter<Studiensemester>() {
            @Override
            public String toString(Studiensemester object) {
                return object.getName();
            }

            @Override
            public Studiensemester fromString(String string) {
                return null;
            }
        });
        if(SchreiberLeser.getNutzerdaten().getAusgewaehlterStudiengang()!=null)
        {
            semesterChoicebox.setItems(FXCollections.observableList(SchreiberLeser.getNutzerdaten().getAusgewaehlterStudiengang().getStudiensemester()));
        }

        if(SchreiberLeser.getNutzerdaten().getAusgewaehltesStudiensemester()!=null)
        {
            semesterChoicebox.setValue(SchreiberLeser.getNutzerdaten().getAusgewaehltesStudiensemester());
        }
        semesterChoicebox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            SchreiberLeser.getNutzerdaten().setAusgewaehltesStudiensemester(newValue);
        });

        darkmodeCheckBox.selectedProperty().setValue(SchreiberLeser.getNutzerdaten().getAktuellesThema().equals(Thema.DUNKEL));
        darkmodeCheckBox.textProperty().bind(Bindings
                .when(darkmodeCheckBox.selectedProperty())
                .then("aktiv")
                .otherwise("inaktiv"));
        darkmodeCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
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

    //
    @FXML private void manuellZuruecksetzen()
    {
        SchreiberLeser.alleDatenLoeschen();
        Platform.exit();
        System.exit(0);
    }

    //
    private ObservableList<Studiensemester> erzeugeSemesterListe(Studiengang studiengang) {
        List<List<Studiensemester>> studiensemester = SchreiberLeser.getDropdownMenue().getStudiengaenge().stream()
                .filter((item) -> item.equals(studiengang))
                .map(Studiengang::getStudiensemester)
                .collect(Collectors.toList());

        return FXCollections.observableList(studiensemester.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
    }
}
