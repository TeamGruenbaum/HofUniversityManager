package Model.NutzerdatenModel;

import java.io.Serializable;

public class Notiz implements Serializable
{
    private String ueberschrift;
    private String inhalt;

    public Notiz (String ueberschrift, String inhalt)
    {
        this.ueberschrift=ueberschrift;
        this.inhalt=inhalt;
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
}
