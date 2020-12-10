package Controller.ViewController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class EinstellungenViewController implements Initializable
{
    @FXML
    TextField tfBenutzername;

    @FXML
    PasswordField pfPasswort;

    @FXML
    ChoiceBox cbStudiengang;

    @FXML
    ChoiceBox cbSemester;

    @FXML
    AnchorPane aP;

    public void initialize(URL location, ResourceBundle resources)
    {
        // Listener für Klick in leere Fläche -> defokussieren
    }
}
