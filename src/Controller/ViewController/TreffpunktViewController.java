package Controller.ViewController;



import Controller.Main;
import Controller.Speicher.SchreiberLeser;
import Model.TreffpunktModel.Freizeitaktivitaet;
import Model.TreffpunktModel.Restaurant;
import Model.TreffpunktModel.Treffpunkt;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;



public class TreffpunktViewController implements Initializable
{
	@FXML private ChoiceBox cbTreffpunktTyp;
	@FXML private VBox contentVBox;
	@FXML private Hyperlink emailHyperlink;

	public void initialize(URL location, ResourceBundle resources)
	{
		emailHyperlink.setOnAction((event->Main.oeffneLinkInBrowser("mailto:hum@meggede1.de")));

		ArrayList<String> listOriginal=new ArrayList<>();
		listOriginal.add("Restaurants");
		listOriginal.add("Freizeitaktivitäten");
		ObservableList<String> list=FXCollections.observableArrayList(listOriginal);
		cbTreffpunktTyp.setItems(list);
		cbTreffpunktTyp.getSelectionModel().selectFirst();

		Accordion contentAccordion=new Accordion();
		contentAccordion.setPrefWidth(700);
		contentAccordion.setMaxWidth(700);
		contentVBox.getChildren().add(contentAccordion);

		ArrayList<Treffpunkt> alleTreffpunkte=SchreiberLeser.getTreffpunkte().getTreffpunkte();

		zeigeRestaurants(contentAccordion, alleTreffpunkte);

		cbTreffpunktTyp.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue)->
		{
			if(newValue.equals(0))
			{
				zeigeRestaurants(contentAccordion, alleTreffpunkte);
			}
			else
			{
				zeigeFreizeitaktivitaeten(contentAccordion, alleTreffpunkte);
			}
		});
	}

	public void zeigeRestaurants(Accordion accordion, ArrayList<Treffpunkt> arrayList)
	{
		accordion.getPanes().clear();
		arrayList.stream().filter((item)->item instanceof Restaurant).forEach((item)->
		{

			VBox vB=new VBox();

			accordion.getPanes().add(new TitledPane(item.getName(), vB));

			ArrayList<Label> treffpunktTextzeilen=new ArrayList<>();
			treffpunktTextzeilen.add(new Label("Ort: "+item.getOrt()));
			treffpunktTextzeilen.add(new Label("Information: "+item.getInformation()));
			treffpunktTextzeilen.add(new Label("Art: "+((Restaurant)item).getArt()));
			treffpunktTextzeilen.add(new Label("Nationalität: "+((Restaurant)item).getNationalitaet()));
			treffpunktTextzeilen.add(new Label((item.getWetterunabhaengig())?"Wetterabhängig: nein":"Wetterabhängig: ja"));
			treffpunktTextzeilen.add(new Label((((Restaurant)item).getLieferdienst())?"Liefert: ja":"Liefert: nein"));
			ObservableList<Label> oaL=FXCollections.observableArrayList(treffpunktTextzeilen);

			vB.getChildren().addAll(oaL);
		});
	}

	public void zeigeFreizeitaktivitaeten(Accordion accordion, ArrayList<Treffpunkt> arrayList)
	{
		accordion.getPanes().clear();
		arrayList.stream().filter((item)->item instanceof Freizeitaktivitaet).forEach((item)->
		{

			VBox vB=new VBox();

			accordion.getPanes().add(new TitledPane(item.getName(), vB));

			ArrayList<Label> aL=new ArrayList<>();
			aL.add(new Label("Ort: "+item.getOrt()));
			aL.add(new Label("Information: "+item.getInformation()));
			aL.add(new Label("Ambiente: "+((Freizeitaktivitaet)item).getAmbiente()));
			aL.add(new Label((item.getWetterunabhaengig())?"Wetterabhängig: nein":"Wetterabhängig: ja"));
			ObservableList<Label> oaL=FXCollections.observableArrayList(aL);

			vB.getChildren().addAll(oaL);
		});
	}
}
