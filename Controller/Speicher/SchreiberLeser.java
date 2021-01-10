package Controller.Speicher;



import Model.DropdownModel.DropdownMenue;
import Model.MensaplanModel.Mensaplan;
import Model.MensaplanModel.Tagesplan;
import Model.NutzerdatenModel.*;
import Model.OberflaechenModel.Menue;
import Model.ModulhandbuchModel.ModulhandbuchFach;
import Model.ModulhandbuchModel.Modulhandbuch;
import Model.TreffpunktModel.Treffpunkt;
import Model.TreffpunktModel.Treffpunkte;

import java.io.*;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;



public class SchreiberLeser
{
    private static Modulhandbuch modulhandbuch;
    private static String modulhandbuchDateiname="Modulhandbuch.sva";

    private static Treffpunkte treffpunkte;
    private static String treffpunkteDateiname="Treffpunkte.sva";

    private static Mensaplan mensaplan;
    private static String mensaplanDateiname="Mensaplan.sva";

    private static Nutzerdaten nutzerdaten;
    private static String nutzerdatenDateiname="Nutzerdaten.sva";

    private static DropdownMenue dropdownMenue;
    private static String dropdownMenueDateiname="Dropdownmenue.sva";



    //Solange die Datei "nichtErsterStart" im Speicherordner nicht vorhanden ist, gibt diese Methode true zurück.
    public static boolean isErsterStart()
    {
        return !(new File(getSpeicherPfad() ,"nichtErsterStart").exists());
    }

    //Damit wird im Speicherodner die Datei "nichtErsterStart" erstellt
    public static void isErsterStartSetzen()
    {
        File file = new File(getSpeicherPfad() ,"nichtErsterStart");
        try
        {
            if(!file.exists())
            {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        }
        catch(Exception keineGefahrExcpetion)
        {
            //Die Gefahr ist gebannt, da die für File benötigten Ordner und Datei erstellt werden, insofern sie nicht schon vorhanden sind.
            keineGefahrExcpetion.printStackTrace();
        }
    }


    //Diese Methode setzt alle Attribute, auf welchen im Laufe der Programmnutzung dauernd zugegriffen wird, auf einen sinnvollen "leeren" Zustand zurück.
    public static void alleZuruecksetzen()
    {
        modulhandbuch=new Modulhandbuch(new ArrayList<ModulhandbuchFach>());
        treffpunkte=new Treffpunkte(new ArrayList<Treffpunkt>());
        mensaplan=new Mensaplan(new ArrayList<Tagesplan>());
        ArrayList<String> faecherNamen=new ArrayList<String>();
        faecherNamen.add("Allgemein");

        nutzerdaten=new Nutzerdaten(null, null, new ArrayList<Doppelstunde>(),
            new ArrayList<Aufgabe>(), new ArrayList<Notiz>(), new ArrayList<Note>(),
            faecherNamen,
            new Login("", ""), Thema.HELL,
            Menue.getMenuepunktInformationen().get(Menue.getMenuepunktInformationen().size()-1));
    }

    //Diese Methode lädt alle persistent gespeicherten Daten in die entsprechenden Attribute.
    public static void alleLaden() throws Exception
    {
        modulhandbuch=SchreiberLeser.<Modulhandbuch>lesen(modulhandbuchDateiname);
        treffpunkte=SchreiberLeser.<Treffpunkte>lesen(treffpunkteDateiname);
        mensaplan=SchreiberLeser.<Mensaplan>lesen(mensaplanDateiname);
        nutzerdaten=SchreiberLeser.<Nutzerdaten>lesen(nutzerdatenDateiname);
        dropdownMenue=SchreiberLeser.<DropdownMenue>lesen(dropdownMenueDateiname);
    }

    //alleSpeichern() speichert alle Attribute, auf welche im Laufe der Programmnutzung dauernd zugegriffen wird, in den persistenten Speicher.
    public static void alleSpeichern()
    {
        SchreiberLeser.<Modulhandbuch>schreiben(modulhandbuch, modulhandbuchDateiname);
        SchreiberLeser.<Treffpunkte>schreiben(treffpunkte, treffpunkteDateiname);
        SchreiberLeser.<Mensaplan>schreiben(mensaplan, mensaplanDateiname);
        SchreiberLeser.<Nutzerdaten>schreiben(nutzerdaten, nutzerdatenDateiname);
        SchreiberLeser.<DropdownMenue>schreiben(dropdownMenue, dropdownMenueDateiname);
    }

    //Hiermit werden alle persistent gespeicherten Daten gelöscht.
    public static void alleDatenLoeschen()
    {
        try
        {
            FileUtils.forceDelete(new File(getSpeicherPfad()));
        }catch(Exception keineGefahrException)
        {
            //Die Gefahr ist gebannt, da, wenn der Ordner der zu löschen ist nicht vorhanden ist, wird einfach nichts gelöscht.
            keineGefahrException.printStackTrace();
        }
    }

    //Alle folgenden Methoden dienen dazu die Attribute abzurufen oder sie komplett neu zu besetzen
    public static Modulhandbuch getModulhandbuch()
    {
        return modulhandbuch;
    }

    public static void modulhandbuchNeuSetzen(Modulhandbuch neuerWert)
    {
        modulhandbuch=neuerWert;
    }


    public static Treffpunkte getTreffpunkte()
    {
        return treffpunkte;
    }

    public static void treffpunkteNeuSetzen(Treffpunkte neuerWert)
    {
        treffpunkte=neuerWert;
    }


    public static Mensaplan getMensaplan()
    {
        return mensaplan;
    }

    public static void mensaplanNeuSetzen(Mensaplan neuerWert)
    {
        mensaplan=neuerWert;
    }


    public static Nutzerdaten getNutzerdaten()
    {
        return nutzerdaten;
    }

    public static void nutzerdatenNeuSetzen(Nutzerdaten neuerWert)
    {
        nutzerdaten=neuerWert;
    }


    public static DropdownMenue getDropdownMenue()
    {
        return dropdownMenue;
    }

    public static void dropdownMenueNeuSetzen(DropdownMenue neuerWert)
    {
        dropdownMenue=neuerWert;
    }


    //Diese Methode speichert ein Objekt beliebigen Typs unter einem dem entsprechendem Dateinamen im Speicherordner.
    //Falls einzelne Daten verändert oder gelöscht werden, wird die Exception geworfen, darauf muss an entsprechender Stelle reagiert werden.
    private static <T extends Serializable> T lesen(String dateiname) throws Exception
    {
        FileInputStream leserFileInputStream=new FileInputStream(new File(getSpeicherPfad()+dateiname));
        ObjectInputStream leserObjectInputStream=new ObjectInputStream(leserFileInputStream);

        T gelesenesT=(T) leserObjectInputStream.readObject();

        leserObjectInputStream.close();
        leserFileInputStream.close();

        return gelesenesT;
    }

    //Hiermit wird ein Objekt beliebigen Typs unter einem entsprechenden Dateinamen im Speicherordner gespeichert.
    private static <T extends Serializable> void schreiben(T objekt, String dateiname)
    {
        File zielFile=new File(getSpeicherPfad()+dateiname);
        zielFile.getParentFile().mkdirs();
        try
        {
            FileOutputStream schreiberFileOutputStream = new FileOutputStream(zielFile, false);
            ObjectOutputStream schreiberObjectOutputStream = new ObjectOutputStream(schreiberFileOutputStream);

            schreiberObjectOutputStream.writeObject(objekt);

            schreiberObjectOutputStream.close();
            schreiberFileOutputStream.close();
        }
        catch(Exception keineGefahrException)
        {
            //Die Gefahr ist gebannt, da die für File benötigten Ordner und Datei erstellt werden, insofern sie nicht schon vorhanden sind.
            keineGefahrException.printStackTrace();
        }
    }

    //Diese Methode gibt den Speicherordner abhängig vom Betriebsystem zurück, die Methode funktioniert bei Windows als auch unxiodien bzw. unix-ähnlichen Betriebsystemen
    private static String getSpeicherPfad()
    {
        String nutzerpfad=System.getProperty("user.home");

        String betriebsystemart = System.getProperty("os.name");
        String speicherpfad;
        if(betriebsystemart.contains("Windows"))
        {
            speicherpfad= "\\Studentenverwaltungsanwendung\\";
        }
        else
        {
            speicherpfad="/.studentenverwaltungsanwendung/";
        }

        return nutzerpfad+speicherpfad;
    }
}
