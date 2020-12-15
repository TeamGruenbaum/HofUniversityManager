package Model.MensaplanModel;

public class Gericht {
    private String kategorie;
    private String name;
    private String beschreibung;
    private int preis;

    public Gericht (String kategorie, String name, String beschreibung, int preis)
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

    public int getPreis() {
        return preis;
    }

    public String getKategorie()
    {
        return kategorie;
    }
}
