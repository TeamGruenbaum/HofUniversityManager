package StudiPlaner;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private MenuItem mb;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editMenuButton();
        setTheme(true);
    }

    public void editMenuButton() {
        // Menu-Button with Image
        Image openIcon = new Image(getClass().getResourceAsStream("img/dots-menu.png"));
        ImageView view = new ImageView(openIcon);
        view.setFitHeight(30);
        view.setPreserveRatio(true);

        mb.setGraphic(view);
    }

    public void setTheme(Boolean themeStatus) {
        if(themeStatus) {
            // Edit elements for changing color while using dark mode
        }
    }
}
