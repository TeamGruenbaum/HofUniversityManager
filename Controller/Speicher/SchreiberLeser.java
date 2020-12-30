package Controller.Speicher;

import Model.DropdownModel.DropdownMenue;
import Model.DropdownModel.Studiensemester;
import Model.MensaplanModel.Mensaplan;
import Model.MensaplanModel.Tagesplan;
import Model.NutzerdatenModel.*;
import Model.OberflaechenModel.MenuepunktInformation;
import Model.StudiengangModel.ModulhandbuchFach;
import Model.StudiengangModel.StudiengangInformationen;
import Model.TreffpunktModel.Treffpunkt;
import Model.TreffpunktModel.Treffpunkte;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

    private static DropdownMenue dropdownMenue;
    private static String dropdownMenueDateiname="Dropdownmenue.sva";

    public static boolean isErsterStart()
    {
        File file = new File(__getSpeicherPfad() ,"nichtErsterStart");

        boolean ersterStart = false;

        try
        {
            if(!file.exists())
            {
                ersterStart = true;
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        }catch(Exception e){e.printStackTrace();}

        return ersterStart;
    }


    public static void datenZuruecksetzen()
    {
        studiengangInformationen=new StudiengangInformationen(new ArrayList<ModulhandbuchFach>());
        treffpunkte=new Treffpunkte(new ArrayList<Treffpunkt>());
        mensaplan=new Mensaplan(new ArrayList<Tagesplan>());
        nutzerdaten=new Nutzerdaten(new Studiensemester("",""), new ArrayList<Doppelstunde>(), new Login("", ""), Thema.HELL, new MenuepunktInformation(Anwendung.STUNDENPLAN,"platzhalter-icon.png", "Platzhalter.fxml"));

        alleSpeichern();
    }

    public static void alleLaden()
    {
        studiengangInformationenLaden();
        treffpunkteLaden();
        mensaplanLaden();
        nutzerdatenLaden();
        dropdownMenueLaden();
    }

    public static void alleSpeichern()
    {
        studiengangInformationenSpeichern();
        treffpunkteSpeichern();
        mensaplanSpeichern();
        nutzerdatenSpeichern();
        dropdownMenueSpeichern();
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

    public static void studiengangInformationenNeuSetzen(StudiengangInformationen neuerWert)
    {
        studiengangInformationen=neuerWert;
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

    public static void treffpunkteNeuSetzen(Treffpunkte neuerWert)
    {
        treffpunkte=neuerWert;
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

    public static void mensaplanNeuSetzen(Mensaplan neuerWert)
    {
        mensaplan=neuerWert;
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

    public static void nutzerdatenNeuSetzen(Nutzerdaten neuerWert)
    {
        nutzerdaten=neuerWert;
    }

    public static void nutzerdatenSpeichern()
    {
        SchreiberLeser.<Nutzerdaten>_schreiben(nutzerdaten, nutzerdatenDateiname);
    }

    //DropdownMenue
    public static void dropdownMenueLaden()
    {
        dropdownMenue=SchreiberLeser.<DropdownMenue>_lesen(dropdownMenueDateiname);
    }

    public static DropdownMenue getDropdownMenue()
    {
        return dropdownMenue;
    }

    public static void dropdownMenueNeuSetzen(DropdownMenue neuerWert)
    {
        dropdownMenue=neuerWert;
    }

    public static void dropdownMenueSpeichern()
    {
        SchreiberLeser.<DropdownMenue>_schreiben(dropdownMenue, dropdownMenueDateiname);
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
