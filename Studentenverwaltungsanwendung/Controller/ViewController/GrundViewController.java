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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class GrundViewController implements Initializable {

    @FXML
    StackPane stackPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editMenuButton();
        setTheme(true);
    }

    @FXML
    public void menuOeffnen()
    {
        /*try
        {
            GridPane root = (GridPane) FXMLLoader.load(getClass().getResource("../View/Menu.fxml"));
        }
        catch (Exception e)
        {
            return;
        }*/

        
        GridPane gridPane=new GridPane();
        gridPane.setStyle("-fx-background-color: black;");
        gridPane.setMinSize(200, 200);

        AnchorPane anchorPane=new AnchorPane();
        AnchorPane.setTopAnchor(gridPane, 60.0);
        AnchorPane.setRightAnchor(gridPane, 10.0);
        anchorPane.getChildren().add(gridPane);

        gridPane.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)-> {
            mouseEvent.consume();
        });

        anchorPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)-> {
            System.out.println("ANCHORPANE");
            stackPane.getChildren().remove(anchorPane);
        });

        stackPane.getChildren().add(anchorPane);
    }

    public void editMenuButton() {
        // Menu-Button with Image
        Image openIcon = new Image(getClass().getResourceAsStream("../../img/dots-menu.png"));
        ImageView openIconView = new ImageView(openIcon);
        openIconView.setFitHeight(30);
        openIconView.setPreserveRatio(true);
    }

    public void setTheme(Boolean themeStatus) {
        if(themeStatus) {
            // Edit elements for changing color while using dark mode
        }
    }
}
