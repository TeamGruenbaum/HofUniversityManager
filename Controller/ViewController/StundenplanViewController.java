package Controller.ViewController;

import Controller.InformationsVermittlung.Datenabrufer;
import Controller.Speicher.SchreiberLeser;
import Model.DropdownModel.Studiengang;
import Model.DropdownModel.Studiensemester;
import Model.NutzerdatenModel.Doppelstunde;
import Model.NutzerdatenModel.Thema;
import javafx.beans.binding.Bindings;
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

public class StundenplanViewController implements Initializable {

    public void initialize(URL location, ResourceBundle resources) {
        Datenabrufer.stundenplanAbrufen();
        SchreiberLeser.getNutzerdaten().getDoppelstunden().stream()
                .map(Doppelstunde::getName)
                .forEach(System.out::println);
    }

    @FXML
    private void stundenplanAbrufen() {
    }
}
