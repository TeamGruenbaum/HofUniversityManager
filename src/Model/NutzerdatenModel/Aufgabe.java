package Model.NutzerdatenModel;



import Model.Datum;
import Model.Uhrzeit;

import java.io.Serializable;



public class Aufgabe implements Serializable
{
    private String name;
    private String inhalt;
    private Datum faelligkeitsdatumDatum;
    private Uhrzeit faelligkeitsuhrzeitUhrzeit;
    private String fach;



    public Aufgabe (String name, String inhalt, Datum faelligkeitsdatumDatum, Uhrzeit faelligkeitsuhrzeitUhrzeit, String fach)
    {
        this.name=name;
        this.inhalt=inhalt;
        this.faelligkeitsdatumDatum=faelligkeitsdatumDatum;
        this.faelligkeitsuhrzeitUhrzeit=faelligkeitsuhrzeitUhrzeit;
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

    public Datum getFaelligkeitsdatum()
    {
        return faelligkeitsdatumDatum;
    }

    public void setFaelligkeitsdatum(Datum neuerWert)
    {
        this.faelligkeitsdatumDatum=neuerWert;
    }

    public Uhrzeit getFaelligkeitsuhrzeit()
    {
        return faelligkeitsuhrzeitUhrzeit;
    }

    public String getFach()
    {
        return fach;
    }
}
