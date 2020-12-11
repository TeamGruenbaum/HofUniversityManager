package Controller.InformationsVermittlung;

import Model.TreffpunktModel.Treffpunkt;
import org.jsoup.*;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Datenabrufer {

   /* public static ArrayList<Document> studiengangAbrufen(String studienJahr, int studienSemester) throws IOException {

    }

    */



    public static String treffpunkteAbrufen(){
        return "www.Stevenswebsite.de/irgendwas.json";
    }

    public static ArrayList<Document> mensaplanAbrufen() throws IOException {

        // String[] daten = new String[6];
        ArrayList<Document> websites = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);

        for(int i=0; i<6; i++){

           // daten[i] = sdf.format(cal.getTime());
            String url = "https://www.studentenwerk-oberfranken.de/essen/speiseplaene/hof/" + sdf.format(cal.getTime()) + ".html";

            Document doc = Jsoup.connect(url).get();

            websites.add(doc);

            cal.add(Calendar.DATE, 1);

        }

        return websites;

    }

}
