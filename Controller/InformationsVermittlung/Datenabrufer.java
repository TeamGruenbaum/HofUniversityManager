package Controller.InformationsVermittlung;

import Controller.Main;
import Controller.Speicher.SchreiberLeser;

import Controller.ViewController.GrundViewController;
import Model.Datum;
import Model.NutzerdatenModel.Doppelstunde;
import Model.TreffpunktModel.Freizeitaktivitaet;
import Model.TreffpunktModel.Restaurant;
import Model.TreffpunktModel.Treffpunkt;
import Model.TreffpunktModel.Treffpunkte;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.*;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;


public class Datenabrufer
{
    @FXML

    private static long start, end;

    public static void studiengangAbrufen(int studienJahr, int studienSemester, int studienfach) throws IOException
    {
        WebView webView = new WebView();

        final WebEngine we = webView.getEngine();

        we.load("https://www.hof-university.de/studierende/info-service/modulhandbuecher.html");

        we.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {

            if (newState == Worker.State.SUCCEEDED) {
                // new page has loaded, process:
                _fetchData(we, studienfach, studienJahr,studienSemester);
            }
        });

        //SchreiberLeser.studiengangInformationenNeuSetzenUndSpeichern(...);
    }

    public static void treffpunkteAbrufen()
    {
        try
        {
            SchreiberLeser.treffpunkteNeuSetzenUndSpeichern(Parser.treffpunkteParsen(new JSONObject(IOUtils.toString(new URL("https://nebenwohnung.stevensolleder.de/Treffpunkte.json"), Charset.forName("UTF-8")))));

        }
        catch(Exception e){}
    }

    public static void mensaplanAbrufen() throws IOException {
        ArrayList<MensaTag> mensatage = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){  //Gibt bei Samstag/Sonntag die nächste Woche aus
            cal.add(Calendar.WEEK_OF_YEAR, 1);
        }
        cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);

        for(int i=0; i<6; i++){


            String url = "https://www.studentenwerk-oberfranken.de/essen/speiseplaene/hof/" + sdf.format(cal.getTime()) + ".html";

            Document doc = Jsoup.connect(url).get();

            Datum datum = new Datum(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));

            MensaTag thisTag = new MensaTag(doc, datum);

            mensatage.add(thisTag);

            cal.add(Calendar.DATE, 1);

        }

        SchreiberLeser.mensaplanNeuSetzenUndSpeichern(Parser.mensaplanParsen(new MensaplanDokumente(mensatage)));
    }

    public static void stundenplanAbrufen()
    {
        Platform.runLater(()->
        {
            WebEngine webEngine= GrundViewController.getUglyWebview().getEngine();

            webEngine.getLoadWorker().stateProperty().addListener(((observable, oldValue, newValue) ->
            {
                System.out.println(observable);

                if(newValue== Worker.State.SUCCEEDED)
                {
                    end=System.nanoTime();

                    System.out.println((end-start)/1000000);

                    Task<Void> task = new Task<Void>()
                    {
                        @Override
                        protected Void call() throws Exception
                        {
                            Platform.runLater(() -> {
                                System.out.println(1);
                                webEngine.executeScript(
                                        "document.getElementsByName('tx_stundenplan_stundenplan[studiengang]')[0].value='MI';" +
                                                "document.getElementsByName('tx_stundenplan_stundenplan[studiengang]')[0].dispatchEvent(new Event('change'));");
                            });

                            Thread.sleep(((end-start)/1000000)/2);

                            Platform.runLater(() -> {
                                System.out.println(2);
                                webEngine.executeScript(
                                        "document.getElementsByName('tx_stundenplan_stundenplan[semester]')[0].children[4].selected=true;" +
                                                "document.getElementsByName('tx_stundenplan_stundenplan[semester]')[0].dispatchEvent(new Event('change'));");
                            });

                            Thread.sleep(((end-start)/1000000)/2);

                            return null;
                        }
                    };

                    task.stateProperty().addListener(((observable1, oldValue1, newValue1) ->
                    {
                        if(newValue1== Worker.State.SUCCEEDED)
                        {
                            SchreiberLeser.getNutzerdaten().setDoppelstunden(Parser.stundenplanParsen(Jsoup.parse((String) webEngine.executeScript("document.documentElement.outerHTML"))));
                            SchreiberLeser.nutzerdatenSpeichern();
                        }
                    }));

                    new Thread(task).start();
                }
            }));

            start=System.nanoTime();
            webEngine.load("https://www.hof-university.de/studierende/info-service/stundenplaene.html");
        });
    }

    public static void dropdownMenueAbrufen()
    {
        Platform.runLater(()->
        {
            WebEngine webEngine= GrundViewController.getUglyWebview().getEngine();

            webEngine.getLoadWorker().stateProperty().addListener(((observable, oldValue, newValue) ->
            {
                System.out.println(observable);

                if(newValue== Worker.State.SUCCEEDED)
                {
                    end=System.nanoTime();

                    System.out.println((end-start)/1000000);

                    Task<Void> task = new Task<Void>()
                    {
                        @Override
                        protected Void call() throws Exception
                        {
                            Platform.runLater(() -> {
                                System.out.println(1);
                                webEngine.executeScript(
                                        "document.getElementsByName('tx_stundenplan_stundenplan[studiengang]')[0].value='MI';" +
                                                "document.getElementsByName('tx_stundenplan_stundenplan[studiengang]')[0].dispatchEvent(new Event('change'));");
                            });

                            Thread.sleep(((end-start)/1000000)/2);

                            return null;
                        }
                    };

                    task.stateProperty().addListener(((observable1, oldValue1, newValue1) ->
                    {
                        if(newValue1== Worker.State.SUCCEEDED)
                        {
                            //SchreiberLeser.studiengangDropdownNeuSetzenUndSpeichern(Parser.stundenplanDropdownParsen(Jsoup.parse((String) webEngine.executeScript("document.documentElement.outerHTML"))));
                        }
                    }));

                    new Thread(task).start();
                }
            }));

            start=System.nanoTime();
            webEngine.load("https://www.hof-university.de/studierende/info-service/stundenplaene.html");
        });
    }

    private static void _fetchData(WebEngine we, int segment, int year, int sem)
    {
       __selectOption(we, "class_change", segment);    //Loaded first segment

        CountDownLatch latch = new CountDownLatch(3);

        Task<Void> sleeper1 = new Task<Void>() {                            //Task for second segment
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(500);

                    Platform.runLater(() -> {
                        __selectOption(we,"year_change", year);
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        Task<Void> sleeper2 = new Task<Void>() {                                //Task for thrid segment
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        __selectOption(we, "sem_change", sem);
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        System.out.println("Starte Threads jetzt!");

        Thread thread1 = new Thread(sleeper1);
        Thread thread2 = new Thread(sleeper2);

        thread1.start();                //loading second segment
        thread2.start();                //loading third segment


    }

    private static void __selectOption(WebEngine we, String id, int intid){

        we.executeScript(" document.getElementById(\"" + id + "\").childNodes[" + intid + "].selected=\"selected\";");

        we.executeScript("if (\"createEvent\" in document) {\n" +
                "                    var evt = document.createEvent(\"HTMLEvents\");\n" +
                "                    evt.initEvent(\"change\", false, true);\n" +
                "                    document.getElementById(\"" + id + "\").dispatchEvent(evt);\n" +
                "                }\n" +
                "                else\n" +
                "                    document.getElementById(\"" + id + "\").fireEvent(\"onchange\");");


    }
}
