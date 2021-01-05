package Controller.ViewController;


import Controller.InformationsVermittlung.Datenabrufer;
import Controller.Main;
import Controller.Speicher.SchreiberLeser;
import Model.DropdownModel.Studiengang;
import Model.DropdownModel.Studiensemester;
import Model.NutzerdatenModel.Thema;
import Model.OberflaechenModel.Blende;
import Model.OberflaechenModel.Menue;
import Model.OberflaechenModel.MenuepunktInformation;
import Model.NutzerdatenModel.Anwendung;
import Model.QuicklinksModel.Quicklinks;
import java.io.IOException;
import javafx.animation.FadeTransition;

import Model.NutzerdatenModel.Nutzerdaten;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import java.util.Optional;
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
    private Studiengang letzterStudiengang;
    private Studiensemester letztesStudiensemester;

    private static WebView uglyWebView;
    private static Button uglyMenuHauptButton;

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
        gridPane.getStyleClass().add("icon-menu-karte");
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

            button.setGraphic(imageView);
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
            hauptmenueSchließen();
        });

        oeffneScene();

        if(SchreiberLeser.isErsterStart())
        {
            ladeErsterStartScene();
        }

        uglyWebView=webView;
        uglyMenuHauptButton=menuHauptButton;
    }

    @FXML
    public void menuOeffnen()
    {
        fadeTransitionStandard(anchorPane, 300, Blende.EINBLENDEN);
        stackPane.getChildren().add(anchorPane);
    }

    public static void setThema(Thema thema)
    {
        ColorAdjust farbwechsel = new ColorAdjust();

        if(thema == Thema.DUNKEL) {
            Main.getRoot().setStyle("-menubar-color: #404040;" +
                    "-font-color: #000000;" +
                    "-anwendung-bgr: #a2a2a2;" +
                    "-accent-color: #318eb1;" +
                    "-warn-color: #691c1c;" +
                    "-menubar-text-color: white;" +
                    "-accent-color-accent: #266e8c");
            farbwechsel.setBrightness(1);
        } else {
            Main.getRoot().setStyle("-menubar-color: white;" +
                    "-font-color: #262626;" +
                    "-anwendung-bgr: #e2e2e2;" +
                    "-accent-color: #0072a0;" +
                    "-warn-color: #8a2828;" +
                    "-menubar-text-color: black;" +
                    "-accent-color-accent: #004a66");
            farbwechsel.setBrightness(0);
        }

        gridPane.getChildren().forEach((obj) -> {
            obj.setEffect(farbwechsel);
        });

        getUglyMenuHauptButton().setEffect(farbwechsel);
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
                if(SchreiberLeser.getNutzerdaten().getStudiengang()!=null && SchreiberLeser.getNutzerdaten().getStudiensemester()!=null)
                {
                    ProgressIndicator progressIndicator=ladeLadenScene();
                    hauptmenueSchließen();

                    if(letzterStudiengang==SchreiberLeser.getNutzerdaten().getStudiengang() && letztesStudiensemester==SchreiberLeser.getNutzerdaten().getStudiensemester())
                    {
                        ladeSceneMitScrollPane();
                    }
                    else
                    {

                        Task task = new Task<Void>()
                        {
                            @Override
                            protected Void call() throws Exception
                            {
                                menuHauptButton.setDisable(true);
                                Datenabrufer.studiengangAbrufen();
                                return null;
                            }
                        };

                        progressIndicator.progressProperty().addListener(((observable, oldValue, newValue) ->
                        {
                            if (newValue.doubleValue() == 1)
                            {
                                letzterStudiengang=SchreiberLeser.getNutzerdaten().getStudiengang();
                                letztesStudiensemester=SchreiberLeser.getNutzerdaten().getStudiensemester();

                                ladeSceneMitScrollPane();
                                menuHauptButton.setDisable(false);
                                studiengangEinmalHeruntergeladen = true;
                            }
                        }));

                        new Thread(task).start();
                    }
                }
                else
                {
                    Alert alert=new Alert(Alert.AlertType.WARNING, "Der Studiengang und das Studiensemester müssen gesetzt werden, bevor du diese Funktion nutzen kannst!");
                    alert.setTitle("Studiengang und -semester setzen");
                    alert.setHeaderText("Warnung");
                    alert.initOwner(Main.getPrimaryStage());
                    alert.showAndWait();
                }
            }break;
            case PANOPTO:
            {
                Main.oeffneLinkInBrowser(Quicklinks.getPanoptoLink());
            }break;
            case NEXTCLOUD:
            {
                Main.oeffneLinkInBrowser(Quicklinks.getNextcloudLink());
            }break;
            case CAMPUSSPORT, BAYERNFAHRPLAN:
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
            case MOODLE, PRIMUSS:
            {
                if(SchreiberLeser.getNutzerdaten().getSsoLogin().getName().compareTo("")==0||SchreiberLeser.getNutzerdaten().getSsoLogin().getPasswort().compareTo("")==0)
                {
                    Alert alert=new Alert(Alert.AlertType.INFORMATION, "Du solltest deine Login-Daten in den Einstellungen hinterlegen!");
                    alert.setTitle("Login setzen");
                    alert.setHeaderText("Empfehlung");
                    alert.initOwner(Main.getPrimaryStage());
                    alert.showAndWait();
                }

                ladeSceneOhneScrollPane();
            }
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
        Timeline ablauf = new Timeline(
                new KeyFrame(Duration.ZERO, event -> fadeTransitionStandard(anchorPane, 300, Blende.AUSBLENDEN)),
                new KeyFrame(Duration.millis(300), event -> stackPane.getChildren().remove(anchorPane))
        );
        ablauf.play();
    }

    private void ladeSceneMitScrollPane()
    {
        try
        {
            ScrollPane sp = new ScrollPane();
            sp.setPrefViewportHeight(300);
            sp.setFitToHeight(true);
            sp.setFitToWidth(true);
            sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            sp.setContent(FXMLLoader.load(getClass().getResource("../../View/"+SchreiberLeser.getNutzerdaten().getLetzterGeoeffneterMenuepunkt().getFxmlDateiname())));
            borderPane.setCenter(sp);
            borderPane.getCenter().setViewOrder(1);
            borderPane.getStyleClass().add("anwendungsBereich");
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
            borderPane.getCenter().setViewOrder(1);
            hauptmenueSchließen();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private ProgressIndicator ladeLadenScene()
    {
        BorderPane borderPane=new BorderPane();
        borderPane.setMinHeight(Double.MIN_VALUE);
        borderPane.setMinWidth(Double.MIN_VALUE);

        ProgressBar progressBar=new ProgressBar();
        progressBar.setMinWidth(100);

        borderPane.setCenter(progressBar);
        this.borderPane.setCenter(borderPane);

        Datenabrufer.setProgressIndicator(progressBar);

        return progressBar;
    }

    private void ladeErsterStartScene()
    {
        menuHauptButton.setDisable(true);
        ort.setText("Erster Start");
        
        try
        {
            Node node=FXMLLoader.load(getClass().getResource("../../View/ErsterStartView.fxml"));
            ((Button) node.lookup("#losgehts")).setOnAction((actionEvent)->
            {
                menuHauptButton.setDisable(false);
                oeffneScene();
            });
            borderPane.setCenter(node);
        }catch(IOException e){}
    }

    //Hilfsmethoden für visuelle Effekte
    private void fadeTransitionStandard(Node node, int dauer, Blende blende) {
        FadeTransition ft = new FadeTransition(Duration.millis(dauer), node);
        if(blende == Blende.EINBLENDEN)
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
        FadeTransition ftHoverEnter = new FadeTransition(Duration.millis(180), imageView);
        ftHoverEnter.setFromValue(1.0);
        ftHoverEnter.setToValue(0.7);

        FadeTransition ftHoverExit = new FadeTransition(Duration.millis(180), imageView);
        ftHoverExit.setFromValue(0.7);
        ftHoverExit.setToValue(1.0);

        button.setOnMouseEntered((MouseEvent) -> {
            ftHoverEnter.play();
        });

        button.setOnMouseExited((MouseEvent) -> {
            ftHoverExit.play();
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

    public static Button getUglyMenuHauptButton()
    {
        return uglyMenuHauptButton;
    }
}
