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

            SchreiberLeser.getStudiengangInformationen().getModulhandbuch().forEach((fach) -> {
                GridPane gpFachInfos = new GridPane();
                gpFachInfos.setVgap(5);
                ColumnConstraints columnConstraints = new ColumnConstraints();
                columnConstraints.setMinWidth(250);
                gpFachInfos.getColumnConstraints().addAll(columnConstraints, columnConstraints);

                TitledPane tP = new TitledPane(fach.getFachName(), gpFachInfos);
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
                        new Label(fach.getFachDozent()),
                        new Label(fach.getFachZweitPruefer()),
                        new Label(String.valueOf(fach.getFachStudienjahr())),
                        new Label(fach.getFachArt()),
                        new Label(String.valueOf(fach.getFachECTS())),
                        new Label(String.valueOf(fach.getFachSWS())),
                        new Label(String.valueOf(fach.getFachPraesenzZeit())),
                        new Label(String.valueOf(fach.getFachPruefungsVorbereitung())),
                        new Label(fach.getFachSprache()),
                        new Label(fach.getFachLehrinhalte()),
                        new Label(fach.getFachLernziel()),
                        new Label(fach.getFachVoraussetzung()),
                        new Label(fach.getFachLiteraturliste()),
                        new Label(fach.getFachPruefungsdurchfuehrung()),
                        new Label(fach.getFachHilfsmittel()),
                        new Label(fach.getFachMedienformen()),
                        new Label(fach.getFachHaeufigkeit())
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
