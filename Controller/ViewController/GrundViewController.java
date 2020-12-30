package Controller.ViewController;


import Controller.InformationsVermittlung.Datenabrufer;
import Controller.Main;
import Controller.Speicher.SchreiberLeser;
import Model.NutzerdatenModel.Thema;
import Model.OberflaechenModel.Menue;
import Model.OberflaechenModel.MenuepunktInformation;
import Model.NutzerdatenModel.Anwendung;
import Model.QuicklinksModel.Quicklinks;
import javafx.animation.FadeTransition;

import Model.NutzerdatenModel.Nutzerdaten;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.Window;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static Model.NutzerdatenModel.Anwendung.*;

public class GrundViewController implements Initializable {

    //Attribute auf welche durch mehrere Methoden zugegriffen wird
    private static GridPane gridPane;
    private AnchorPane anchorPane;

    //Methoden und Attribute für den Zugriff auf die View
    @FXML
    private BorderPane borderPane;

    @FXML
    private StackPane stackPane;

    @FXML
    private Label ort;

    @FXML
    private Button menuHauptButton;

    @FXML
    private WebView webView;

    private boolean mensaplanEinmalHeruntergeladen, studiengangEinmalHeruntergeladen, treffpunkteEinmalHeruntergeladen;

    private static WebView uglyWebView;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        mensaplanEinmalHeruntergeladen=false;
        studiengangEinmalHeruntergeladen=false;
        treffpunkteEinmalHeruntergeladen=false;

        //Initialisieren des Hauptmenuebuttons
        ImageView view = new ImageView(new Image(getClass().getResourceAsStream("../../Ressourcen/Grafiken/dots-menu.png")));
        view.setFitHeight(35);
        view.setPreserveRatio(true);

        menuHauptButton.setGraphic(view);

        hoverIconsEffect(menuHauptButton, view);

        //Initialisieren des Hauptmenues
        int menuepunktHoeheBreite=90;

        //GridPane initialisieren
        gridPane=new GridPane();
        gridPane.getStyleClass().add("icon-menu");
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setMinWidth(menuepunktHoeheBreite);
        columnConstraints.setMaxWidth(menuepunktHoeheBreite);
        gridPane.getColumnConstraints().addAll(columnConstraints, columnConstraints, columnConstraints);
        gridPane.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)-> {
            mouseEvent.consume();
        });

        //Buttons erstellen und initialisieren
        int k=0;
        for(int i=0; i<Menue.getMenuepunkte().size(); i++)
        {
            if(i%3==0)
            {
                if(i!=0)
                {
                    k++;
                }

                RowConstraints rowConstraints = new RowConstraints();
                rowConstraints.setMinHeight(menuepunktHoeheBreite);
                rowConstraints.setMaxHeight(menuepunktHoeheBreite);
                gridPane.getRowConstraints().add(rowConstraints);
            }

            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("../../Ressourcen/Grafiken/"+Menue.getMenuepunkte().get(i).getIconDateiname())));
            imageView.setFitHeight(menuepunktHoeheBreite-55);
            imageView.setPreserveRatio(true);

            Button button=new Button();
            int finalI=i;
            button.setOnAction((actionEvent)->
            {
                SchreiberLeser.getNutzerdaten().setLetzterGeoeffneterMenuepunkt(Menue.getMenuepunkte().get(finalI));

                oeffneScene();
            });
            button.setTooltip(new Tooltip(grossschreiben(Menue.getMenuepunkte().get(i).getAnwendung().toString())));
            button.getTooltip().getStyleClass().add("breadcrumb-menu");
            button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            //Region icon = new Region();
            //icon.getStyleClass().add("icon");

            //button.setGraphic(icon);
            button.setGraphic(imageView);
            //button.getStyleClass().add("icon-button");
            button.getStyleClass().add("icon-menu-button");

            hoverIconsEffect(button, imageView);

            gridPane.add(button, i%3, k);
        }

        //AnchorPane initialisieren
        anchorPane=new AnchorPane();
        AnchorPane.setTopAnchor(gridPane, 70.0);
        AnchorPane.setRightAnchor(gridPane, 10.0);
        anchorPane.getChildren().add(gridPane);
        anchorPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)-> {
            // fadeTransitionStandard(anchorPane, 300, false); // geht nicht, liegt am remove, das wohl zu schnell geht... ANSCHAUEN
            stackPane.getChildren().remove(anchorPane);
        });

        oeffneScene();

        uglyWebView=webView;
    }

    @FXML
    public void menuOeffnen()
    {
        fadeTransitionStandard(anchorPane, 300, true);
        stackPane.getChildren().add(anchorPane);
    }

    public static void setThema(Thema thema)
    {
        gridPane.getChildren().forEach((obj) -> {
            ColorAdjust farbwechsel = new ColorAdjust();
            farbwechsel.setBrightness((thema == Thema.HELL)?0:1);
            obj.setEffect(farbwechsel);
        });

        //ColorAdjust aufhellen = new ColorAdjust();
        //aufhellen.setBrightness((thema == Thema.HELL)?0:1);
        //menuHauptButton.setEffect(aufhellen);

        if(thema == Thema.DUNKEL) {
            Main.getRoot().setStyle("-menubar-color: #404040;" +
                    "-font-color: #262626;" +
                    "-anwendung-bgr: #a2a2a2;" +
                    "-accent-color: #45cbff;" +
                    "-warn-color: #691c1c;" +
                    "-menubar-text-color: white");
            //System.out.println("Übergabe, dass nun auf Darkmode geschaltet werden soll");
        } else {
            Main.getRoot().setStyle("-menubar-color: white;" +
                    "-font-color: #262626;" +
                    "-anwendung-bgr: lightgrey;" +
                    "-accent-color: #0072a0;" +
                    "-warn-color: #8a2828;" +
                    "-menubar-text-color: black");

            //System.out.println("Übergabe, dass auf LightMode geschaltet werden soll");
        }
    }

    //Hilfsmethoden und Hilfsklasse allgemeiner Art
    private void oeffneScene()
    {
        ort.setText(grossschreiben(SchreiberLeser.getNutzerdaten().getLetzterGeoeffneterMenuepunkt().getAnwendung().toString()));

        switch(SchreiberLeser.getNutzerdaten().getLetzterGeoeffneterMenuepunkt().getAnwendung())
        {
            case STUNDENPLAN:
            {
                ladeSceneMitScrollPane();
            }break;
            case MENSAPLAN:
            {
                ladeLadenScene();
                hauptmenueSchließen();

                if(mensaplanEinmalHeruntergeladen)
                {
                    ladeSceneMitScrollPane();
                }
                else
                {
                    Task task=new Task<Void>()
                    {
                        @Override
                        protected Void call() throws Exception
                        {
                            menuHauptButton.setDisable(true);
                            Datenabrufer.mensaplanAbrufen();
                            return null;
                        }
                    };

                    task.stateProperty().addListener(((observable, oldValue, newValue) ->
                    {
                        if(newValue== Worker.State.SUCCEEDED)
                        {
                            ladeSceneMitScrollPane();
                            menuHauptButton.setDisable(false);
                            mensaplanEinmalHeruntergeladen=true;
                        }
                    }));

                    new Thread(task).start();
                }
            } break;
            case STUDIENGANG:
            {
                Datenabrufer.studiengangAbrufen();
                ladeSceneMitScrollPane();
            }break;
            case MOODLE:
            {
                ladeSceneOhneScrollPane();
            }break;
            case PANOPTO:
            {
                Main.oeffneLinkInBrowser(Quicklinks.getPanoptoLink());
            }break;
            case NEXTCLOUD:
            {
                Main.oeffneLinkInBrowser(Quicklinks.getNextcloudLink());
            }break;
            case CAMPUSSPORT:
            {
                ladeSceneOhneScrollPane();
            }break;
            case TREFFPUNKTE:
            {
                ladeLadenScene();
                hauptmenueSchließen();

                if(treffpunkteEinmalHeruntergeladen)
                {
                    ladeSceneMitScrollPane();
                }
                else
                {
                    Task task=new Task<Void>()
                    {
                        @Override
                        protected Void call() throws Exception
                        {
                            menuHauptButton.setDisable(true);
                            Datenabrufer.treffpunkteAbrufen();
                            return null;
                        }
                    };

                    task.stateProperty().addListener(((observable, oldValue, newValue) ->
                    {
                        if(newValue== Worker.State.SUCCEEDED)
                        {
                            ladeSceneMitScrollPane();
                            menuHauptButton.setDisable(false);
                            treffpunkteEinmalHeruntergeladen=true;
                        }
                    }));

                    new Thread(task).start();
                }
            }break;
            case BAYERNFAHRPLAN:
            {
                ladeSceneOhneScrollPane();
            }break;
            case PRIMUSS:
            {
                ladeSceneOhneScrollPane();
            }break;
            case EINSTELLUNGEN:
            {
                ladeSceneMitScrollPane();
            }break;
            default:
            {
                ladeSceneMitScrollPane();
            }
        }
    }

    private void hauptmenueSchließen()
    {
        stackPane.getChildren().remove(anchorPane);
    }

    private void ladeSceneMitScrollPane()
    {
        try
        {
            ScrollPane sp = new ScrollPane();
            sp.setPrefViewportHeight(300);
            sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            sp.setContent(FXMLLoader.load(getClass().getResource("../../View/"+SchreiberLeser.getNutzerdaten().getLetzterGeoeffneterMenuepunkt().getFxmlDateiname())));
            borderPane.setCenter(sp);
            //borderPane.setCenter(FXMLLoader.load(getClass().getResource("../../View/"+SchreiberLeser.getNutzerdaten().getLetzterGeoeffneterMenuepunkt().getFxmlDateiname())));
            borderPane.getCenter().setViewOrder(0);
            hauptmenueSchließen();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void ladeSceneOhneScrollPane()
    {
        try
        {
            borderPane.setCenter(FXMLLoader.load(getClass().getResource("../../View/"+SchreiberLeser.getNutzerdaten().getLetzterGeoeffneterMenuepunkt().getFxmlDateiname())));
            borderPane.getCenter().setViewOrder(0);
            hauptmenueSchließen();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void ladeLadenScene()
    {
        BorderPane borderPane=new BorderPane();
        borderPane.setMinHeight(Double.MIN_VALUE);
        borderPane.setMinWidth(Double.MIN_VALUE);

        ProgressBar progressBar=new ProgressBar();
        progressBar.setMinWidth(100);

        borderPane.setCenter(progressBar);
        this.borderPane.setCenter(borderPane);

        Datenabrufer.setProgressIndicator(progressBar);
    }

    //Hilfsmethoden für visuelle Effekte
    private void fadeTransitionStandard(Node node, int dauer, Boolean io) {
        FadeTransition ft = new FadeTransition(Duration.millis(dauer), node);
        if(io)
        {
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
        }
        else
        {
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
        }
        ft.play();
    }

    private void hoverIconsEffect(Node button, ImageView imageView)
    {
        Thema aktuellesThema = SchreiberLeser.getNutzerdaten().getAktuellesThema();

        button.setOnMouseMoved((MouseEvent) -> {
            ColorAdjust aufhellen = new ColorAdjust();
            aufhellen.setBrightness(0.3);

            imageView.setEffect(aufhellen);
            imageView.setCache(true);
            imageView.setCacheHint(CacheHint.SPEED);
        });

        button.setOnMouseExited((MouseEvent) -> {
            ColorAdjust abdunkeln = new ColorAdjust();
            abdunkeln.setBrightness(-0.3);

            imageView.setEffect(abdunkeln);
            imageView.setCache(true);
            imageView.setCacheHint(CacheHint.SPEED);
        });
    }

    //Weitere
    private String grossschreiben(String wort)
    {
        return wort.substring(0,1).toUpperCase() + wort.substring(1).toLowerCase();
    }

    public static WebView getUglyWebview()
    {
        return uglyWebView;
    }
}
