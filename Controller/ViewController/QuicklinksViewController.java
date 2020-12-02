package Controller.ViewController;

import Model.QuicklinksModel.QuicklinksModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class QuicklinksViewController implements Initializable
{
    @FXML
    private WebView webview;

    private WebEngine webEngine;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        webEngine=webview.getEngine();
        webEngine.load(QuicklinksModel.getOepnvLink());
    }

    public void zurueckGehen(ActionEvent actionEvent)
    {
        webEngine.executeScript("history.back()");
    }
}
