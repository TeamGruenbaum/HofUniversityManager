package Controller.Speicher;

import Model.MensaplanModel.Mensaplan;
import Model.NutzerdatenModel.Nutzerdaten;
import Model.QuicklinksModel.Quicklinks;
import Model.StudiengangModel.StudiengangInformationen;
import Model.TreffpunktModel.Treffpunkt;
import Model.TreffpunktModel.Treffpunkte;

import java.io.*;

public class SchreiberLeser
{
    private static StudiengangInformationen studiengangInformationen;
    private static String studiengangInformationenDateiname="StudiengangInformationen.sva";

    private static Treffpunkte treffpunkte;
    private static String treffpunkteDateiname="Treffpunkte.sva";

    private static Mensaplan mensaplan;
    private static String mensaplanDateiname="Mensaplan.sva";

    private static Nutzerdaten nutzerdaten;
    private static String nutzerdatenDateiname="Nutzerdaten.sva";


    //StudiengangInformationen
    public static void studiengangInformationenLaden()
    {
        studiengangInformationen=SchreiberLeser.<StudiengangInformationen>_lesen(studiengangInformationenDateiname);
    }

    public static StudiengangInformationen getStudiengangInformationen()
    {
        return studiengangInformationen;
    }

    public static void studiengangInformationenSetzen(StudiengangInformationen neuerWert)
    {
        studiengangInformationen=neuerWert;
        SchreiberLeser.<StudiengangInformationen>_schreiben(studiengangInformationen, studiengangInformationenDateiname);
    }


    //Treffpunkt
    public static void treffpunkteLaden()
    {
        treffpunkte=SchreiberLeser.<Treffpunkte>_lesen(treffpunkteDateiname);
    }

    public static Treffpunkte getTreffpunkte()
    {
        return treffpunkte;
    }

    public static void treffpunkteSpeichern(Treffpunkte neuerWert)
    {
        treffpunkte=neuerWert;
        SchreiberLeser.<Treffpunkte>_schreiben(treffpunkte, treffpunkteDateiname);
    }


    //Mensaplan
    public static void mensaplanLaden()
    {
        mensaplan=SchreiberLeser.<Mensaplan>_lesen(mensaplanDateiname);
    }

    public static Mensaplan getMensaplan()
    {
        return mensaplan;
    }

    public static void mensaplanSpeichern(Mensaplan neuerWert)
    {
        mensaplan=neuerWert;
        SchreiberLeser.<Mensaplan>_schreiben(mensaplan, mensaplanDateiname);
    }


    //Nutzerdaten
    public static void nutzerdatenLaden()
    {
        nutzerdaten=SchreiberLeser.<Nutzerdaten>_lesen(nutzerdatenDateiname);
    }

    public static Nutzerdaten getNutzerdaten()
    {
        return nutzerdaten;
    }

    public static void nutzerdatenSpeichern(Nutzerdaten neuerWert)
    {
        nutzerdaten=neuerWert;
        SchreiberLeser.<Nutzerdaten>_schreiben(nutzerdaten, mensaplanDateiname);
    }


    //Allgemeines Speichern und Lesen
    private static <T extends Serializable> T _lesen(String dateiname)
    {
        T transferred=null;

        File file=new File(__getSpeicherPfad()+dateiname);

        try
        {
            FileInputStream fileInputStream=new FileInputStream(file);
            ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
            transferred=(T) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        }
        catch (Exception exception){}

        return transferred;
    }

    private static <T extends Serializable> void _schreiben(T objekt, String dateiname)
    {
        File file=new File(__getSpeicherPfad()+dateiname);

        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            T transferred=objekt;
            objectOutputStream.writeObject(transferred);

            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch (Exception e){}
    }

    private static String __getSpeicherPfad()
    {
        String nutzerPfad=System.getProperty("os.home");
        String speicherPfad;

        if(nutzerPfad.contains("Windows"))
        {
            speicherPfad="/Studentenverwaltungsanwendung/";
        }
        else
        {
            speicherPfad="/.studentenverwaltungsanwendung/";
        }

        return nutzerPfad+speicherPfad;
    }
}
