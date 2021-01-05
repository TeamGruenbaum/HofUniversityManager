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
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;



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
        }catch(Exception e){e.printStackTrace();}
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
        dropdownMenueLaden();
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
        studiengangInformationen=SchreiberLeser.<StudiengangInformationen>lesen(studiengangInformationenDateiname);
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
        SchreiberLeser.<StudiengangInformationen>schreiben(studiengangInformationen, studiengangInformationenDateiname);
    }


    //Treffpunkt
    public static void treffpunkteLaden()
    {
        treffpunkte=SchreiberLeser.<Treffpunkte>lesen(treffpunkteDateiname);
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
        SchreiberLeser.<Treffpunkte>schreiben(treffpunkte, treffpunkteDateiname);
    }


    //Mensaplan
    public static void mensaplanLaden()
    {
        mensaplan=SchreiberLeser.<Mensaplan>lesen(mensaplanDateiname);
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
        SchreiberLeser.<Mensaplan>schreiben(mensaplan, mensaplanDateiname);
    }


    //Nutzerdaten
    public static void nutzerdatenLaden()
    {
        nutzerdaten=SchreiberLeser.<Nutzerdaten>lesen(nutzerdatenDateiname);
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
        SchreiberLeser.<Nutzerdaten>schreiben(nutzerdaten, nutzerdatenDateiname);
    }

    //DropdownMenue
    public static void dropdownMenueLaden()
    {
        dropdownMenue=SchreiberLeser.<DropdownMenue>lesen(dropdownMenueDateiname);
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
        SchreiberLeser.<DropdownMenue>schreiben(dropdownMenue, dropdownMenueDateiname);
    }


    //Allgemeines Speichern und Lesen
    private static <T extends Serializable> T lesen(String dateiname)
    {
        T transferred=null;

        File file=new File(getSpeicherPfad()+dateiname);

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
        catch (Exception e){}
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
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if(eingang != null)
        {
            try
            {
                eingang.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        if(ausgang != null)
        {
            try
            {
                ausgang.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void alleDatenLoeschen()
    {
        try
        {
            FileUtils.forceDelete(new File(getSpeicherPfad()));
        }catch(IOException e)
        {
            e.printStackTrace();
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
