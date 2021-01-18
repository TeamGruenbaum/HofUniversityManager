package Controller.ViewController;



import Controller.Speicher.SchreiberLeser;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;



public class ModulhandbuchViewController implements Initializable
{

	@FXML private VBox inhaltVBox;
	@FXML private Label studiengangtitelLabel;



	public void initialize(URL location, ResourceBundle resources)
	{
		if(SchreiberLeser.getModulhandbuch().getModulhandbuchFaecher().size()==0)
		{
			Label fehlendeInformationLabel=new Label("Für das ausgewählte Studierensemester scheint es kein Modulhandbuch zu geben");
			fehlendeInformationLabel.setWrapText(true);
			fehlendeInformationLabel.getStyleClass().add("warnhinweis");
			inhaltVBox.getChildren().add(fehlendeInformationLabel);
		}
		else
		{
			studiengangtitelLabel.setText("Studiengang: "+SchreiberLeser.getNutzerdaten().getAusgewaehlterStudiengang().getName()+" ("+SchreiberLeser.getNutzerdaten().getAusgewaehlterStudiengang().getKuerzel()+", "+SchreiberLeser.getNutzerdaten().getAusgewaehltesStudiensemester().getName()+")");
			studiengangtitelLabel.setWrapText(true);

			Accordion faecherAccordion=new Accordion();
			faecherAccordion.setPrefWidth(700);
			inhaltVBox.getChildren().add(faecherAccordion);

			SchreiberLeser.getModulhandbuch().getModulhandbuchFaecher().forEach((fach)->
			{
				GridPane fachinformationenGridPane=new GridPane();
				fachinformationenGridPane.setVgap(5);
				ColumnConstraints abstandColumnConstraints=new ColumnConstraints();
				abstandColumnConstraints.setMinWidth(250);
				fachinformationenGridPane.getColumnConstraints().addAll(abstandColumnConstraints, abstandColumnConstraints);

				faecherAccordion.getPanes().add(new TitledPane(fach.getName(), fachinformationenGridPane));

				ObservableList<Label> bezeichnungen=FXCollections.observableArrayList(List.of(new Label("Dozent: "), new Label("Zweitprüfer: "), new Label("Studienjahr: "), new Label("Art: "), new Label("ECTS: "), new Label("SWS: "), new Label("Präsenzzeit: "), new Label("Prüfungsvorbereitung: "), new Label("Sprache: "), new Label("Lehrinhalte: "), new Label("Lernziel: "), new Label("Voraussetzung: "), new Label("Literaturliste: "), new Label("Prüfungsdurchführung: "), new Label("Hilfsmittel: "), new Label("Medienformen: "), new Label("Häufigkeit: ")));

				ObservableList<Label> informationen=FXCollections.observableArrayList(List.of(new Label(fach.getDozent()), new Label(fach.getZweitpruefer()), new Label(String.valueOf(fach.getStudienjahr())), new Label(fach.getArt()), new Label(String.valueOf(fach.getECTS())), new Label(String.valueOf(fach.getSemesterwochenstunden())), new Label(String.valueOf(fach.getPraesenzzeit())), new Label(String.valueOf(fach.getPruefungsvorbereitung())), new Label(fach.getSprache()), new Label(fach.getLehrinhalte()), new Label(fach.getLernziel()), new Label(fach.getVoraussetzung()), new Label(fach.getLiteraturliste()), new Label(fach.getPruefungsdurchfuehrung()), new Label(fach.getHilfsmittel()), new Label(fach.getMedienformen()), new Label(fach.getHaeufigkeit())));

				for(int i=0;i<bezeichnungen.size();i++)
				{
					bezeichnungen.get(i).setWrapText(true);
					informationen.get(i).setWrapText(true);
					fachinformationenGridPane.add(bezeichnungen.get(i), 0, i);
					fachinformationenGridPane.add(informationen.get(i), 1, i);
				}
			});
		}
	}
}
