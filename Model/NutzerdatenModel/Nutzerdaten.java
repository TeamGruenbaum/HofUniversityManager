package Model.NutzerdatenModel;

import java.io.Serializable;
import java.util.ArrayList;

public class Nutzerdaten implements Serializable
{
    private int studiensemester;
    private ArrayList<Doppelstunde> doppelstunden;
    private Login ssoLogin;
    private Thema aktuellesThema;
    private Anwendung letzteGeoeffneteAnwendung;

    public Nutzerdaten(int studiensemester, ArrayList<Doppelstunde> doppelstunden, Login ssoLogin, Thema aktuellesThema, Anwendung letzteGeoeffneteAnwendung)
    {
        this.studiensemester = studiensemester;
        this.doppelstunden = doppelstunden;
        this.ssoLogin = ssoLogin;
        this.aktuellesThema = aktuellesThema;
        this.letzteGeoeffneteAnwendung = letzteGeoeffneteAnwendung;
    }

    public int getStudiensemester()
    {
        return studiensemester;
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
