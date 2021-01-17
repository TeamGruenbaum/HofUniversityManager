package Controller.ViewController;



import Controller.Main;
import Controller.Speicher.SchreiberLeser;
import Model.QuicklinksModel.Quicklinks;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.codefx.libfx.control.webview.WebViews;

import javax.swing.event.HyperlinkEvent;
import java.net.URL;
import java.util.ResourceBundle;



public class QuicklinksViewController implements Initializable
{
    @FXML private WebView anzeigeWebWiew;
    @FXML private ProgressIndicator websiteLadeProgressIndicator;

    private WebEngine anzeigeWebEngine;
    private String letztGeklickteURL;
    private boolean ersterLoginVersuch=true;



    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        anzeigeWebEngine=anzeigeWebWiew.getEngine();

        WebViews.addHyperlinkListener(anzeigeWebWiew, (event) ->
        {
            try
            {
                letztGeklickteURL=event.getURL().toString();
            }
            catch(Exception exception)
            {
                letztGeklickteURL=null;
            }
            return false;
        }, HyperlinkEvent.EventType.ACTIVATED);
        
        websiteLadeProgressIndicator.progressProperty().bind(anzeigeWebEngine.getLoadWorker().progressProperty());
        anzeigeWebEngine.getLoadWorker().stateProperty().addListener(((observable, oldValue, newValue)->
        {

            if(newValue==Worker.State.SUCCEEDED)
            {
                websiteLadeProgressIndicator.setVisible(false);
            }
            else
            {
                if(newValue==Worker.State.CANCELLED)
                {
                    if(letztGeklickteURL!=null)
                    {
                        Main.oeffneLinkInBrowser(letztGeklickteURL);
                    }
                    websiteLadeProgressIndicator.setVisible(false);
                }
                else
                {
                    websiteLadeProgressIndicator.setVisible(true);
                }
            }
        }));

        switch(SchreiberLeser.getNutzerdaten().getLetzterGeoeffneterMenuepunkt().getZielanwendung())
        {
            case MOODLE:
            {
                anzeigeWebEngine.load(Quicklinks.getMoodleLink());
                loginInSSO();
            } break;
            case CAMPUSSPORT:
            {
                anzeigeWebEngine.load(Quicklinks.getCampusSportLink());
            } break;
            case BAYERNFAHRPLAN:
            {
                anzeigeWebEngine.load(Quicklinks.getBayernfahrplanLink());
            } break;
            case PRIMUSS:
            {
                anzeigeWebEngine.load(Quicklinks.getPrimussLink());
                loginInSSO();
            } break;
            default:
            {
                anzeigeWebEngine.loadContent
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


    //
    private void loginInSSO()
    {
        anzeigeWebEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue)->
        {
            if(ersterLoginVersuch && newValue==Worker.State.SUCCEEDED)
            {
                try
                {
                    anzeigeWebEngine.executeScript("document.getElementById('username').value='"+SchreiberLeser.getNutzerdaten().getSsoLogin().getName()+"'");
                    anzeigeWebEngine.executeScript("document.getElementById('password').value='"+SchreiberLeser.getNutzerdaten().getSsoLogin().getPasswort()+"'");
                    anzeigeWebEngine.executeScript("document.getElementsByName('_eventId_proceed')[0].click()");
                }catch(Exception exception){}
                ersterLoginVersuch=false;
            }
        });
    }

    //
    @FXML public void zurueckGehen(ActionEvent actionEvent)
    {
        anzeigeWebEngine.executeScript("history.back()");
    }

    //
    @FXML private void vorwärtsGehen(ActionEvent actionEvent)
	{
        anzeigeWebEngine.executeScript("history.forward()");
	}
}
