package Model.StundenplanaenderungModel;

import Model.Datum;
import Model.Uhrzeit;

public class Termin
{
    private Datum datum;
    private Uhrzeit uhrzeit;
    private String raum;

    public Termin(Datum datum, Uhrzeit uhrzeit, String raum)
    {
        this.datum=datum;
        this.uhrzeit=uhrzeit;
        this.raum=raum;
    }

    public Datum getDatum()
    {
        return datum;
    }

    public Uhrzeit getUhrzeit()
    {
        return uhrzeit;
    }

    public String getRaum()
    {
        return raum;
    }
}
