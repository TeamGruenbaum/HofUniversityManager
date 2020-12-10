package Controller.InformationsVermittlung;

import Model.TreffpunktModel.Treffpunkt;
import org.jsoup.*;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Datenabrufer {

    public static ArrayList<Document> studiengangAbrufen(String studienJahr, int studienSemester) throws IOException {
        Document doc = Jsoup.connect("https://www.hof-university.de/studierende/info-service/modulhandbuecher.html").get();

    }

    public static File treffpunkteAbrufen(){

    }

    public static File mensaplanAbrufen(){
        return "";
    }

}
