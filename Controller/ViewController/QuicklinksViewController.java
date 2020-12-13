package Controller.ViewController;

import Model.NutzerdatenModel.Nutzerdaten;
import Model.QuicklinksModel.Quicklinks;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
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
        switch(Nutzerdaten.getLetzteGeoeffneteAnwendung())
        {
            case MOODLE:
                {
                    final boolean[] ersterVersuch = {true};

                    webEngine.load(Quicklinks.getMoodleLink());

                    webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>()
                        {
                            @Override
                            public void changed(ObservableValue<? extends Worker.State> observable,Worker.State oldValue, Worker.State newValue )
                            {
                                if( newValue != Worker.State.SUCCEEDED ) {
                                    return;
                                }

                                if(ersterVersuch[0])
                                {
                                    webEngine.executeScript("document.getElementById('username').value='mmustermann'; document.getElementById('password').value='';");
                                    ersterVersuch[0] =false;
                                }
                            }
                        });


                } break;
            case PANOPTO: webEngine.load(Quicklinks.getPanoptoLink()); break;
            case NEXTCLOUD: webEngine.load(Quicklinks.getNextcloudLink()); break;
            case CAMPUSSPORT: webEngine.load(Quicklinks.getCampusSportLink()); break;
            case BAYERNFAHRPLAN: webEngine.load(Quicklinks.getBayernfahrplanLink());; break;
            case PRIMUSS: webEngine.load(Quicklinks.getPrimussLink()); break;
            default: Quicklinks.getBayernfahrplanLink(); break;
        }
    }

    @FXML
    public void zurueckGehen(ActionEvent actionEvent)
    {
        webEngine.executeScript("history.back()");
    }
}
