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



public class Internetdatenatenabrufer
{
	private static ProgressIndicator fortschrittProgressIndicator;
	private static long start, end;
	private static ChangeListener<Worker.State> letzteListener=null;

	public static void setFortschrittProgressIndicator(ProgressIndicator neuerWertProgressIndicator)
	{
		fortschrittProgressIndicator=neuerWertProgressIndicator;
	}

	public static void stundenplanAbrufen()
	{

		Platform.runLater(()->
		{
			fortschrittProgressIndicator.setProgress(0);

			WebEngine webEngine=GrundViewController.getUglyWebview().getEngine();

			entferneLetztenListener(webEngine);

			ChangeListener<Worker.State> stateChangeListener=(observable, oldValue, newValue)->
			{
				if(newValue==Worker.State.SUCCEEDED)
				{
					end=System.nanoTime();

					fortschrittProgressIndicator.setProgress(0.25);


					Task<Void> task=new Task<Void>()
					{
						@Override
						protected Void call() throws Exception
						{

							Platform.runLater(()->
							{
								webEngine.executeScript("document.getElementsByName('tx_stundenplan_stundenplan[studiengang]')[0].value='"+SchreiberLeser.getNutzerdaten().getStudiengang().getKuerzel()+"';"+"document.getElementsByName('tx_stundenplan_stundenplan[studiengang]')[0].dispatchEvent(new Event('change'));");
							});
							fortschrittProgressIndicator.setProgress(0.5);

							Thread.sleep(((end-start)/1000000)/2);

							Platform.runLater(()->
							{
								webEngine.executeScript("document.getElementsByName('tx_stundenplan_stundenplan[semester]')[0].value='"+SchreiberLeser.getNutzerdaten().getStudiensemester().getKuerzel()+"';"+"document.getElementsByName('tx_stundenplan_stundenplan[semester]')[0].dispatchEvent(new Event('change'));");
							});
							fortschrittProgressIndicator.setProgress(0.75);

							Thread.sleep(((end-start)/1000000)/2);

							Platform.runLater(()->
							{
								SchreiberLeser.getNutzerdaten().setDoppelstunden(Parser.stundenplanParsen(Jsoup.parse((String) webEngine.executeScript("document.documentElement.outerHTML"))));
								fortschrittProgressIndicator.setProgress(1);
							});

							return null;
						}
					};

					new Thread(task).start();
				}
			};

			webEngine.getLoadWorker().stateProperty().addListener(stateChangeListener);

			letzteListener=stateChangeListener;

			start=System.nanoTime();
			webEngine.load("https://www.hof-university.de/studierende/info-service/stundenplaene.html");
		});
	}

	public static void studiengangAbrufen()
	{
		Platform.runLater(()->
		{
			fortschrittProgressIndicator.setProgress(0);

			WebEngine webEngine=GrundViewController.getUglyWebview().getEngine();

			entferneLetztenListener(webEngine);

			ChangeListener<Worker.State> stateChangeListener=((observable, oldValue, newValue)->
			{
				if(newValue==Worker.State.SUCCEEDED)
				{
					ArrayList<Document> arrayList=new ArrayList<>();

					end=System.nanoTime();

					Task<Void> task=new Task<Void>()
					{
						@Override
						protected Void call() throws Exception
						{

							Platform.runLater(()->
							{
								webEngine.executeScript("document.getElementById('class_change').value='"+SchreiberLeser.getNutzerdaten().getStudiengang().getKuerzel()+"';"+"document.getElementById('class_change').dispatchEvent(new Event('change'));");
							});

							Platform.runLater(()->fortschrittProgressIndicator.setProgress(0.1));

							Thread.sleep(((end-start)/1000000)/2);

							Pattern pattern=Pattern.compile("(.*)(#)([A-Z]{2}#\\d{4})"); //1a#SS#2020

							Platform.runLater(()->
							{
								Matcher matcher=pattern.matcher(SchreiberLeser.getNutzerdaten().getStudiensemester().getKuerzel());
								matcher.matches();

								webEngine.executeScript("document.getElementById('year_change').value='"+matcher.group(3).replace('#', ' ')+"';"+"document.getElementById('year_change').dispatchEvent(new Event('change'));");
							});

							Platform.runLater(()->fortschrittProgressIndicator.setProgress(0.2));

							Thread.sleep(((end-start)/1000000)/2);

							Platform.runLater(()->
							{
								Matcher matcher=pattern.matcher(SchreiberLeser.getNutzerdaten().getStudiensemester().getKuerzel());
								matcher.matches();

								webEngine.executeScript("document.getElementById('sem_change').value='"+matcher.group(1)+"';"+"document.getElementById('sem_change').dispatchEvent(new Event('change'));");
							});

							Thread.sleep(((end-start)/1000000)/2);

							Platform.runLater(()->fortschrittProgressIndicator.setProgress(0.3));

							Platform.runLater(()->
							{
								Document dokument=_webengineZuJSoupDocument(webEngine);

								if(dokument.select("tbody").size()!=0)
								{

									for(int i=0; i<dokument.select("tbody>tr").size(); i++)
									{
										try
										{
											arrayList.add(Jsoup.connect("https://www.hof-university.de"+dokument.select("tbody>tr").get(i).
												select("a").get(0).attr("href")).get());
										}
										catch(Exception keineGefahrException)
										{
											//Die Gefahr ist gebannt, da vor dem Aufruf dieser Methode die Internetverbindung überprüft wird
											keineGefahrException.printStackTrace();
										}

										int finalI=i;
										Platform.runLater(()->
										{
											fortschrittProgressIndicator.setProgress(0.3+(((double) finalI)/(dokument.select("tbody>tr").size()-1))*0.6);
										});
									}
								}
							});

							return null;
						}
					};

					task.stateProperty().addListener(((observable1, oldValue1, newValue1)->
					{
						if(newValue1==Worker.State.SUCCEEDED)
						{

								SchreiberLeser.modulhandbuchNeuSetzen(Parser.modulhandbuchParsen(arrayList));


							Platform.runLater(()->fortschrittProgressIndicator.setProgress(1));
						}
					}));

					new Thread(task).start();
				}
			});

			webEngine.getLoadWorker().stateProperty().addListener(stateChangeListener);

			letzteListener=stateChangeListener;

			start=System.nanoTime();
			webEngine.load("https://www.hof-university.de/studierende/info-service/modulhandbuecher.html");
		});
	}

	public static void mensaplanAbrufen()
	{
		ArrayList<DatumDocumentPaar> mensatage=new ArrayList<>();

		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal=Calendar.getInstance();
		if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY)
		{  //Gibt bei Samstag/Sonntag die nächste Woche aus
			cal.add(Calendar.WEEK_OF_YEAR, 1);
		}
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		for(int i=0; i<6; i++)
		{
			int finalI=i;
			Platform.runLater(()->
			{
				fortschrittProgressIndicator.setProgress(((double) finalI)/5);
			});

			String url="https://www.studentenwerk-oberfranken.de/essen/speiseplaene/hof/"+sdf.format(cal.getTime())+".html";

			Document doc=null;

			try
			{
				doc=Jsoup.connect(url).get();
			}catch(Exception keineGefahrException)
			{
				//Die Gefahr ist gebannt, da vor dem Aufruf dieser Methode die Internetverbindung überprüft wird
				keineGefahrException.printStackTrace();
			}

			Datum datum=new Datum(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH)+1, cal.get(Calendar.YEAR));

			DatumDocumentPaar thisTag=new DatumDocumentPaar(datum, doc);

			mensatage.add(thisTag);

			cal.add(Calendar.DATE, 1);
		}

		SchreiberLeser.mensaplanNeuSetzen(Parser.mensaplanParsen(new MensaplanTupel(mensatage.get(0), mensatage.get(1), mensatage.get(2), mensatage.get(3), mensatage.get(4), mensatage.get(5))));
	}

	public static void treffpunkteAbrufen()
	{

		try
		{
			SchreiberLeser.treffpunkteNeuSetzen(Parser.treffpunkteParsen(new JSONObject(IOUtils.toString(new URL("https://nebenwohnung.stevensolleder.de/Treffpunkte.json"), StandardCharsets.UTF_8))));
			Platform.runLater(()->
			{
				fortschrittProgressIndicator.setProgress(1);
			});
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

			ChangeListener<Worker.State> changeListener=((observable, oldValue, newValue)->
			{
				if(newValue==Worker.State.SUCCEEDED)
				{
					end=System.nanoTime();

					Task<Void> task=new Task<Void>()
					{
						@Override
						protected Void call()
						{
							int laenge=0;

							try
							{
								laenge=Jsoup.connect("https://www.hof-university.de/studierende/info-service/stundenplaene.html").get().select("[name='tx_stundenplan_stundenplan[studiengang]']").get(0).childNodeSize();
							}catch(Exception keineGefahrException)
							{
								//Die Gefahr ist gebannt, da vor dem Aufruf dieser Methode die Internetverbindung überprüft wird
								keineGefahrException.printStackTrace();
							}

							fortschrittProgressIndicator.setProgress(0.1);

							for(int i=1; i<laenge; i++)
							{
								updateProgress(i, laenge);

								int finalI=i;

								Platform.runLater(()->
								{
									webEngine.executeScript("document.getElementsByName('tx_stundenplan_stundenplan[studiengang]')[0]["+finalI+"].selected='selected';"+"document.getElementsByName('tx_stundenplan_stundenplan[studiengang]')[0].dispatchEvent(new Event('change'));");
								});

								try
								{
									Thread.sleep(((end-start)/1000000)/2);
								}catch(Exception keineGefahrExcpetion)
								{
									//Die Gefahr ist gebannt, da der Thread nicht unterbrochen werden kann
									keineGefahrExcpetion.printStackTrace();

								}

								Platform.runLater(()->
								{
									arrayList.add(new NameKuerzelDocumentTripel((String) webEngine.executeScript("document.getElementsByName('tx_stundenplan_stundenplan[studiengang]')[0]["+finalI+"].innerText;"), (String) webEngine.executeScript("document.getElementsByName('tx_stundenplan_stundenplan[studiengang]')[0]["+finalI+"].value;"), Internetdatenatenabrufer._webengineZuJSoupDocument(webEngine)));
								});

								fortschrittProgressIndicator.setProgress(0.1 + (((double) i)/laenge) * 0.8 );
							}

							return null;
						}
					};

					//EinstellungenViewController.getStudiengangAktualisierungsProgressBar().progressProperty().bind(task.progressProperty());

					task.stateProperty().addListener(((observable1, oldValue1, newValue1)->
					{
						if(newValue1==Worker.State.SUCCEEDED)
						{
							SchreiberLeser.dropdownMenueNeuSetzen(Parser.dropdownMenueParsen(arrayList));

							fortschrittProgressIndicator.setProgress(1);
						}
					}));

					new Thread(task).start();
				}
			});

			webEngine.getLoadWorker().stateProperty().addListener(changeListener);

			letzteListener=changeListener;

			start=System.nanoTime();
			webEngine.load("https://www.hof-university.de/studierende/info-service/stundenplaene.html");
		});
	}

	private static Document _webengineZuJSoupDocument(WebEngine webEngine)
	{

		return Jsoup.parse((String) webEngine.executeScript("document.documentElement.outerHTML"));
	}

	private static void entferneLetztenListener(WebEngine webEngine)
	{
		if(letzteListener!=null)
		{
			webEngine.getLoadWorker().stateProperty().removeListener(letzteListener);
		}
	}
}
