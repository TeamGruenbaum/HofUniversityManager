package Controller.InformationsVermittlung;

import Model.TreffpunktModel.Treffpunkt;
import org.jsoup.*;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Datenabrufer {

    public static ArrayList<Document> studiengangAbrufen(String studienJahr, int studienSemester) throws IOException {
        Document doc = Jsoup.connect("https://www.hof-university.de/studierende/info-service/modulhandbuecher.html").get();

    }

    public static File treffpunkteAbrufen(){

    }

    public static File mensaplanAbrufen(){

        String[] daten = new String[6];

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);

        for(int i=0; i<6; i++){

            daten[i] = sdf.format(cal.getTime());
            cal.add(Calendar.DATE, 1);


        }


    }

}
