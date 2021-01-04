package Model.NutzerdatenModel;

import Model.Datum;
import Model.Uhrzeit;

import java.io.Serializable;

public class Aufgabe implements Serializable
{
    private String name;
    private String inhalt;
    private Datum datum;
    private Uhrzeit zeit;
    private String fach;

    public Aufgabe (String name, String inhalt, Datum datum, Uhrzeit zeit, String fach)
    {
        this.name=name;
        this.inhalt=inhalt;
        this.datum=datum;
        this.zeit=zeit;
        this.fach=fach;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getFach() {
        return fach;
    }

    public void setFach(String fach) {
        this.fach = fach;
    }
}
