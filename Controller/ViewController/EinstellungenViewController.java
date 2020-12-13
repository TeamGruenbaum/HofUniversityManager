package Controller.ViewController;

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
        // Get Status of the Setting Dark Mode!
        // Set ChckboxText from Model-Info!

        cbDarkMode.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(!oldValue && newValue) {
                //System.out.println("DARKMODE AKTIV GESETZT");
                cbDarkMode.setText("aktiv");
                GrundViewController._setTheme(true);
                // Übertragung der Information an das Model, um dann bei Neustart im Initialize anzufragen (und bei Neuaufruf)
            }
            if(oldValue && !newValue) {
                //System.out.println("DARKMODE INAKTIV GESETZT");
                cbDarkMode.setText("inaktiv");
                GrundViewController._setTheme(false);
            }
        });
    }
}
