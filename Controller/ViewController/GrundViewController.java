package Controller.ViewController;


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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.CacheHint;
import javafx.scene.Node;
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

import static Model.NutzerdatenModel.Anwendung.EINSTELLUNGEN;

public class GrundViewController implements Initializable {

    //Attribute auf welche durch mehrere Methoden zugegriffen wird
    private GridPane gridPane;
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

    private static WebView webView2;

    public GrundViewController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        _setHauptButton();

        _initialisiereMenue(90, Menue.getMenuepunkte());

        _oeffneLetzteScene();

        webView2=webView;
    }

    @FXML
    public void menuOeffnen()
    {
        _fadeTransitionStandard(anchorPane, 300, true);
        stackPane.getChildren().add(anchorPane);
    }

    //Methoden zur Initalisierung von Layoutelementen
    private void _setHauptButton()
    {
        ImageView view = new ImageView(new Image(getClass().getResourceAsStream("../../Ressourcen/Grafiken/dots-menu.png")));
        view.setFitHeight(35);
        view.setPreserveRatio(true);

        menuHauptButton.setGraphic(view);

        _hoverIconsEffect(menuHauptButton, view);
    }

    private void _initialisiereMenue(int menuepunktHoeheBreite, ArrayList<MenuepunktInformation> menuepunktInformationen)
    {
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
        for(int i=0; i<menuepunktInformationen.size(); i++)
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

            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("../../Ressourcen/Grafiken/"+menuepunktInformationen.get(i).iconDateiname)));
            imageView.setFitHeight(menuepunktHoeheBreite-55);
            imageView.setPreserveRatio(true);

            Button button=new Button();
            int finalI=i;
            button.setOnAction((actionEvent)->
                {
                    SchreiberLeser.getNutzerdaten().setLetzteGeoeffneteAnwendung(menuepunktInformationen.get(finalI).anwendung);

                    _ladeScene(menuepunktInformationen.get(finalI));
                });
            button.setTooltip(new Tooltip(_grossschreiben(menuepunktInformationen.get(i).anwendung.toString())));
            button.getTooltip().getStyleClass().add("breadcrumb-menu");
            button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            button.setGraphic(imageView);
            button.getStyleClass().add("icon-menu-button");

            _hoverIconsEffect(button, imageView);

            gridPane.add(button, i%3, k);
        }

        //AnchorPane initialisieren
        anchorPane=new AnchorPane();
        AnchorPane.setTopAnchor(gridPane, 70.0);
        AnchorPane.setRightAnchor(gridPane, 10.0);
        anchorPane.getChildren().add(gridPane);
        anchorPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)-> {
            // _fadeTransitionStandard(anchorPane, 300, false); // geht nicht, liegt am remove, das wohl zu schnell geht... ANSCHAUEN
            stackPane.getChildren().remove(anchorPane);
        });
    }

    public static void _setTheme(Thema thema)
    {
        if(thema == Thema.DUNKEL) {
            Main.getRoot().setStyle("-menubar-color: darkgrey;" +
                    "-font-color: white;" +
                    "-anwendung-bgr: #434343;" +
                    "-accent-color: #003c54;" +
                    "-warn-color: #ff3e3e;");
            //System.out.println("Übergabe, dass nun auf Darkmode geschaltet werden soll");
        } else {
            Main.getRoot().setStyle("-menubar-color: white;" +
                    "-font-color: black;" +
                    "-anwendung-bgr: lightgrey;" +
                    "-accent-color: #0072a0;" +
                    "-warn-color: #8a2828");

            //System.out.println("Übergabe, dass auf LightMode geschaltet werden soll");
        }
    }

    private void _oeffneLetzteScene()
    {
        /*switch(STUNDENPLAN)
        {
            case STUNDENPLAN: _ladeScene("Platzhalter.fxml"); break;
            case MENSAPLAN: _ladeScene("Platzhalter.fxml"); break;
            case STUDIENGANG: _ladeScene("Platzhalter.fxml"); break;
            case MOODLE: _ladeScene("QuicklinksView.fxml"); break;
            case PANOPTO: _ladeScene("Platzhalter.fxml"); break;
            case NEXTCLOUD: _ladeScene("QuicklinksView.fxml"); break;
            case CAMPUSSPORT: _ladeScene("Platzhalter.fxml"); break;
            case TREFFPUNKTE: _ladeScene("Platzhalter.fxml"); break;
            case BAYERNFAHRPLAN: _ladeScene("QuicklinksView.fxml"); break;
            case PRIMUSS: _ladeScene("QuicklinksView.fxml"); break;
            case EINSTELLUNGEN: _ladeScene("Platzhalter.fxml"); break;
            default: _ladeScene("Platzhalter.fxml"); break;
        }*/
    }

    //Hilfsmethoden und Hilfsklasse allgemeiner Art
    private String _grossschreiben(String wort)
    {
        return wort.substring(0,1).toUpperCase() + wort.substring(1).toLowerCase();
    }

    private void _ladeScene(MenuepunktInformation wert)
    {
        if(SchreiberLeser.getNutzerdaten().getLetzteGeoeffneteAnwendung()==Anwendung.NEXTCLOUD)
        {
            Main.oeffneLinkInBrowser(Quicklinks.getNextcloudLink());
            return;
        }

        if(SchreiberLeser.getNutzerdaten().getLetzteGeoeffneteAnwendung()==Anwendung.PANOPTO)
        {
            Main.oeffneLinkInBrowser(Quicklinks.getPanoptoLink());
            return;
        }

        try
        {
            ort.setText(_grossschreiben(wert.anwendung.toString()));

            ScrollPane sp = new ScrollPane();
            sp.setPrefViewportHeight(300);
            sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            sp.setContent(FXMLLoader.load(getClass().getResource("../../View/"+wert.fxmlDateiname)));
            borderPane.setCenter(sp);
            borderPane.getCenter().setViewOrder(0);
            stackPane.getChildren().remove(anchorPane);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //Hilfsmethoden für visuelle Effekte
    private void _fadeTransitionStandard(Node node, int dauer, Boolean io) {
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

    private void _hoverIconsEffect(Node button, ImageView imageView)
    {
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

    public static WebView getUglyWebview()
    {
        return webView2;
    }
}
