package Model.MensaplanModel;



import java.io.Serializable;



public class Gericht implements Serializable
{
    private String gang;
    private String name;
    private String beschreibung;
    private float preis;



    public Gericht (String gang, String name, String beschreibung, float preis)
    {
        this.gang=gang;
        this.name=name;
        this.beschreibung=beschreibung;
        this.preis=preis;
    }


    public String getGang()
    {
        return gang;
    }

    public String getName()
    {
        return name;
    }

    public String getBeschreibung()
    {
        return beschreibung;
    }

    public float getPreis()
    {
        return preis;
    }
}
