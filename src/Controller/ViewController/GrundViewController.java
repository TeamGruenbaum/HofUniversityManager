package Controller.ViewController;



import Controller.InformationsVermittlung.Downloader;
import Controller.InformationsVermittlung.Internetverbindungskontrolleur;
import Controller.Main;
import Controller.Sonstiges.TextHelfer;
import Controller.Speicher.SchreiberLeser;
import Model.DropdownModel.Studiengang;
import Model.DropdownModel.Studiensemester;
import Model.NutzerdatenModel.Thema;
import Model.OberflaechenModel.Blende;
import Model.OberflaechenModel.Menue;
import Model.QuicklinksModel.Quicklinks;
import javafx.animation.FadeTransition;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.util.Duration;



public class GrundViewController implements Initializable
{
	@FXML private BorderPane geruestBorderPane;
	@FXML private StackPane geruestStackPane;
	@FXML private Label aktuellAusgewaehlterMenuepunktTextLabel;
	@FXML private Button menueHauptbuttonButton;
	@FXML private WebView downloaderWebView;

	private static GridPane hauptmenueGridPane;
	private AnchorPane hauptmenueAnchorPane;

	private boolean mensaplanEinmalHeruntergeladen, treffpunkteEinmalHeruntergeladen;
	private Studiengang letzterStudiengangStudiengang;
	private Studiensemester letztesStudiensemesterStudiensemester;

	private static WebView staticDownloaderWebView;
	private static Button staticMenueHauptbuttonButton;



	@Override public void initialize(URL location, ResourceBundle resources)
	{
		mensaplanEinmalHeruntergeladen=false;
		treffpunkteEinmalHeruntergeladen=false;

		ImageView hauptmenueIconImageView=new ImageView(new Image(getClass().getResourceAsStream("/Grafiken/dots-menu.png")));
		hauptmenueIconImageView.setFitHeight(35);
		hauptmenueIconImageView.setPreserveRatio(true);

		menueHauptbuttonButton.setGraphic(hauptmenueIconImageView);

		setHoverEffekt(menueHauptbuttonButton, hauptmenueIconImageView);

		int menuepunktHoeheBreite=90;

		hauptmenueGridPane=new GridPane();
		hauptmenueGridPane.getStyleClass().add("icon-menu-karte");
		ColumnConstraints abstandColumnConstraints=new ColumnConstraints();
		abstandColumnConstraints.setMinWidth(menuepunktHoeheBreite);
		abstandColumnConstraints.setMaxWidth(menuepunktHoeheBreite);
		hauptmenueGridPane.getColumnConstraints().addAll(abstandColumnConstraints, abstandColumnConstraints, abstandColumnConstraints);
		hauptmenueGridPane.addEventFilter(MouseEvent.MOUSE_CLICKED, Event::consume);

		int j=0;
		for(int i=0;i<Menue.getMenuepunktInformationen().size();i++)
		{
			if(i%3==0)
			{
				if(i!=0)
				{
					j++;
				}

				RowConstraints abstandRowConstraints=new RowConstraints();
				abstandRowConstraints.setMinHeight(menuepunktHoeheBreite);
				abstandRowConstraints.setMaxHeight(menuepunktHoeheBreite);
				hauptmenueGridPane.getRowConstraints().add(abstandRowConstraints);
			}

			Button menuepunktButton=new Button();
			int finalI=i;
			menuepunktButton.setOnAction((actionEvent)->
			{
				SchreiberLeser.getNutzerdaten().setLetzterGeoeffneterMenuepunkt(Menue.getMenuepunktInformationen().get(finalI));

				oeffneScene();
			});
			menuepunktButton.setTooltip(new Tooltip(TextHelfer.grossschreiben(Menue.getMenuepunktInformationen().get(i).getZielanwendung().toString())));
			menuepunktButton.getTooltip().getStyleClass().add("breadcrumb-menu");

			menuepunktButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

			ImageView menuepunktIconImageView=new ImageView(new Image(getClass().getResourceAsStream("/Grafiken/"+Menue.getMenuepunktInformationen().get(i).getIconDateiname())));
			menuepunktIconImageView.setFitHeight(menuepunktHoeheBreite-55);
			menuepunktIconImageView.setPreserveRatio(true);
			menuepunktButton.setGraphic(menuepunktIconImageView);
			menuepunktButton.getStyleClass().add("icon-menu-button");
			setHoverEffekt(menuepunktButton, menuepunktIconImageView);

			hauptmenueGridPane.add(menuepunktButton, i%3, j);
		}

		hauptmenueAnchorPane=new AnchorPane();
		AnchorPane.setTopAnchor(hauptmenueGridPane, 70.0);
		AnchorPane.setRightAnchor(hauptmenueGridPane, 10.0);
		hauptmenueAnchorPane.getChildren().add(hauptmenueGridPane);
		hauptmenueAnchorPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)->
		{
			hauptmenueSchließen();
		});


		if(SchreiberLeser.isErsterStart())
		{
			ladeErsterStartScene();
		}
		else
		{
			oeffneScene();
		}

		staticDownloaderWebView=downloaderWebView;
		staticMenueHauptbuttonButton=menueHauptbuttonButton;
	}


	@FXML public void hauptmenueOeffnen()
	{
		blenden(hauptmenueAnchorPane, 300, Blende.EINBLENDEN);
		geruestStackPane.getChildren().add(hauptmenueAnchorPane);
	}

	private void hauptmenueSchließen()
	{
		new Timeline(new KeyFrame(Duration.ZERO, event->blenden(hauptmenueAnchorPane, 300, Blende.AUSBLENDEN)), new KeyFrame(Duration.millis(300), event->geruestStackPane.getChildren().remove(hauptmenueAnchorPane))).play();
	}


	private void oeffneScene()
	{
		aktuellAusgewaehlterMenuepunktTextLabel.setText(TextHelfer.grossschreiben(SchreiberLeser.getNutzerdaten().getLetzterGeoeffneterMenuepunkt().getZielanwendung().toString()));

		switch(SchreiberLeser.getNutzerdaten().getLetzterGeoeffneterMenuepunkt().getZielanwendung())
		{
			case STUNDENPLAN:
			{
				ladeSceneMitScrollPane();
			}
			break;
			case MENSAPLAN:
			{
				if(Internetverbindungskontrolleur.isInternetVerbindungVorhanden("https://www.studentenwerk-oberfranken.de/"))
				{
					hauptmenueSchließen();

					if(mensaplanEinmalHeruntergeladen)
					{
						ladeSceneMitScrollPane();
					}
					else
					{
						ladeLadenScene().progressProperty().addListener(((observable, oldValue, newValue)->
						{
							if(newValue.doubleValue()==1.0)
							{
								ladeSceneMitScrollPane();
								menueHauptbuttonButton.setDisable(false);
								mensaplanEinmalHeruntergeladen=true;
							}
						}));

						new Thread(new Task<Void>()
						{
							@Override protected Void call()
							{

								Platform.runLater(()->menueHauptbuttonButton.setDisable(true));
								Downloader.mensaplanAbrufen();

								return null;
							}
						}).start();
					}
				}
				else
				{
					oeffneFehlendeInternetverbindungAlertHinweis();

					ladeSceneMitScrollPane();
				}
			}
			break;
			case MODULHANDBUCH:
			{
				hauptmenueSchließen();

				if(SchreiberLeser.getNutzerdaten().getAusgewaehlterStudiengang()!=null&&SchreiberLeser.getNutzerdaten().getAusgewaehltesStudiensemester()!=null)
				{
					if(Internetverbindungskontrolleur.isInternetVerbindungVorhanden("https://www.hof-university.de"))
					{
						if(letzterStudiengangStudiengang==SchreiberLeser.getNutzerdaten().getAusgewaehlterStudiengang()&&letztesStudiensemesterStudiensemester==SchreiberLeser.getNutzerdaten().getAusgewaehltesStudiensemester())
						{
							ladeSceneMitScrollPane();
						}
						else
						{
							ladeLadenScene().progressProperty().addListener(((observable, oldValue, newValue)->
							{
								if(newValue.doubleValue()==1.0)
								{
									letzterStudiengangStudiengang=SchreiberLeser.getNutzerdaten().getAusgewaehlterStudiengang();
									letztesStudiensemesterStudiensemester=SchreiberLeser.getNutzerdaten().getAusgewaehltesStudiensemester();

									ladeSceneMitScrollPane();
									menueHauptbuttonButton.setDisable(false);
								}
							}));

							new Thread(new Task<Void>()
							{
								@Override protected Void call() throws Exception
								{

									menueHauptbuttonButton.setDisable(true);
									Downloader.modulhandbuchAbrufen();
									return null;
								}
							}).start();
						}
					}
					else
					{
						oeffneFehlendeInternetverbindungAlertHinweis();
						ladeSceneMitScrollPane();
					}
				}
				else
				{
					oeffneFehlenderStudiengangDialog();
				}
			}
			break;
			case MOODLE, PRIMUSS:
			{
				if(Internetverbindungskontrolleur.isInternetVerbindungVorhanden(Quicklinks.getMoodleLink()))
				{
					if(SchreiberLeser.getNutzerdaten().getSsoLogin().getName().compareTo("")==0||SchreiberLeser.getNutzerdaten().getSsoLogin().getPasswort().compareTo("")==0)
					{
						Alert alert=new Alert(Alert.AlertType.INFORMATION, "Du solltest Deine Login-Daten in den Einstellungen hinterlegen!");
						alert.getDialogPane().getStylesheets().add(getClass().getResource("/View/CSS/Application.css").toExternalForm());
						alert.setTitle("Login setzen");
						alert.setHeaderText("Empfehlung");
						alert.initOwner(Main.getPrimaryStage());
						alert.showAndWait();
					}
					ladeSceneOhneScrollPane();
				}
				else
				{
					oeffneFehlendeInternetverbindungAlertWarnung();
				}
			}
			break;
			case PANOPTO:
			{
				if(Internetverbindungskontrolleur.isInternetVerbindungVorhanden(Quicklinks.getPanoptoLink()))
				{
					Main.oeffneLinkInBrowser(Quicklinks.getPanoptoLink());
				}
				else
				{
					oeffneFehlendeInternetverbindungAlertWarnung();
				}
			}
			break;
			case NEXTCLOUD:
			{
				if(Internetverbindungskontrolleur.isInternetVerbindungVorhanden(Quicklinks.getPanoptoLink()))
				{
					Main.oeffneLinkInBrowser(Quicklinks.getNextcloudLink());
				}
				else
				{
					oeffneFehlendeInternetverbindungAlertWarnung();
				}
			}
			break;
			case CAMPUSSPORT:
			{
				if(Internetverbindungskontrolleur.isInternetVerbindungVorhanden(Quicklinks.getCampusSportLink()))
				{
					ladeSceneOhneScrollPane();
				}
				else
				{
					oeffneFehlendeInternetverbindungAlertWarnung();
				}
			}
			break;
			case TREFFPUNKTE:
			{
				ladeLadenScene();
				hauptmenueSchließen();

				if(Internetverbindungskontrolleur.isInternetVerbindungVorhanden("https://stevensolleder.de"))
				{
					if(treffpunkteEinmalHeruntergeladen)
					{
						ladeSceneMitScrollPane();
					}
					else
					{
						ladeLadenScene().progressProperty().addListener(((observable, oldValue, newValue)->
						{
							if(newValue.doubleValue()==1.0)
							{
								ladeSceneMitScrollPane();
								menueHauptbuttonButton.setDisable(false);
								treffpunkteEinmalHeruntergeladen=true;
							}
						}));

						new Thread(new Task<Void>()
						{
							@Override protected Void call() throws Exception
							{
								menueHauptbuttonButton.setDisable(true);
								Downloader.treffpunkteAbrufen();
								return null;
							}
						}).start();
					}
				}
				else
				{
					oeffneFehlendeInternetverbindungAlertHinweis();

					ladeSceneMitScrollPane();
				}
			}
			break;
			case BAYERNFAHRPLAN:
			{
				if(Internetverbindungskontrolleur.isInternetVerbindungVorhanden(Quicklinks.getBayernfahrplanLink()))
				{
					ladeSceneOhneScrollPane();
				}
				else
				{
					oeffneFehlendeInternetverbindungAlertWarnung();
				}
			}
			break;
			case EINSTELLUNGEN:
			{
				ladeSceneMitScrollPane();
			}
			break;
			default:
			{
				ladeSceneMitScrollPane();
			}
		}
	}


	private void ladeSceneMitScrollPane()
	{
		try
		{
			ScrollPane sp=new ScrollPane();
			sp.setPrefViewportHeight(300);
			sp.setFitToHeight(true);
			sp.setFitToWidth(true);
			sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
			sp.setContent(FXMLLoader.load(getClass().getResource("/View/"+SchreiberLeser.getNutzerdaten().getLetzterGeoeffneterMenuepunkt().getFxmlDateiname())));
			geruestBorderPane.setCenter(sp);
			geruestBorderPane.getCenter().setViewOrder(1);
			geruestBorderPane.getStyleClass().add("anwendungsBereich");
			hauptmenueSchließen();
		}
		catch(Exception keineGefahrExcpetion)
		{
			//Die Gefahr ist gebannt, da der Pfad zur richtigen FXML-Datei hartkodiert ist
			keineGefahrExcpetion.printStackTrace();
		}
	}

	private void ladeSceneOhneScrollPane()
	{
		try
		{
			geruestBorderPane.setCenter(FXMLLoader.load(getClass().getResource("/View/"+SchreiberLeser.getNutzerdaten().getLetzterGeoeffneterMenuepunkt().getFxmlDateiname())));
			geruestBorderPane.getCenter().setViewOrder(1);
			hauptmenueSchließen();
		}
		catch(Exception keineGefahrExcepttion)
		{
			//Die Gefahr ist gebannt, da der Pfad zur richtigen FXML-Datei hartkodiert ist
			keineGefahrExcepttion.printStackTrace();
		}
	}

	private ProgressIndicator ladeLadenScene()
	{
		BorderPane rahmenBorderPane=new BorderPane();
		rahmenBorderPane.setMinHeight(Double.MIN_VALUE);
		rahmenBorderPane.setMinWidth(Double.MIN_VALUE);

		ProgressBar downloadFortschrittProgressBar=new ProgressBar();
		downloadFortschrittProgressBar.setMinWidth(100);

		rahmenBorderPane.setCenter(downloadFortschrittProgressBar);
		this.geruestBorderPane.setCenter(rahmenBorderPane);

		Downloader.setDownloadfortschrittProgressIndicator(downloadFortschrittProgressBar);

		return downloadFortschrittProgressBar;
	}

	private void ladeErsterStartScene()
	{
		menueHauptbuttonButton.setDisable(true);
		aktuellAusgewaehlterMenuepunktTextLabel.setText("Erster Start");

		try
		{
			Node szeneNode=FXMLLoader.load(getClass().getResource("/View/ErsterStartView.fxml"));
			Button zuDenEinstellungenButton=(Button) szeneNode.lookup("#losgehts");
			zuDenEinstellungenButton.setDisable(true);
			zuDenEinstellungenButton.setOnAction((actionEvent)->
			{
				menueHauptbuttonButton.setDisable(false);
				oeffneScene();
			});

			((Label) szeneNode.lookup("#text")).setWrapText(true);

			ProgressIndicator initialisierungProgressIndicator=(ProgressIndicator) szeneNode.lookup("#dropdownMenueLadenProgressBar");
			initialisierungProgressIndicator.progressProperty().addListener((observable, oldValue, newValue)->
			{
				if(newValue.doubleValue()==1)
				{
					zuDenEinstellungenButton.setDisable(false);
				}
			});
			Downloader.setDownloadfortschrittProgressIndicator(initialisierungProgressIndicator);

			geruestBorderPane.setCenter(szeneNode);

			Downloader.dropdownMenueAbrufen();
		}
		catch(Exception keinGefahreException)
		{
			//Die Gefahr ist gebannt, da der Pfad zur richtigen FXML-Datei hartkodiert ist
			keinGefahreException.printStackTrace();
		}
	}


	public static void oeffneFehlenderStudiengangDialog()
	{
		Alert fehlenderStudiengangAlert=new Alert(Alert.AlertType.WARNING, "Der Studiengang und das Studiensemester müssen gesetzt werden, bevor Du diese Funktion nutzen kannst!");
		fehlenderStudiengangAlert.getDialogPane().getStylesheets().add(GrundViewController.class.getResource("/View/CSS/Application.css").toExternalForm());
		fehlenderStudiengangAlert.setTitle("Studiengang und -semester setzen");
		fehlenderStudiengangAlert.setHeaderText("Warnung");
		fehlenderStudiengangAlert.initOwner(Main.getPrimaryStage());
		fehlenderStudiengangAlert.showAndWait();
	}

	public static void oeffneFehlendeInternetverbindungAlertWarnung()
	{
		oeffneFehlendeInternetverbindungAlert("Es besteht keine Verbindung zum Internet. Dieser Dienst ist aktuell nicht verfügbar.");
	}

	public static void oeffneFehlendeInternetverbindungAlertHinweis()
	{
		oeffneFehlendeInternetverbindungAlert("Es besteht keine Verbindung zum Internet. Eventuell sind keine oder veraltete Daten vorhanden.");
	}

	private static void oeffneFehlendeInternetverbindungAlert(String inhalt)
	{
		Alert fehlendeInternetverbindungAlert=new Alert(Alert.AlertType.WARNING, inhalt);
		fehlendeInternetverbindungAlert.getDialogPane().getStylesheets().add(GrundViewController.class.getResource("/View/CSS/Application.css").toExternalForm());
		fehlendeInternetverbindungAlert.getDialogPane().setStyle(Main.getRoot().getStyle());
		fehlendeInternetverbindungAlert.setTitle("Keine Internetverbindung");
		fehlendeInternetverbindungAlert.setHeaderText("Hinweis");
		fehlendeInternetverbindungAlert.initOwner(Main.getPrimaryStage());
		fehlendeInternetverbindungAlert.showAndWait();
	}


	public static void setThema(Thema neuerWert)
	{
		ColorAdjust farbwechselColorAdjust=new ColorAdjust();

		if(neuerWert==Thema.DUNKEL)
		{
			Main.getRoot().setStyle("-menubar-color: #676767;"+"-font-color: #ffffff;"+"-anwendung-bgr: #404040;"+"-accent-color: #318eb1;"+"-warn-color: #691c1c;"+"-menubar-text-color: white;"+"-accent-color-accent: #266e8c;"+"-anwendung-bgr-accent: #4f4f4f;"+"-menubar-titel-color: #45c8ff");
			farbwechselColorAdjust.setBrightness(1);
		}
		else
		{
			Main.getRoot().setStyle("-menubar-color: white;"+"-font-color: #262626;"+"-anwendung-bgr: #e2e2e2;"+"-warn-color: #8a2828;"+"-menubar-text-color: black;"+"-accent-color-accent: #004a66;"+"-anwendung-bgr-accent: #ffffff;"+"-menubar-titel-color: #0072a0");
			farbwechselColorAdjust.setBrightness(0);
		}

		hauptmenueGridPane.getChildren().forEach((item)->
		{
			item.setEffect(farbwechselColorAdjust);
		});

		getStaticMenueHauptbuttonButton().setEffect(farbwechselColorAdjust);
	}

	private void blenden(Node zielNode, int dauer, Blende artBlende)
	{
		FadeTransition ft=new FadeTransition(Duration.millis(dauer), zielNode);
		if(artBlende==Blende.EINBLENDEN)
		{
			ft.setFromValue(0.0);
			ft.setToValue(1.0);
		}
		else
		{
			ft.setFromValue(1.0);
			ft.setToValue(0.0);
		}
		ft.play();
	}

	private void setHoverEffekt(Node zielNode, ImageView zielImageView)
	{
		FadeTransition hoverStartFadeTransition=new FadeTransition(Duration.millis(180), zielImageView);
		hoverStartFadeTransition.setFromValue(1.0);
		hoverStartFadeTransition.setToValue(0.7);

		FadeTransition hoverEndeFadeTransition=new FadeTransition(Duration.millis(180), zielImageView);
		hoverEndeFadeTransition.setFromValue(0.7);
		hoverEndeFadeTransition.setToValue(1.0);

		zielNode.setOnMouseEntered((MouseEvent)->
		{
			hoverStartFadeTransition.play();
		});

		zielNode.setOnMouseExited((MouseEvent)->
		{
			hoverEndeFadeTransition.play();
		});
	}


	public static WebView getUglyWebview()
	{
		return staticDownloaderWebView;
	}

	public static Button getStaticMenueHauptbuttonButton()
	{
		return staticMenueHauptbuttonButton;
	}
}
