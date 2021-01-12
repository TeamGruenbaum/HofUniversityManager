package Controller.InformationsVermittlung;



import Controller.InformationsVermittlung.Hilfsklassen.NameKuerzelDocumentTripel;
import Controller.InformationsVermittlung.Hilfsklassen.DatumDocumentPaar;
import Controller.InformationsVermittlung.Hilfsklassen.MensaplanTupel;
import Controller.Speicher.SchreiberLeser;
import Controller.ViewController.GrundViewController;
import Model.Datum;

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



	public static void
	setDownloadfortschrittProgressIndicator(ProgressIndicator neuerWertProgressIndicator)
	{
		downloadfortschrittProgressIndicator=neuerWertProgressIndicator;
	}

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
								downloadWebEnginge.executeScript("document.getElementsByName('tx_stundenplan_stundenplan[studiengang]')[0].value='"+SchreiberLeser.getNutzerdaten().getStudiengang().getKuerzel()+"';"+"document.getElementsByName('tx_stundenplan_stundenplan[studiengang]')[0].dispatchEvent(new Event('change'));");
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
								downloadWebEnginge.executeScript("document.getElementsByName('tx_stundenplan_stundenplan[semester]')[0].value='"+SchreiberLeser.getNutzerdaten().getStudiensemester().getKuerzel()+"';"+"document.getElementsByName('tx_stundenplan_stundenplan[semester]')[0].dispatchEvent(new Event('change'));");
								downloadfortschrittProgressIndicator.setProgress(0.75);
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

	//TODO - Text in View, Fehler beim runterladen
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
								downloadWebEngine.executeScript("document.getElementById('class_change').value='"+SchreiberLeser.getNutzerdaten().getStudiengang().getKuerzel()+"';"+"document.getElementById('class_change').dispatchEvent(new Event('change'));");
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
								Matcher matcher=pattern.matcher(SchreiberLeser.getNutzerdaten().getStudiensemester().getKuerzel());
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
								Matcher matcher=pattern.matcher(SchreiberLeser.getNutzerdaten().getStudiensemester().getKuerzel());
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

							Document modulübersichtDocument=webengineZuJSoupDocument(downloadWebEngine);
							Platform.runLater(()->
							{
								if(modulübersichtDocument.select("tbody").size()!=0)
								{
									for(int i=0; i<modulübersichtDocument.select("tbody>tr").size(); i++)
									{
										try
										{
											module.add(Jsoup.connect("https://www.hof-university.de"+modulübersichtDocument.select("tbody>tr").get(i).select("a").get(0).attr("href")).get());
										}
										catch(Exception keineGefahrException)
										{
											//Die Gefahr ist gebannt, da vor dem Aufruf dieser Methode die Internetverbindung überprüft wird
											keineGefahrException.printStackTrace();
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
			Platform.runLater(()->downloadfortschrittProgressIndicator.setProgress(((double) finalI)/5));

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
	}

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


	private static long getDownloadzeit()
	{
		return (((messEndzeit-messStartzeit)/1000000)/2);
	}

	private static void entferneLetztenListener(WebEngine webEngine)
	{
		if(letzteListenerChangeListener!=null)
		{
			webEngine.getLoadWorker().stateProperty().removeListener(letzteListenerChangeListener);
		}
	}

	private static Document webengineZuJSoupDocument(WebEngine webEngine)
	{
		return Jsoup.parse((String) webEngine.executeScript("document.documentElement.outerHTML"));
	}
}
