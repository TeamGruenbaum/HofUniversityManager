package Controller.InformationsVermittlung;

import Controller.Speicher.SchreiberLeser;

import Model.Datum;
import Model.TreffpunktModel.Freizeitaktivitaet;
import Model.TreffpunktModel.Restaurant;
import Model.TreffpunktModel.Treffpunkt;
import Model.TreffpunktModel.Treffpunkte;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
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
    public static void studiengangAbrufen(int studienJahr, int studienSemester, int studienfach) throws IOException {

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
