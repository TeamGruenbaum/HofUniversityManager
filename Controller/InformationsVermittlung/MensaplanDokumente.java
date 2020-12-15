package Controller.InformationsVermittlung;

import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;

public class MensaplanDokumente {
    private ArrayList<MensaTag> mensaplanDokumente;
    private MensaTag montag;
    private MensaTag dienstag;
    private MensaTag mittwoch;
    private MensaTag donnerstag;
    private MensaTag freitag;
    private MensaTag samstag;

    public MensaplanDokumente(ArrayList<MensaTag> mensaDokumente){
        this.mensaplanDokumente = mensaDokumente;
        montag = mensaDokumente.get(0);
        dienstag = mensaDokumente.get(1);
        mittwoch = mensaDokumente.get(2);
        donnerstag = mensaDokumente.get(3);
        freitag = mensaDokumente.get(4);
        samstag = mensaDokumente.get(5);
    }

    public ArrayList<MensaTag> getMensaDokumente() {
        return mensaplanDokumente;
    }

    public MensaTag getMontag() {
        if(montag!=null){
            return montag;
        }
        else {
            System.out.println("Kein Dokument vorhanden");
            return null;
        }
    }

    public MensaTag getDienstag() {
        if(dienstag!=null){
            return dienstag;
        }
        else {
            System.out.println("Kein Dokument vorhanden");
            return null;
        }
    }

    public MensaTag getMittwoch() {
        if(mittwoch!=null){
            return mittwoch;
        }
        else {
            System.out.println("Kein Dokument vorhanden");
            return null;
        }
    }

    public MensaTag getDonnerstag() {
        if(donnerstag!=null){
            return donnerstag;
        }
        else {
            System.out.println("Kein Dokument vorhanden");
            return null;
        }
    }

    public MensaTag getFreitag() {
        if(freitag!=null){
            return freitag;
        }
        else {
            System.out.println("Kein Dokument vorhanden");
            return null;
        }
    }

    public MensaTag getSamstag() {
        if(samstag!=null){
            return samstag;
        }
        else {
            System.out.println("Kein Dokument vorhanden");
            return null;
        }
    }
}
