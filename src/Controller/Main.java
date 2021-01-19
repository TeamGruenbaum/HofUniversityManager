package Controller;



import Controller.InformationsVermittlung.Internetverbindungskontrolleur;
import Controller.Speicher.SchreiberLeser;
import Controller.ViewController.GrundViewController;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.awt.Taskbar;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.imageio.ImageIO;



public class Main extends Application
{
	private static Parent rootParent;
	private static HostServices programmOeffnerHostServices;
	private static Stage primaryStage;

	@Override public void start(Stage primaryStage) throws Exception
	{
        if(System.getProperty("os.name").contentEquals("Mac OS X"))
        {
            Taskbar.getTaskbar().setIconImage(ImageIO.read(getClass().getResource("/Grafiken/HUM_Icon_Short.png")));
        }

		if(SchreiberLeser.isErsterStart())
		{
			if(Internetverbindungskontrolleur.isInternetVerbindungVorhanden("https://www.hof-university.de/"))
			{
				SchreiberLeser.alleDatenLoeschen();
				SchreiberLeser.alleZuruecksetzen();
			}
			else
			{
				Alert keinInternetAlert=new Alert(Alert.AlertType.WARNING);
				keinInternetAlert.setHeaderText("Internetverbindung notwendig");
				keinInternetAlert.setContentText("Beim ersten Start des Programmes ist es zwingend notwendig, dass Du eine Verbindung zum Internet hast. Starte das Programm erneut, wenn Du eine Verbindung zum Internet hergestellt hast.");
				keinInternetAlert.getDialogPane().getStylesheets().add(getClass().getResource("View/CSS/Application.css").toExternalForm());
				keinInternetAlert.showAndWait().ifPresentOrElse((item)->
				{
					Platform.exit();
					System.exit(0);
				}, ()->
				{
					Platform.exit();
					System.exit(0);
				});
			}
		}
		else
		{
			try
			{
				SchreiberLeser.alleLaden();

				if(SchreiberLeser.getDropdownMenue()==null)
				{
					throw new Exception();
				}
			}
			catch(Exception keineGefahrException)
			{
				keineGefahrException.printStackTrace();

				Alert datenKorruptAlert=new Alert(Alert.AlertType.ERROR);
				datenKorruptAlert.setHeaderText("Daten korrupt");
				datenKorruptAlert.setContentText("Die gespeicherten Daten wurden manipuliert. Deshalb wir das Programm nach dem Schließen des Fensters zurückgesetzt.");
				datenKorruptAlert.getDialogPane().getStylesheets().add(getClass().getResource("/View/CSS/Application.css").toExternalForm());
				datenKorruptAlert.showAndWait().ifPresentOrElse((item)->
				{
					SchreiberLeser.alleDatenLoeschen();
					Platform.exit();
					System.exit(0);
				}, ()->
				{
					SchreiberLeser.alleDatenLoeschen();
					Platform.exit();
					System.exit(0);
				});
			}
		}

		programmOeffnerHostServices=getHostServices();

		rootParent=FXMLLoader.load(getClass().getResource("/View/GrundView.fxml"));
		primaryStage.setTitle("HofUniversityManager");
		primaryStage.setScene(new Scene(rootParent, 900, 600));
		rootParent.getStylesheets().add(getClass().getResource("/View/CSS/Application.css").toExternalForm());
		Font.loadFont(Main.class.getResource("/Schriften/Rubik-Regular.ttf").toExternalForm(), 10);
		primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Grafiken/HUM_Icon_Short.png")));

		GrundViewController.setThema(SchreiberLeser.getNutzerdaten().getAktuellesThema());

		primaryStage.setMinHeight(600);
		primaryStage.setMinWidth(900);
		primaryStage.setOnCloseRequest(t->
		{
			SchreiberLeser.alleSpeichern();

			if(SchreiberLeser.isErsterStart())
			{
				SchreiberLeser.isErsterStartSetzen();
			}

			Platform.exit();
			System.exit(0);
		});
		Main.primaryStage=primaryStage;
		primaryStage.show();
	}

	public static Stage getPrimaryStage()
	{
		return primaryStage;
	}

	public static Parent getRoot()
	{
		return rootParent;
	}

	public static void oeffneLinkInBrowser(String url)
	{
		programmOeffnerHostServices.showDocument(url);
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
