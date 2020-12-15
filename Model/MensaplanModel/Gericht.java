package Model.MensaplanModel;

public class Gericht {
    private String kategorie;
    private String name;
    private String beschreibung;
    private float preis;

    public Gericht (String kategorie, String name, String beschreibung, float preis)
    {
        this.kategorie=kategorie;
        this.name=name;
        this.beschreibung=beschreibung;
        this.preis=preis;
    }

    public String getName() {
        return name;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public float getPreis() {
        return preis;
    }

    public String getKategorie()
    {
        return kategorie;
    }
}
