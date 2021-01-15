package Model.NutzerdatenModel;



import Model.Datum;
import Model.Tag;
import Model.Uhrzeit;

import java.io.Serializable;



public class Doppelstunde implements Serializable
{
    //nullable
    private Datum datum;
    private String name;
    private String dozent;
    //nullable
    private String raum;
    //nullable
    private Tag tag;
    //nullable
    private Uhrzeit beginnUhrzeit;
    //nullable
    private Uhrzeit endeUhrzeit;



    public Doppelstunde(Datum datum, String name, String dozent, String raum, Tag tag, Uhrzeit beginnUhrzeit, Uhrzeit endeUhrzeit)
    {
        this.datum=datum;
        this.name=name;
        this.dozent=dozent;
        this.raum=raum;
        this.tag=tag;
        this.beginnUhrzeit=beginnUhrzeit;
        this.endeUhrzeit=endeUhrzeit;
    }

    public Datum getDatum()
    {
        return datum;
    }

    public void setDatum(Datum neuerWert)
    {
        this.datum=neuerWert;
    }

    public String getName() {
        return name;
    }

    public void setName(String neuerWert) {
        this.name=neuerWert;
    }

    public String getDozent() {
        return dozent;
    }

    public void setDozent(String neuerWert) {
        this.dozent=neuerWert;
    }

    public String getRaum() {
        return raum;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag neuerWert) {
        this.tag=neuerWert;
    }

    public Uhrzeit getBeginnUhrzeit() {
        return beginnUhrzeit;
    }

    public Uhrzeit getEndeUhrzeit() {
        return endeUhrzeit;
    }
}
