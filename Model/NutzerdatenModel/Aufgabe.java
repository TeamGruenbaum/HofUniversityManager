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


    public String getName()
    {
        return name;
    }

    public void setName(String neuerWert)
    {
        this.name=neuerWert;
    }

    public String getInhalt()
    {
        return inhalt;
    }

    public void setInhalt(String neuerWert)
    {
        this.inhalt=neuerWert;
    }

    public Datum getDatum()
    {
        return datum;
    }

    public void setDatum(Datum neuerWert)
    {
        this.datum=neuerWert;
    }

    public Uhrzeit getZeit()
    {
        return zeit;
    }

    public String getFach()
    {
        return fach;
    }
}
