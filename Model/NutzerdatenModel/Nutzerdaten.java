package Model.NutzerdatenModel;

import java.io.Serializable;
import java.util.ArrayList;

public class Nutzerdaten implements Serializable
{
    private int studiensemester;
    private ArrayList<Fach> faecher;
    private Login ssoLogin;
    private Login campusSportLogin;
    private Thema aktuellesThema;
    private Anwendung letzteGeoeffneteAnwendung;

    public Nutzerdaten(int studiensemester, ArrayList<Fach> faecher, Login ssoLogin, Login campusSportLogin, Thema aktuellesThema, Anwendung letzteGeoeffneteAnwendung)
    {
        this.studiensemester = studiensemester;
        this.faecher = faecher;
        this.ssoLogin = ssoLogin;
        this.campusSportLogin = campusSportLogin;
        this.aktuellesThema = aktuellesThema;
        this.letzteGeoeffneteAnwendung = letzteGeoeffneteAnwendung;
    }

    public int getStudiensemester()
    {
        return studiensemester;
    }

    public ArrayList<Fach> getFaecher()
    {
        return faecher;
    }

    public Login getSsoLogin()
    {
        return ssoLogin;
    }

    public Login getCampusSportLogin()
    {
        return campusSportLogin;
    }

    public Thema getAktuellesThema()
    {
        return aktuellesThema;
    }

    public Anwendung getLetzteGeoeffneteAnwendung()
    {
        return letzteGeoeffneteAnwendung;
    }
}
