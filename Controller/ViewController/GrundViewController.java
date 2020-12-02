package Controller.ViewController;

import javafx.animation.FadeTransition;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class GrundViewController implements Initializable {

    @FXML
    private BorderPane borderPane;

    @FXML
    private StackPane stackPane;

    @FXML
    private Label ort;

    @FXML
    private Button menuHauptButton;

    private GridPane gridPane;
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        _setHauptButton();
        _initialisiereMenue(70,
                new _MenuepunktInformationen("Bayernfahrplan","bayernfahrplan-icon.png", "QuicklinksView.fxml"),
                new _MenuepunktInformationen("Bayernfahrplan","bayernfahrplan-icon.png", "QuicklinksView.fxml"),
                new _MenuepunktInformationen("Bayernfahrplan","bayernfahrplan-icon.png", "QuicklinksView.fxml"),
                new _MenuepunktInformationen("Bayernfahrplan","bayernfahrplan-icon.png", "QuicklinksView.fxml"),
                new _MenuepunktInformationen("Bayernfahrplan","bayernfahrplan-icon.png", "QuicklinksView.fxml"),
                new _MenuepunktInformationen("Bayernfahrplan","bayernfahrplan-icon.png", "QuicklinksView.fxml"),
                new _MenuepunktInformationen("Bayernfahrplan","bayernfahrplan-icon.png", "QuicklinksView.fxml"),
                new _MenuepunktInformationen("Bayernfahrplan","bayernfahrplan-icon.png", "QuicklinksView.fxml"),
                new _MenuepunktInformationen("Bayernfahrplan","bayernfahrplan-icon.png", "QuicklinksView.fxml"),
                new _MenuepunktInformationen("Bayernfahrplan","bayernfahrplan-icon.png", "QuicklinksView.fxml"));
        _setTheme(true);
    }

    @FXML
    public void menuOeffnen()
    {
        _fadeTransitionStandard(anchorPane, 300, true);
        stackPane.getChildren().add(anchorPane);
    }

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

    private void _setHauptButton()
    {
        ImageView view = new ImageView(new Image(getClass().getResourceAsStream("../../Ressourcen/Grafiken/dots-menu.png")));
        view.setFitHeight(35);
        view.setPreserveRatio(true);

        menuHauptButton.setGraphic(view);
    }

    private void _initialisiereMenue(int menuepunktHoeheBreite, _MenuepunktInformationen... menuepunktInformationen)
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
        for(int i=0; i<menuepunktInformationen.length; i++)
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

            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("../../Ressourcen/Grafiken/"+menuepunktInformationen[i].iconDateiname)));
            imageView.setFitHeight(menuepunktHoeheBreite-30);
            imageView.setPreserveRatio(true);

            Button button=new Button();
            int finalI=i;
            button.setOnAction((actionEvent)->
                {
                    __ladeScene(menuepunktInformationen[finalI].fxmlDateiname);
                    ort.setText(menuepunktInformationen[finalI].label);
                });
            button.setTooltip(new Tooltip(menuepunktInformationen[i].label));
            button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            button.setGraphic(imageView);
            button.getStyleClass().add("icon-menu-button");

            gridPane.add(button, i%3, k);
        }

        //AnchorPane initialisieren
        anchorPane=new AnchorPane();
        AnchorPane.setTopAnchor(gridPane, 70.0);
        AnchorPane.setRightAnchor(gridPane, 10.0);
        anchorPane.getChildren().add(gridPane);
        anchorPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)-> {
            _fadeTransitionStandard(anchorPane, 300, false);
            stackPane.getChildren().remove(anchorPane);
        });
    }

    private void __ladeScene(String FXMLDateiname)
    {
        try
        {
            borderPane.setCenter(FXMLLoader.load(getClass().getResource("../../View/"+FXMLDateiname)));
            stackPane.getChildren().remove(anchorPane);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    static class _MenuepunktInformationen
    {
        private String label;
        private String iconDateiname;
        private String fxmlDateiname;

        public _MenuepunktInformationen(String label, String iconDateiname, String fxmlDateiname)
        {
            this.label=label;
            this.iconDateiname=iconDateiname;
            this.fxmlDateiname=fxmlDateiname;
        }
    }



    public void _setTheme(Boolean themaStatus) {
        if(themaStatus) {
            // Edit elements for changing color while using dark mode
        }
    }
}
