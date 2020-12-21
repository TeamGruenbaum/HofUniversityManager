package Controller.Speicher;

import Model.MensaplanModel.Mensaplan;
import Model.MensaplanModel.Tagesplan;
import Model.NutzerdatenModel.*;
import Model.StudiengangModel.ModulhandbuchFach;
import Model.StudiengangModel.StudiengangInformationen;
import Model.TreffpunktModel.Treffpunkt;
import Model.TreffpunktModel.Treffpunkte;

import java.io.*;
import java.util.ArrayList;

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

    public static boolean isErsterStart()
    {
        File file = new File(__getSpeicherPfad() ,"notFirstStart");

        boolean firstRun = false;

        try
        {
            if(!file.exists())
            {
                firstRun = true;
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        }catch(Exception e){e.printStackTrace();}

        return firstRun;
    }


    public static void datenZuruecksetzen()
    {
        studiengangInformationen=new StudiengangInformationen("","","", new ArrayList<ModulhandbuchFach>());
        treffpunkte=new Treffpunkte(new ArrayList<Treffpunkt>());
        mensaplan=new Mensaplan(new ArrayList<Tagesplan>());
        nutzerdaten=new Nutzerdaten(0, new ArrayList<Doppelstunde>(), new Login("", ""), new Login("", ""), Thema.HELL, Anwendung.EINSTELLUNGEN);
    }

    //StudiengangInformationen
    public static void studiengangInformationenLaden()
    {
        studiengangInformationen=SchreiberLeser.<StudiengangInformationen>_lesen(studiengangInformationenDateiname);
    }

    public static StudiengangInformationen getStudiengangInformationen()
    {
        return studiengangInformationen;
    }

    public static void studiengangInformationenNeuSetzenUndSpeichern(StudiengangInformationen neuerWert)
    {
        studiengangInformationen=neuerWert;
        studiengangInformationenSpeichern();
    }

    public static void studiengangInformationenSpeichern()
    {
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

    public static void treffpunkteNeuSetzenUndSpeichern(Treffpunkte neuerWert)
    {
        treffpunkte=neuerWert;
        treffpunkteSpeichern();
    }

    public static void treffpunkteSpeichern()
    {
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

    public static void mensaplanNeuSetzenUndSpeichern(Mensaplan neuerWert)
    {
        mensaplan=neuerWert;
        mensaplanSpeichern();
    }

    public static void mensaplanSpeichern()
    {
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

    public static void nutzerdatenNeuSetzenUndSpeichern(Nutzerdaten neuerWert)
    {
        nutzerdaten=neuerWert;
        nutzerdatenSpeichern();
    }

    public static void nutzerdatenSpeichern()
    {
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
        }catch (Exception e){}



        return transferred;
    }



    private static <T extends Serializable> void _schreiben(T objekt, String dateiname)
    {
        File file=new File(__getSpeicherPfad()+dateiname);
        file.getParentFile().mkdirs();

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
        String nutzerPfad=System.getProperty("user.home");
        String OS = System.getProperty("os.name");
        String speicherPfad;

        if(OS.contains("Windows"))
        {
            speicherPfad= "\\Studentenverwaltungsanwendung\\";
        }
        else
        {
            speicherPfad="/.studentenverwaltungsanwendung/";
        }

        return nutzerPfad+speicherPfad;
    }
}
