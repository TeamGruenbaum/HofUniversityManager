package Controller.Speicher;

import Model.Datum;
import Model.DropdownModel.DropdownMenue;
import Model.DropdownModel.Studiensemester;
import Model.MensaplanModel.Mensaplan;
import Model.MensaplanModel.Tagesplan;
import Model.NutzerdatenModel.*;
import Model.OberflaechenModel.Menue;
import Model.OberflaechenModel.MenuepunktInformation;
import Model.StudiengangModel.ModulhandbuchFach;
import Model.StudiengangModel.StudiengangInformationen;
import Model.TreffpunktModel.Treffpunkt;
import Model.TreffpunktModel.Treffpunkte;

import Model.Uhrzeit;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
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
        return !(new File(getSpeicherPfad() ,"nichtErsterStart").exists());
    }

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
            //Die Gefahr ist gebannt, da die für File benötigten Ordner und Datei erstellt werden, insofern sie nicht schon vorhanden sind
            keineGefahrExcpetion.printStackTrace();
        }
    }

    public static void alleZuruecksetzen()
    {
        studiengangInformationen=new StudiengangInformationen(new ArrayList<ModulhandbuchFach>());
        treffpunkte=new Treffpunkte(new ArrayList<Treffpunkt>());
        mensaplan=new Mensaplan(new ArrayList<Tagesplan>());

        ArrayList<String> temp=new ArrayList<String>();
        temp.add("Allgemein");
        nutzerdaten=new Nutzerdaten(null, null, new ArrayList<Doppelstunde>(),
            new FachDatensatz(new ArrayList<Aufgabe>(), new ArrayList<Notiz>(), new ArrayList<Note>()),
            temp,
            new Login("", ""), Thema.HELL,
            Menue.getMenuepunkte().get(Menue.getMenuepunkte().size()-1));

        kopieren(new File(SchreiberLeser.class.getResource("../../Ressourcen/Andere/Dropdownmenue.sva").getPath()), "Dropdownmenue.sva");
        try
        {
            dropdownMenue=SchreiberLeser.<DropdownMenue>lesen(dropdownMenueDateiname);
        }
        catch(Exception keineGefahrExcpetion)
        {
            //Die Gefahr ist gebannt, da zwischen Kopieren und Lesen nicht genügend Zeit ist, die Datei zu manipulieren
            keineGefahrExcpetion.printStackTrace();
        }
    }

    public static void alleLaden() throws Exception
    {
        studiengangInformationen=SchreiberLeser.<StudiengangInformationen>lesen(studiengangInformationenDateiname);
        treffpunkte=SchreiberLeser.<Treffpunkte>lesen(treffpunkteDateiname);
        mensaplan=SchreiberLeser.<Mensaplan>lesen(mensaplanDateiname);
        nutzerdaten=SchreiberLeser.<Nutzerdaten>lesen(nutzerdatenDateiname);
        dropdownMenue=SchreiberLeser.<DropdownMenue>lesen(dropdownMenueDateiname);
    }

    public static void alleSpeichern()
    {
        SchreiberLeser.<StudiengangInformationen>schreiben(studiengangInformationen, studiengangInformationenDateiname);
        SchreiberLeser.<Treffpunkte>schreiben(treffpunkte, treffpunkteDateiname);
        SchreiberLeser.<Mensaplan>schreiben(mensaplan, mensaplanDateiname);
        SchreiberLeser.<Nutzerdaten>schreiben(nutzerdaten, nutzerdatenDateiname);
        SchreiberLeser.<DropdownMenue>schreiben(dropdownMenue, dropdownMenueDateiname);
    }

    //StudiengangInformationen
    public static StudiengangInformationen getStudiengangInformationen()
    {
        return studiengangInformationen;
    }

    public static void studiengangInformationenNeuSetzen(StudiengangInformationen neuerWert)
    {
        studiengangInformationen=neuerWert;
    }


    //Treffpunkt
    public static Treffpunkte getTreffpunkte()
    {
        return treffpunkte;
    }

    public static void treffpunkteNeuSetzen(Treffpunkte neuerWert)
    {
        treffpunkte=neuerWert;
    }


    //Mensaplan
    public static Mensaplan getMensaplan()
    {
        return mensaplan;
    }

    public static void mensaplanNeuSetzen(Mensaplan neuerWert)
    {
        mensaplan=neuerWert;
    }


    //Nutzerdaten
    public static Nutzerdaten getNutzerdaten()
    {
        return nutzerdaten;
    }

    public static void nutzerdatenNeuSetzen(Nutzerdaten neuerWert)
    {
        nutzerdaten=neuerWert;
    }

    //DropdownMenue
    public static DropdownMenue getDropdownMenue()
    {
        return dropdownMenue;
    }

    public static void dropdownMenueNeuSetzen(DropdownMenue neuerWert)
    {
        dropdownMenue=neuerWert;
    }


    //Allgemeines Speichern und Lesen
    //Falls einzelne Daten verändert oder gelöscht werden, wird die Exception geworfen, darauf muss an entsprechender Stelle reagiert werden
    private static <T extends Serializable> T lesen(String dateiname) throws Exception
    {
        T transferred=null;

        File file=new File(getSpeicherPfad()+dateiname);


        FileInputStream fileInputStream=new FileInputStream(file);
        ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
        transferred=(T) objectInputStream.readObject();

        objectInputStream.close();
        fileInputStream.close();

        return transferred;
    }



    private static <T extends Serializable> void schreiben(T objekt, String dateiname)
    {
        File file=new File(getSpeicherPfad()+dateiname);
        file.getParentFile().mkdirs();

        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(objekt);

            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch(Exception keineGefahrException)
        {
            //Die Gefahr ist gebannt, da die für File benötigten Ordner und Datei erstellt werden, insofern sie nicht schon vorhanden sind
            keineGefahrException.printStackTrace();
        }
    }

    private static void kopieren(File von, String dateiname)
    {
        File zu=new File(getSpeicherPfad()+dateiname);
        zu.getParentFile().mkdirs();

        FileChannel eingang = null;
        FileChannel ausgang = null;

        try
        {
            eingang = new FileInputStream(von).getChannel();
            ausgang = new FileOutputStream(zu).getChannel();
            eingang.transferTo(0, eingang.size(), ausgang);

            eingang.close();
            ausgang.close();
        }
        catch (Exception keineGefahrException)
        {
            //Die Gefahr ist gebannt, da die für den File benötigten Ordner und Datei erstellt werden, insofern sie nicht schon vorhanden sind
            keineGefahrException.printStackTrace();
        }
    }

    public static boolean isInternetVerbindungVorhanden(String url)
    {
        try
        {
            new URL(url).openConnection().connect();

            return true;
        }
        catch(Exception keineGefahrException)
        {
            //Es ist gewollt, dass manchmal hier reingesprungen wird, nämlich dann wenn keine Verbindung zum Ziel aufgebaut werden kann
            return false;
        }
    }

    private static String getSpeicherPfad()
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
