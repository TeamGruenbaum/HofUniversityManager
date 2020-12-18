package Controller.ViewController;

import Controller.Speicher.SchreiberLeser;
import Model.NutzerdatenModel.Nutzerdaten;
import Model.QuicklinksModel.Quicklinks;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class QuicklinksViewController implements Initializable
{
    @FXML
    private WebView webview;

    @FXML
    private ProgressIndicator progressIndicator;

    private WebEngine webEngine;
    private boolean ersterLoginVersuch=true;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        webEngine=webview.getEngine();

        progressIndicator.progressProperty().bind(webEngine.getLoadWorker().progressProperty());

        webEngine.getLoadWorker().stateProperty().addListener(((observable, oldValue, newValue)->
        {
            if(newValue==Worker.State.SUCCEEDED)
            {
                progressIndicator.setVisible(false);
            }
            else
            {
                progressIndicator.setVisible(true);
            }
        }));

        switch(SchreiberLeser.getNutzerdaten().getLetzteGeoeffneteAnwendung())
        {
            case MOODLE:
            {
                webEngine.load(Quicklinks.getMoodleLink());
                loginSSO();
            } break;
            case PANOPTO:
            {
                webEngine.load(Quicklinks.getPanoptoLink());
                loginSSO();
            } break;
            case NEXTCLOUD:
            {
                webEngine.load(Quicklinks.getNextcloudLink());
                loginSSO();
            } break;
            case CAMPUSSPORT:
            {
                webEngine.load(Quicklinks.getCampusSportLink());
            } break;
            case BAYERNFAHRPLAN:
            {
                webEngine.load(Quicklinks.getBayernfahrplanLink());
            } break;
            case PRIMUSS:
            {
                webEngine.load(Quicklinks.getPrimussLink());
                loginSSO();
            } break;
            default:
            {
                webEngine.loadContent
                (
                    "<html>" +
                        "<header>" +
                        "</header>" +
                        "<body>" +
                            "<p>Es gab einen Fehler beim Laden der Website</p>" +
                        "</body>" +
                    "</html>"
                );
            } break;
        }
    }

    private void loginSSO()
    {
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue)->
        {
            if(ersterLoginVersuch && newValue==Worker.State.SUCCEEDED)
            {
                try
                {
                    webEngine.executeScript("document.getElementById('username').value='mmustermann'");
                    webEngine.executeScript("document.getElementById('password').value=''");
                    webEngine.executeScript("document.getElementsByName('_eventId_proceed')[0].click()");
                }catch(Exception exception){}
                ersterLoginVersuch=false;
            }
        });
    }

    @FXML
    public void zurueckGehen(ActionEvent actionEvent)
    {
        webEngine.executeScript("history.back()");
    }
}
