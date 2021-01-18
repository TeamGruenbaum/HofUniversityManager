package Controller;



import Controller.InformationsVermittlung.Internetverbindungskontrolleur;
import Controller.Speicher.SchreiberLeser;
import Controller.ViewController.GrundViewController;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.awt.Taskbar;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;



public class Main extends Application
{
	private static Parent root;
	private static HostServices hostServices;
	private static Stage primaryStage;

	@Override public void start(Stage primaryStage) throws Exception
	{
        /*if(System.getProperty("os.name").contentEquals("Mac OS X"))
        {
            Taskbar.getTaskbar().setIconImage(ImageIO.read(getClass().getResource("Grafiken/HUM_Icon_Short.png")));
        }*/

		if(SchreiberLeser.isErsterStart())
		{
			if(Internetverbindungskontrolleur.isInternetVerbindungVorhanden("https://www.hof-university.de/"))
			{
				SchreiberLeser.alleDatenLoeschen();
				SchreiberLeser.alleZuruecksetzen();
			}
			else
			{
				Alert alert=new Alert(Alert.AlertType.WARNING);
				alert.setHeaderText("Internetverbindung notwendig");
				alert.setContentText("Beim ersten Start des Programmes ist es zwingend notwendig, dass Du eine Verbindung zum Internet hast. Starte das Programm erneut, wenn Du eine Verbindung zum Internet hergestellt hast.");
				alert.getDialogPane().getStylesheets().add(getClass().getResource("View/CSS/Application.css").toExternalForm());
				alert.showAndWait().ifPresentOrElse((item)->
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

				Alert alert=new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText("Daten korrupt");
				alert.setContentText("Die gespeicherten Daten wurden manipuliert. Deshalb wir das Programm nach dem Schließen des Fensters zurückgesetzt.");
				alert.getDialogPane().getStylesheets().add(getClass().getResource("/View/CSS/Application.css").toExternalForm());
				alert.showAndWait().ifPresentOrElse((item)->
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

		hostServices=getHostServices();

		root=FXMLLoader.load(getClass().getResource("/View/GrundView.fxml"));
		primaryStage.setTitle("HofUniversityManager");
		primaryStage.setScene(new Scene(root, 900, 600));
		root.getStylesheets().add(getClass().getResource("/View/CSS/Application.css").toExternalForm());
		Font.loadFont(Main.class.getResource("/Schriften/Rubik-Regular.ttf").toExternalForm(), 10);
		primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/Grafiken/HUM_Icon_Short.png")));

        /*final URL imageResource = Main.class.getClassLoader().getResource("../Ressourcen/Grafiken/HUM_Icon_Short.png");
        final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        final java.awt.Image image = defaultToolkit.getImage(imageResource);
        final Taskbar taskbar = Taskbar.getTaskbar();
        taskbar.setIconImage(image);*/

		GrundViewController.setThema(SchreiberLeser.getNutzerdaten().getAktuellesThema());

		primaryStage.setMinHeight(600);
		primaryStage.setMinWidth(900);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{

			@Override public void handle(WindowEvent t)
			{
				SchreiberLeser.alleSpeichern();

				if(SchreiberLeser.isErsterStart())
				{
					SchreiberLeser.isErsterStartSetzen();
				}

				Platform.exit();
				System.exit(0);
			}
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
		return root;
	}

	public static void oeffneLinkInBrowser(String url)
	{
		hostServices.showDocument(url);
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
