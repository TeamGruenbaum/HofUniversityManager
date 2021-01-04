package Model.NutzerdatenModel;

import java.io.Serializable;

public class Notiz implements Serializable
{
    private String ueberschrift;
    private String inhalt;
    private String fach;

    public Notiz (String ueberschrift, String inhalt, String fach)
    {
        this.ueberschrift=ueberschrift;
        this.inhalt=inhalt;
        this.fach=fach;
    }

    public String getUeberschrift() {
        return ueberschrift;
    }

    public void setUeberschrift(String ueberschrift) {
        this.ueberschrift = ueberschrift;
    }

    public String getInhalt() {
        return inhalt;
    }

    public void setInhalt(String inhalt) {
        this.inhalt = inhalt;
    }

    public String getFach() {
        return fach;
    }

    public void setFach(String fach) {
        this.fach = fach;
    }
}
