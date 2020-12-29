package Controller.ViewController;

import Controller.InformationsVermittlung.Datenabrufer;
import Controller.Main;
import Controller.Speicher.SchreiberLeser;
import Model.DropdownModel.Studiengang;
import Model.DropdownModel.Studiensemester;
import Model.NutzerdatenModel.Doppelstunde;
import Model.NutzerdatenModel.Thema;
import Model.StudiengangModel.ModulhandbuchFach;
import Model.StudiengangModel.StudiengangInformationen;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class StudiengangViewController implements Initializable {

    @FXML
    VBox vbContent;

    @FXML
    Label studiengangTitel;

    StudiengangInformationen testStudiengangInformation=new StudiengangInformationen
            (
                    "Buhl",
                    "Bachelor",
                    "https://www.hof-university.de/fileadmin/user_upload/studienbuero/Studien-_und_Pruefungsordnungen/Informatik/MI/SPO_MI_2021.pdf",
                    new ArrayList<ModulhandbuchFach>(List.of(
                            new ModulhandbuchFach(
                                    "Marketing",
                                    "Iiina",
                                    "Torsten Stapel",
                                    3,
                                    "SÜ, Ü",
                                    5,
                                    4,
                                    45,
                                    105,
                                    "deutsch",
                                    "Gaaanz viel Marketing",
                                    "Die studierenden sollen tolle dinge lernen",
                                    "kein",
                                    "Grundgesetz, Bürgerliches Gesetzbuch",
                                    "schrP 90",
                                    "Selbstbeschriebens Blatt",
                                    "Beamer, Tafel",
                                    "WS"
                            ),
                            new ModulhandbuchFach(
                                    "Fach2",
                                    "Seeeebi",
                                    "Torsten Stapel",
                                    3,
                                    "SÜ, Ü",
                                    5,
                                    4,
                                    45,
                                    105,
                                    "deutsch",
                                    "Gaaanz viel Marketing",
                                    "Die studierenden sollen tolle dinge lernen",
                                    "kein",
                                    "Grundgesetz, Bürgerliches Gesetzbuch",
                                    "schrP 90",
                                    "Selbstbeschriebens Blatt",
                                    "Beamer, Tafel",
                                    "WS"
                            ),
                            new ModulhandbuchFach(
                                    "Fach3",
                                    "Lage",
                                    "Torsten Stapel",
                                    3,
                                    "SÜ, Ü",
                                    5,
                                    4,
                                    45,
                                    105,
                                    "deutsch",
                                    "Gaaanz viel Marketing",
                                    "Die studierenden sollen tolle dinge lernen",
                                    "kein",
                                    "Grundgesetz, Bürgerliches Gesetzbuch",
                                    "schrP 90",
                                    "Selbstbeschriebens Blatt",
                                    "Beamer, Tafel",
                                    "WS"
                            )
                            )
                    )
            );

    public void initialize(URL location, ResourceBundle resources) {
        if((SchreiberLeser.getNutzerdaten().getStudiengang() == null) || (SchreiberLeser.getNutzerdaten().getStudiensemester() == null)) {
            Label fehlendeInformation = new Label("Leider hast Du in den Einstellungen keinen Studiengang definiert. Bitte definiere Deinen Studiengang!");
            fehlendeInformation.setWrapText(true);
            fehlendeInformation.getStyleClass().add("warnhinweis");
            vbContent.getChildren().add(fehlendeInformation);
        } else {
            studiengangTitel.setText("Studiengang: " + SchreiberLeser.getNutzerdaten().getStudiengang().getName() + " (" + SchreiberLeser.getNutzerdaten().getStudiengang().getKuerzel() + ")");
            studiengangTitel.setWrapText(true);

            // Erstelle GridPane mit allgemeinen Infos
            GridPane gpInfo = new GridPane();
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setMinWidth(250);
            gpInfo.getColumnConstraints().addAll(columnConstraints, columnConstraints);
            gpInfo.add(new Label("Studiengangleiter:"), 0, 0);
            gpInfo.add(new Label(testStudiengangInformation.getStudiengangLeiter()), 1, 0);
            gpInfo.add(new Label("Studiengangtyp:"), 0, 1);
            gpInfo.add(new Label(testStudiengangInformation.getStudiengangTyp()), 1, 1);
            gpInfo.add(new Label("Studien- & Prüfungsordnung:"), 0, 2);
            vbContent.getChildren().add(gpInfo);

            Hyperlink spoLink = new Hyperlink("Link zur SPO");
            spoLink.setOnAction(event -> Main.oeffneLinkInBrowser(testStudiengangInformation.getSPO()));
            gpInfo.add(spoLink, 1, 2);

            // Erstelle Accordion mit Fächern
            Accordion contentAccordion = new Accordion();
            contentAccordion.setPrefWidth(500);
            vbContent.getChildren().add(contentAccordion);

            testStudiengangInformation.getModulhandbuch().forEach((objName) -> {
                VBox vB = new VBox();
                vB.setSpacing(10);
                TitledPane tP = new TitledPane(objName.getFachName(), vB);
                contentAccordion.getPanes().add(tP);

                testStudiengangInformation.getModulhandbuch().stream().filter((fach) -> fach.getFachName() == objName.getFachName()).forEach((fach) -> {
                    ArrayList<Label> aL = new ArrayList<>();
                    VBox vbGericht = new VBox();
                    aL.add(new Label("Dozent: " + fach.getFachDozent()));
                    aL.add(new Label("Zweitprüfer: " + fach.getFachZweitPruefer()));
                    aL.add(new Label("Studienjahr: " + fach.getFachStudienjahr()));
                    aL.add(new Label("Art: " + fach.getFachArt()));
                    aL.add(new Label("ECTS: " + fach.getFachECTS()));
                    aL.add(new Label("SWS: " + fach.getFachSWS()));
                    aL.add(new Label("Präsenzzeit: " + fach.getFachPraesenzZeit()));
                    aL.add(new Label("Prüfungsvorbereitung: " + fach.getFachPruefungsVorbereitung()));
                    aL.add(new Label("Sprache: " + fach.getFachSprache()));
                    aL.add(new Label("Lehrinhalte: " + fach.getFachLehrinhalte()));
                    aL.add(new Label("Lernziel: " + fach.getFachLernziel()));
                    aL.add(new Label("Voraussetzung: " + fach.getFachVoraussetzung()));
                    aL.add(new Label("Literaturliste: " + fach.getFachLiteraturliste()));
                    aL.add(new Label("Prüfungsdurchführung: " + fach.getFachPruefungsdurchfuehrung()));
                    aL.add(new Label("Hilfsmittel: " + fach.getFachHilfsmittel()));
                    aL.add(new Label("Medienformen: " + fach.getFachMedienformen()));
                    aL.add(new Label("Häufigkeit: " + fach.getFachHaeufigkeit()));
                    aL.forEach((lbl) -> lbl.setWrapText(true));

                    ObservableList<Label> oaL = FXCollections.observableArrayList(aL);
                    vbGericht.getChildren().addAll(oaL);
                    vB.getChildren().add(vbGericht);
                });
            });

            for(int i=0; i<20; i++) {
                vbContent.getChildren().add(new Label("Blabla"));
            }
        }
    }
}
