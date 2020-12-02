package Controller.ViewController;

import Model.NutzerdatenModel.Haupt;
import Model.QuicklinksModel.Quicklinks;
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
        webEngine.load(_linkWaehlen());
    }

    @FXML
    public void zurueckGehen(ActionEvent actionEvent)
    {
        webEngine.executeScript("history.back()");
    }

    private String _linkWaehlen()
    {
        switch(Haupt.getLetzteGeoeffneteAnwendung())
        {
            case MOODLE: return Quicklinks.getMoodleLink();
            case PANOPTO: return Quicklinks.getPanoptoLink();
            case NEXTCLOUD: return Quicklinks.getNextcloudLink();
            case CAMPUSSPORT: return Quicklinks.getCampusSportLink();
            case BAYERNFAHRPLAN: return Quicklinks.getBayernfahrplanLink();
            case PRIMUSS: return Quicklinks.getPrimussLink();
            default: return Quicklinks.getBayernfahrplanLink();
        }
    }
}
