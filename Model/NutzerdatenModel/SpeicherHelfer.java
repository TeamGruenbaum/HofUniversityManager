package Model.NutzerdatenModel;

import Model.Anwendung;

public class SpeicherHelfer
{
    private static Anwendung letzteGeoeffneteAnwendung;

    public static Anwendung getLetzteGeoeffneteAnwendung()
    {
        return letzteGeoeffneteAnwendung;
    }

    public static void setLetzteGeoeffneteAnwendung(Anwendung letzteGeoeffneteAnwendung)
    {
        SpeicherHelfer.letzteGeoeffneteAnwendung = letzteGeoeffneteAnwendung;
    }
}
