package Controller.ViewController;

import Controller.Speicher.SchreiberLeser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudiengangViewController implements Initializable {

    @FXML
    VBox vbContent;

    @FXML
    Label studiengangTitel;

    public void initialize(URL location, ResourceBundle resources) {
        if((SchreiberLeser.getNutzerdaten().getStudiengang() == null) || (SchreiberLeser.getNutzerdaten().getStudiensemester() == null)) {
            Label fehlendeInformation = new Label("Leider hast Du in den Einstellungen keinen Studiengang definiert. Bitte definiere Deinen Studiengang!");
            fehlendeInformation.setWrapText(true);
            fehlendeInformation.getStyleClass().add("warnhinweis");
            vbContent.getChildren().add(fehlendeInformation);
        } else {
            studiengangTitel.setText("Studiengang: " + SchreiberLeser.getNutzerdaten().getStudiengang().getName() + " (" + SchreiberLeser.getNutzerdaten().getStudiengang().getKuerzel() + ", " + SchreiberLeser.getNutzerdaten().getStudiensemester().getName() + ")");
            studiengangTitel.setWrapText(true);

            // Erstelle Accordion mit Fächern
            Accordion contentAccordion = new Accordion();
            contentAccordion.setPrefWidth(500);
            vbContent.getChildren().add(contentAccordion);

            SchreiberLeser.getModulhandbuch().getModulhandbuchFaecher().forEach((fach) -> {
                GridPane gpFachInfos = new GridPane();
                gpFachInfos.setVgap(5);
                ColumnConstraints columnConstraints = new ColumnConstraints();
                columnConstraints.setMinWidth(250);
                gpFachInfos.getColumnConstraints().addAll(columnConstraints, columnConstraints);

                TitledPane tP = new TitledPane(fach.getName(), gpFachInfos);
                contentAccordion.getPanes().add(tP);

                ObservableList<Label> alBezeichnung = FXCollections.observableArrayList(List.of(
                        new Label("Dozent: "),
                        new Label("Zweitprüfer: "),
                        new Label("Studienjahr: "),
                        new Label("Art: "),
                        new Label("ECTS: "),
                        new Label("SWS: "),
                        new Label("Präsenzzeit: "),
                        new Label("Prüfungsvorbereitung: "),
                        new Label("Sprache: "),
                        new Label("Lehrinhalte: "),
                        new Label("Lernziel: "),
                        new Label("Voraussetzung: "),
                        new Label("Literaturliste: "),
                        new Label("Prüfungsdurchführung: "),
                        new Label("Hilfsmittel: "),
                        new Label("Medienformen: "),
                        new Label("Häufigkeit: ")
                ));

                ObservableList<Label> alInfo = FXCollections.observableArrayList(List.of(
                        new Label(fach.getDozent()),
                        new Label(fach.getZweitpruefer()),
                        new Label(String.valueOf(fach.getStudienjahr())),
                        new Label(fach.getArt()),
                        new Label(String.valueOf(fach.getECTS())),
                        new Label(String.valueOf(fach.getSemesterwochenstunden())),
                        new Label(String.valueOf(fach.getPraesenzzeit())),
                        new Label(String.valueOf(fach.getPruefungsvorbereitung())),
                        new Label(fach.getSprache()),
                        new Label(fach.getLehrinhalte()),
                        new Label(fach.getLernziel()),
                        new Label(fach.getVoraussetzung()),
                        new Label(fach.getLiteraturliste()),
                        new Label(fach.getPruefungsdurchfuehrung()),
                        new Label(fach.getHilfsmittel()),
                        new Label(fach.getMedienformen()),
                        new Label(fach.getHaeufigkeit())
                ));

                for(int i=0; i<alBezeichnung.size(); i++) {
                    alBezeichnung.get(i).setWrapText(true);
                    alInfo.get(i).setWrapText(true);
                    gpFachInfos.add(alBezeichnung.get(i), 0, i);
                    gpFachInfos.add(alInfo.get(i), 1, i);
                }
            });
        }
    }
}
