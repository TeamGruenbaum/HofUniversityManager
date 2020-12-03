package Model.NutzerdatenModel;

import java.util.ArrayList;

public final class Haupt
{
    private static int studiensemester;
    private static ArrayList<Fach> faecher;
    private static Login ssoLogin;
    private static Login campusSportLogin;
    private static Thema aktuellesThema;
    private static Anwendung letzteGeoeffneteAnwendung;

    private Haupt ()
    {

    }

    public static int getStudiensemester()
    {
        return studiensemester;
    }

    public static ArrayList<Fach> getFaecher()
    {
        return faecher;
    }

    public static Login getSsoLogin()
    {
        return ssoLogin;
    }

    public static void setSsoLogin(Login login)
    {
        ssoLogin=login;
    }

    public static Login getCampusSportLogin()
    {
        return campusSportLogin;
    }

    public static void setCampusSportLogin(Login login)
    {
        campusSportLogin=login;
    }

    public static Thema getAktuellesThema()
    {
        return aktuellesThema;
    }

    public static void setAktuellesThema(Thema neuesThema)
    {
        aktuellesThema=neuesThema;
    }

    public static Anwendung getLetzteGeoeffneteAnwendung()
    {
        return letzteGeoeffneteAnwendung;
    }

    public static void setLetzteGeoeffneteAnwendung(Anwendung neuerWert)
    {
        letzteGeoeffneteAnwendung=neuerWert;
    }
}
