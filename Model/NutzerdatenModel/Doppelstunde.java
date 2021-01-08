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
    private String raum;
    //nullable
    private Tag tag;
    //nullable
    private Uhrzeit beginn;
    //nullable
    private Uhrzeit ende;



    public Doppelstunde(Datum datum, String name, String dozent, String raum, Tag tag, Uhrzeit beginn, Uhrzeit ende)
    {
        this.datum=datum;
        this.name=name;
        this.dozent=dozent;
        this.raum=raum;
        this.tag=tag;
        this.beginn=beginn;
        this.ende=ende;
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

    public Uhrzeit getBeginn() {
        return beginn;
    }

    public void setBeginn(Uhrzeit neuerWert) {
        this.beginn=neuerWert;
    }

    public Uhrzeit getEnde() {
        return ende;
    }

    public void setEnde(Uhrzeit neuerWert) {
        this.ende=neuerWert;
    }
}
