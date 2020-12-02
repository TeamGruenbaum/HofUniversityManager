package Controller.ViewController;

import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class GrundViewController implements Initializable {

    @FXML
    private Pane pane;

    @FXML
    private StackPane stackPane;

    private GridPane gridPane;
    private AnchorPane anchorPane;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        _initialisiereMenue(70,
                new _MenuepunktInformationen("Bayernfahrplan","Bayern_Fahrplan_Icon.png", "GrundView.fxml"),
                new _MenuepunktInformationen("Bayernfahrplan","Bayern_Fahrplan_Icon.png", "GrundView.fxml"),
                new _MenuepunktInformationen("Bayernfahrplan","Bayern_Fahrplan_Icon.png", "GrundView.fxml"),
                new _MenuepunktInformationen("Bayernfahrplan","Bayern_Fahrplan_Icon.png", "GrundView.fxml"));
        _setTheme(true);
    }

    @FXML
    public void menuOeffnen()
    {
        stackPane.getChildren().add(anchorPane);
    }



    private void _initialisiereMenue(int menuepunktHoeheBreite, _MenuepunktInformationen... menuepunktInformationen)
    {
        //GridPane initialisieren
        gridPane=new GridPane();
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
            imageView.setFitHeight(menuepunktHoeheBreite-20);
            imageView.setPreserveRatio(true);

            Button button=new Button();
            int finalI=i;
            button.setOnAction((actionEvent)->{__ladeScene(menuepunktInformationen[finalI].fxmlDateiname);});
            button.setTooltip(new Tooltip(menuepunktInformationen[i].label));
            button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            button.setGraphic(imageView);

            gridPane.add(button, i%3, k);
        }

        //AnchorPane initialisieren
        anchorPane=new AnchorPane();
        AnchorPane.setTopAnchor(gridPane, 60.0);
        AnchorPane.setRightAnchor(gridPane, 10.0);
        anchorPane.getChildren().add(gridPane);
        anchorPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)-> {
            stackPane.getChildren().remove(anchorPane);
        });
    }

    private void __ladeScene(String FXMLDateiname)
    {
        try
        {
            pane.getChildren().clear();
            pane.getChildren().add(FXMLLoader.load(getClass().getResource("../../View/"+FXMLDateiname)));
        }
        catch(Exception e){}
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
