package Model.NutzerdatenModel;

import Model.DropdownModel.Studiengang;
import Model.DropdownModel.Studiensemester;

import java.io.Serializable;
import java.util.ArrayList;

public class Nutzerdaten implements Serializable
{
    private Studiensemester studiensemester;
    private ArrayList<Doppelstunde> doppelstunden;
    private Login ssoLogin;
    private Thema aktuellesThema;
    private Anwendung letzteGeoeffneteAnwendung;
    private Studiengang studiengang;

    public Nutzerdaten(Studiensemester studiensemester, ArrayList<Doppelstunde> doppelstunden, Login ssoLogin, Thema aktuellesThema, Anwendung letzteGeoeffneteAnwendung)
    {
        this.studiensemester = studiensemester;
        this.doppelstunden = doppelstunden;
        this.ssoLogin = ssoLogin;
        this.aktuellesThema = aktuellesThema;
        this.letzteGeoeffneteAnwendung = letzteGeoeffneteAnwendung;
    }

    public Studiensemester getStudiensemester()
    {
        return studiensemester;
    }

    public void setStudiensemester(Studiensemester studiensemester) {
        this.studiensemester = studiensemester;
    }

    public Studiengang getStudiengang() {
        return studiengang;
    }

    public void setStudiengang(Studiengang studiengang) {
        this.studiengang = studiengang;
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

    public void setAktuellesThema(Thema thema)
    {
        aktuellesThema = thema;
    }

    public Anwendung getLetzteGeoeffneteAnwendung()
    {
        return letzteGeoeffneteAnwendung;
    }

    public void setLetzteGeoeffneteAnwendung(Anwendung anwendung)
    {
        letzteGeoeffneteAnwendung=anwendung;
    }
}
