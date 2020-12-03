package Model.NutzerdatenModel;

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
