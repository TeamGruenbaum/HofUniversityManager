package Model.NutzerdatenModel;



import Model.DropdownModel.Studiengang;
import Model.DropdownModel.Studiensemester;
import Model.OberflaechenModel.MenuepunktInformation;

import java.io.Serializable;
import java.util.ArrayList;



public class Nutzerdaten implements Serializable
{
    //nullable
    private Studiengang ausgewaehlterStudiengangStudiengang;
    //nullable
    private Studiensemester ausgewaehltesStudiensemesterStudiensemester;
    private ArrayList<Doppelstunde> doppelstunden;
    private ArrayList<Aufgabe> aufgaben;
    private ArrayList<Notiz> notizen;
    private ArrayList<Note> noten;
    private ArrayList<String> faecher;
    private Login ssoLogin;
    private Thema aktuellesThema;
    private MenuepunktInformation letzterGeoeffneterMenuepunkt;




    public Nutzerdaten(Studiengang ausgewaehlterStudiengangStudiengang, Studiensemester studiensemester,
                       ArrayList<Doppelstunde> doppelstunden, ArrayList<Aufgabe> aufgaben, ArrayList<Notiz> notizen, ArrayList<Note> noten, ArrayList<String> faecher,Login ssoLogin,
                       Thema aktuellesThema, MenuepunktInformation letzterGeoeffneterMenuepunkt)
    {
        this.ausgewaehlterStudiengangStudiengang=ausgewaehlterStudiengangStudiengang;
        this.ausgewaehltesStudiensemesterStudiensemester= studiensemester;
        this.doppelstunden = doppelstunden;
        this.aufgaben=aufgaben;
        this.notizen=notizen;
        this.noten=noten;
        this.faecher=faecher;
        this.ssoLogin = ssoLogin;
        this.aktuellesThema = aktuellesThema;
        this.letzterGeoeffneterMenuepunkt = letzterGeoeffneterMenuepunkt;
    }

    public Studiengang getAusgewaehlterStudiengang()
    {
        return ausgewaehlterStudiengangStudiengang;
    }

    public void setAusgewaehlterStudiengang(Studiengang neuerWert)
    {
        this.ausgewaehlterStudiengangStudiengang=neuerWert;
    }

    public Studiensemester getAusgewaehltesStudiensemester()
    {
        return ausgewaehltesStudiensemesterStudiensemester;
    }

    public void setAusgewaehltesStudiensemester(Studiensemester neuerWert)
    {
        this.ausgewaehltesStudiensemesterStudiensemester=neuerWert;
    }

    public ArrayList<Doppelstunde> getDoppelstunden()
    {
        return doppelstunden;
    }

    public void setDoppelstunden(ArrayList<Doppelstunde> neuerWert)
    {
        doppelstunden=neuerWert;
    }

    public Login getSsoLogin()
    {
        return ssoLogin;
    }

    public Thema getAktuellesThema()
    {
        return aktuellesThema;
    }

    public void setAktuellesThema(Thema neuerWert)
    {
        aktuellesThema = neuerWert;
    }

    public MenuepunktInformation getLetzterGeoeffneterMenuepunkt()
    {
        return letzterGeoeffneterMenuepunkt;
    }

    public void setLetzterGeoeffneterMenuepunkt(MenuepunktInformation neuerWert)
    {
        letzterGeoeffneterMenuepunkt=neuerWert;
    }

    public ArrayList<Aufgabe> getAufgaben()
    {
        return aufgaben;
    }

    public ArrayList<Notiz> getNotizen()
    {
        return notizen;
    }

    public ArrayList<Note> getNoten()
    {
        return noten;
    }

    public ArrayList<String> getFaecher()
    {
        return faecher;
    }

    public void setFaecher(ArrayList<String> neuerWert)
    {
        this.faecher=neuerWert;
    }
}
