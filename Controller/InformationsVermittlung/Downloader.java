package Controller.InformationsVermittlung;



import Controller.InformationsVermittlung.Hilfsklassen.NameKuerzelDocumentTripel;
import Controller.InformationsVermittlung.Hilfsklassen.DatumDocumentPaar;
import Controller.InformationsVermittlung.Hilfsklassen.MensaplanTupel;
import Controller.Speicher.SchreiberLeser;
import Controller.ViewController.GrundViewController;
import Model.Datum;

import Model.ModulhandbuchModel.Modulhandbuch;
import Model.ModulhandbuchModel.ModulhandbuchFach;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.web.WebEngine;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



public class Downloader
{
	private static ProgressIndicator downloadfortschrittProgressIndicator;
	private static long messStartzeit, messEndzeit;
	//nullable
	private static ChangeListener<Worker.State> letzteListenerChangeListener=null;


	//Hiermit wird der für den Downloader benötigte DownloadfortschrittProgressIndicator gesetzt.
	public static void setDownloadfortschrittProgressIndicator(ProgressIndicator neuerWertProgressIndicator)
	{
		downloadfortschrittProgressIndicator=neuerWertProgressIndicator;
	}

	//Hiermit wird der Stundenplan der Hochschule auf Basis des in den Einstellungen hinterlegten Studiengangs und Semesters als Html-Datei heruntergeladen, geparst und als ArrayList<Doppelstunde>-Objekt im Nutzerdaten-Objekt, welches im SpeicherLeser als Attrbitut gespeichert ist, gesichert.
	public static void stundenplanAbrufen()
	{
		Platform.runLater(()->
		{
			downloadfortschrittProgressIndicator.setProgress(0);

			WebEngine downloadWebEnginge=GrundViewController.getUglyWebview().getEngine();
			entferneLetztenListener(downloadWebEnginge);

			ChangeListener<Worker.State> downloadChangeListener=(observable, oldValue, newValue)->
			{
				if(newValue==Worker.State.SUCCEEDED)
				{
					messEndzeit=System.nanoTime();

					downloadfortschrittProgressIndicator.setProgress(0.25);

					Task<Void> task=new Task<Void>()
					{
						@Override
						protected Void call()
						{
							Platform.runLater(()->
							{
								downloadWebEnginge.executeScript("document.getElementsByName('tx_stundenplan_stundenplan[studiengang]')[0].value='"+SchreiberLeser.getNutzerdaten().getAusgewaehlterStudiengang().getKuerzel()+"';"+"document.getElementsByName('tx_stundenplan_stundenplan[studiengang]')[0].dispatchEvent(new Event('change'));");
								downloadfortschrittProgressIndicator.setProgress(0.5);
							});

							try
							{
								Thread.sleep((getDownloadzeit()));
							}catch(Exception keineGefahrExcpetion)
							{
								//Die Gefahr ist gebannt, da der Thread nicht unterbrochen werden kann
								keineGefahrExcpetion.printStackTrace();
							}

							Platform.runLater(()->
							{
								downloadWebEnginge.executeScript("document.getElementsByName('tx_stundenplan_stundenplan[semester]')[0].value='"+SchreiberLeser.getNutzerdaten().getAusgewaehltesStudiensemester().getKuerzel()+"';"+"document.getElementsByName('tx_stundenplan_stundenplan[semester]')[0].dispatchEvent(new Event('change'));");
								downloadfortschrittProgressIndicator.setProgress(0.75);
							});

							try
							{
								Thread.sleep((getDownloadzeit()*2));
							}catch(Exception keineGefahrExcpetion)
							{
								//Die Gefahr ist gebannt, da der Thread nicht unterbrochen werden kann
								keineGefahrExcpetion.printStackTrace();
							}

							Platform.runLater(()->
							{
								SchreiberLeser.getNutzerdaten().setDoppelstunden(Parser.stundenplanParsen(Jsoup.parse((String) downloadWebEnginge.executeScript("document.documentElement.outerHTML"))));
								downloadfortschrittProgressIndicator.setProgress(1);
							});

							return null;
						}
					};

					new Thread(task).start();
				}
			};
			downloadWebEnginge.getLoadWorker().stateProperty().addListener(downloadChangeListener);
			letzteListenerChangeListener=downloadChangeListener;

			messStartzeit=System.nanoTime();
			downloadWebEnginge.load("https://www.hof-university.de/studierende/info-service/stundenplaene.html");
		});
	}


	//Hiermit wird das Modulhandbuch der Hochschule auf Basis des in den Einstellungen hinterlegten Studiengangs und Semesters als Html-Datei heruntergeladen, geparst und als Modulhandbuch-Objekt im SpeicherLeser als Attrbitut gespeichert.
	public static void modulhandbuchAbrufen()
	{
		Platform.runLater(()->
		{
			downloadfortschrittProgressIndicator.setProgress(0);

			WebEngine downloadWebEngine=GrundViewController.getUglyWebview().getEngine();
			entferneLetztenListener(downloadWebEngine);

			ChangeListener<Worker.State> downloadChangeListener=((observable, oldValue, newValue)->
			{
				if(newValue==Worker.State.SUCCEEDED)
				{
					ArrayList<Document> module=new ArrayList<>();

					messEndzeit=System.nanoTime();

					Task<Void> downloadTask=new Task<Void>()
					{
						@Override
						protected Void call() throws Exception
						{
							Platform.runLater(()->
							{
								downloadWebEngine.executeScript("document.getElementById('class_change').value='"+SchreiberLeser.getNutzerdaten().getAusgewaehlterStudiengang().getKuerzel()+"';"+"document.getElementById('class_change').dispatchEvent(new Event('change'));");
								downloadfortschrittProgressIndicator.setProgress(0.1);
							});

							try
							{
								Thread.sleep((getDownloadzeit()));
							}catch(Exception keineGefahrExcpetion)
							{
								//Die Gefahr ist gebannt, da der Thread nicht unterbrochen werden kann
								keineGefahrExcpetion.printStackTrace();
							}

							Pattern pattern=Pattern.compile("(.*)(#)([A-Z]{2}#\\d{4})");

							Platform.runLater(()->
							{
								Matcher matcher=pattern.matcher(SchreiberLeser.getNutzerdaten().getAusgewaehltesStudiensemester().getKuerzel());
								matcher.matches();
								downloadWebEngine.executeScript("document.getElementById('year_change').value='"+matcher.group(3).replace('#', ' ')+"';"+"document.getElementById('year_change').dispatchEvent(new Event('change'));");
								downloadfortschrittProgressIndicator.setProgress(0.2);
							});

							try
							{
								Thread.sleep((getDownloadzeit()));
							}catch(Exception keineGefahrExcpetion)
							{
								//Die Gefahr ist gebannt, da der Thread nicht unterbrochen werden kann
								keineGefahrExcpetion.printStackTrace();
							}

							Platform.runLater(()->
							{
								Matcher matcher=pattern.matcher(SchreiberLeser.getNutzerdaten().getAusgewaehltesStudiensemester().getKuerzel());
								matcher.matches();
								downloadWebEngine.executeScript("document.getElementById('sem_change').value='"+matcher.group(1)+"';"+"document.getElementById('sem_change').dispatchEvent(new Event('change'));");
								downloadfortschrittProgressIndicator.setProgress(0.3);
							});

							try
							{
								Thread.sleep((getDownloadzeit()));
							}catch(Exception keineGefahrExcpetion)
							{
								//Die Gefahr ist gebannt, da der Thread nicht unterbrochen werden kann
								keineGefahrExcpetion.printStackTrace();
							}

							Platform.runLater(()->
							{
								Document modulübersichtDocument=webengineZuJSoupDocument(downloadWebEngine);

								if(modulübersichtDocument.select("tbody").size()!=0)
								{
									for(int i=0; i<modulübersichtDocument.select("tbody>tr").size(); i++)
									{
										System.out.println(i+" von "+modulübersichtDocument.select("tbody>tr").size());

										try
										{
											module.add(Jsoup.connect("https://www.hof-university.de"+modulübersichtDocument.select("tbody>tr").get(i).select("a").get(0).attr("href")).get());
										}
										catch(Exception keineGefahrException)
										{
											//Die Gefahr ist gebannt, da vor dem Aufruf dieser Methode die Internetverbindung überprüft wird und bei Nichtvorhandensein eines Modulhandbuchs, dieses einfach zurückgesetzt wird und der Abrufvorgang abgebrochen wird.
											SchreiberLeser.modulhandbuchNeuSetzen(new Modulhandbuch(new ArrayList<ModulhandbuchFach>()));
											downloadfortschrittProgressIndicator.setProgress(1);
											return;
										}
										downloadfortschrittProgressIndicator.setProgress(0.3+(((double) i)/(modulübersichtDocument.select("tbody>tr").size()-1))*0.6);
									}
								}
							});

							return null;
						}
					};
					downloadTask.stateProperty().addListener(((observable1, oldValue1, newValue1)->
					{
						if(newValue1==Worker.State.SUCCEEDED)
						{
								System.out.println(333);

								SchreiberLeser.modulhandbuchNeuSetzen(Parser.modulhandbuchParsen(module));
								Platform.runLater(()->downloadfortschrittProgressIndicator.setProgress(1));
						}
					}));
					new Thread(downloadTask).start();
				}
			});
			downloadWebEngine.getLoadWorker().stateProperty().addListener(downloadChangeListener);

			letzteListenerChangeListener=downloadChangeListener;

			messStartzeit=System.nanoTime();
			downloadWebEngine.load("https://www.hof-university.de/studierende/info-service/modulhandbuecher.html");
		});
	}

	//Mit diesr Methode wird der Mensaplan als Html-Datei abgerufen, geparst und im SchreiberLeser in Form eines Mensaplan-Objekt als Attribut gespeichert.
	public static void mensaplanAbrufen()
	{
		Calendar naechsterMontagDatumCalendar=Calendar.getInstance();

		if(naechsterMontagDatumCalendar.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||naechsterMontagDatumCalendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
		{
			naechsterMontagDatumCalendar.add(Calendar.WEEK_OF_YEAR, 1);
		}
		naechsterMontagDatumCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		ArrayList<DatumDocumentPaar> mensatage=new ArrayList<>();
		for(int i=0; i<6; i++)
		{
			int finalI=i;
			Platform.runLater(()->downloadfortschrittProgressIndicator.setProgress((((double) finalI)/5)*0.9));

			Document mensaplanDocument=null;
			try
			{
				mensaplanDocument=Jsoup.connect("https://www.studentenwerk-oberfranken.de/essen/speiseplaene/hof/"+new SimpleDateFormat("yyyy-MM-dd").format(naechsterMontagDatumCalendar.getTime())+".html").get();
			}
			catch(Exception keineGefahrException)
			{
				//Die Gefahr ist gebannt, da vor dem Aufruf dieser Methode die Internetverbindung überprüft wird
				keineGefahrException.printStackTrace();
			}
			Datum datum=new Datum(naechsterMontagDatumCalendar.get(Calendar.DAY_OF_MONTH), naechsterMontagDatumCalendar.get(Calendar.MONTH)+1, naechsterMontagDatumCalendar.get(Calendar.YEAR));
			DatumDocumentPaar thisTag=new DatumDocumentPaar(datum, mensaplanDocument);
			mensatage.add(thisTag);

			naechsterMontagDatumCalendar.add(Calendar.DATE, 1);
		}

		SchreiberLeser.mensaplanNeuSetzen(Parser.mensaplanParsen(new MensaplanTupel(mensatage.get(0), mensatage.get(1), mensatage.get(2), mensatage.get(3), mensatage.get(4), mensatage.get(5))));
		Platform.runLater(()->downloadfortschrittProgressIndicator.setProgress(1));
	}

	//Mit diese Methode werden die Treffpunkte als Json-Datei abgerufen, geparst und im SchreiberLeser in Form eines Treffpunkte-Objekt als Attribut gespeichert.
	public static void treffpunkteAbrufen()
	{
		try
		{
			SchreiberLeser.treffpunkteNeuSetzen(Parser.treffpunkteParsen(new JSONObject(IOUtils.toString(new URL("https://nebenwohnung.stevensolleder.de/Treffpunkte.json"), StandardCharsets.UTF_8))));
			Platform.runLater(()->downloadfortschrittProgressIndicator.setProgress(1));
		}
		catch(Exception keineGefahrException)
		{
			//Die Gefahr ist gebannt, da vor dem Aufruf dieser Methode die Internetverbindung überprüft wird
			keineGefahrException.printStackTrace();
		}
	}

	//Damit werden alle Studiengänge samt deren Semestern auf Basis des Dropdownmenüs von https://www.hof-university.de/studierende/info-service/stundenplaene.html abgerufen, geparst und als DropdownMenue-Objekt als Attribut im SpeicherLeser gesichert.
	public static void dropdownMenueAbrufen()
	{
		Platform.runLater(()->
		{
			ArrayList<NameKuerzelDocumentTripel> arrayList=new ArrayList<NameKuerzelDocumentTripel>();

			WebEngine webEngine=GrundViewController.getUglyWebview().getEngine();
			entferneLetztenListener(webEngine);

			ChangeListener<Worker.State> downloadChangeListener=((observable, oldValue, newValue)->
			{
				if(newValue==Worker.State.SUCCEEDED)
				{
					messEndzeit=System.nanoTime();

					Task<Void> downloadTask=new Task<Void>()
					{
						@Override
						protected Void call()
						{
							int studiengaengeDropdownEintraegeLaenge=0;

							try
							{
								studiengaengeDropdownEintraegeLaenge=Jsoup.connect("https://www.hof-university.de/studierende/info-service/stundenplaene.html").get().select("[name='tx_stundenplan_stundenplan[studiengang]']").get(0).childNodeSize();
							}catch(Exception keineGefahrException)
							{
								//Die Gefahr ist gebannt, da vor dem Aufruf dieser Methode die Internetverbindung überprüft wird
								keineGefahrException.printStackTrace();
							}
							Platform.runLater(()->downloadfortschrittProgressIndicator.setProgress(0.1));

							for(int i=1; i<studiengaengeDropdownEintraegeLaenge; i++)
							{
								int finalI=i;
								Platform.runLater(()->webEngine.executeScript("document.getElementsByName('tx_stundenplan_stundenplan[studiengang]')[0]["+finalI+"].selected='selected';"+"document.getElementsByName('tx_stundenplan_stundenplan[studiengang]')[0].dispatchEvent(new Event('change'));"));

								try
								{
									Thread.sleep((getDownloadzeit()));
								}catch(Exception keineGefahrExcpetion)
								{
									//Die Gefahr ist gebannt, da der Thread nicht unterbrochen werden kann
									keineGefahrExcpetion.printStackTrace();

								}

								Platform.runLater(()->
								{
									arrayList.add(new NameKuerzelDocumentTripel((String) webEngine.executeScript("document.getElementsByName('tx_stundenplan_stundenplan[studiengang]')[0]["+finalI+"].innerText;"), (String) webEngine.executeScript("document.getElementsByName('tx_stundenplan_stundenplan[studiengang]')[0]["+finalI+"].value;"), Downloader.webengineZuJSoupDocument(webEngine)));
								});

								downloadfortschrittProgressIndicator.setProgress(0.1 + (((double) i)/studiengaengeDropdownEintraegeLaenge) * 0.8 );
							}

								return null;
							}
					};

					downloadTask.stateProperty().addListener(((observable1, oldValue1, newValue1)->
					{
						if(newValue1==Worker.State.SUCCEEDED)
						{
							SchreiberLeser.dropdownMenueNeuSetzen(Parser.dropdownMenueParsen(arrayList));
							downloadfortschrittProgressIndicator.setProgress(1);
						}
					}));

					new Thread(downloadTask).start();
				}
			});
			webEngine.getLoadWorker().stateProperty().addListener(downloadChangeListener);

			letzteListenerChangeListener=downloadChangeListener;

			messStartzeit=System.nanoTime();
			webEngine.load("https://www.hof-university.de/studierende/info-service/stundenplaene.html");
		});
	}

	//Mit dieser Methode kann die ungefähr geschätzte Downloadzeit auf Basis von einer vorherig gemessenen Downloaddauer berechnet werden.
	private static long getDownloadzeit()
	{
		return (((messEndzeit-messStartzeit)/1000000)/2);
	}

	//Hiermit wird von der übergebenen WebEnginge der im Attribut letzteListenerChangeListener gespeicherte ChangeListener entfernt.
	private static void entferneLetztenListener(WebEngine webEngine)
	{
		if(letzteListenerChangeListener!=null)
		{
			webEngine.getLoadWorker().stateProperty().removeListener(letzteListenerChangeListener);
		}
	}

	//Diese Methode macht aus der Website, welche gerade in der übergebenen WebEnginge geladen ist, ein Document-Objekt der JSoup-Bibliotheken.
	private static Document webengineZuJSoupDocument(WebEngine webEngine)
	{
		return Jsoup.parse((String) webEngine.executeScript("document.documentElement.outerHTML"));
	}
}
