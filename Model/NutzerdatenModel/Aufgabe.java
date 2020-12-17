package Model.NutzerdatenModel;

import Model.Datum;
import Model.Uhrzeit;

import java.io.Serializable;

public class Aufgabe implements Serializable
{
    private String inhalt;
    private Datum datum;
    private Uhrzeit zeit;

    public Aufgabe (String inhalt, Datum datum, Uhrzeit zeit)
    {
        this.inhalt=inhalt;
        this.datum=datum;
        this.zeit=zeit;
    }



    public String getInhalt() {
        return inhalt;
    }

    public void setInhalt(String inhalt) {
        this.inhalt = inhalt;
    }

    public Datum getDatum() {
        return datum;
    }

    public void setDatum(Datum datum) {
        this.datum = datum;
    }

    public Uhrzeit getZeit() {
        return zeit;
    }

    public void setZeit(Uhrzeit zeit) {
        this.zeit = zeit;
    }
}
