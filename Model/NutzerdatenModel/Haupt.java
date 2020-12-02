package Model.NutzerdatenModel;

import Model.Anwendung;

public final class Haupt
{
    private static Anwendung letzteGeoeffneteAnwendung;

    public static Anwendung getLetzteGeoeffneteAnwendung()
    {
        return letzteGeoeffneteAnwendung;
    }

    public static void setLetzteGeoeffneteAnwendung(Anwendung neuerWert)
    {
        letzteGeoeffneteAnwendung=neuerWert;
    }
}
