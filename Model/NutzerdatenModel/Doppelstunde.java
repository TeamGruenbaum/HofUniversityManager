package Model.NutzerdatenModel;

import Model.Tag;
import Model.Uhrzeit;

import java.io.Serializable;

public class Doppelstunde implements Serializable
{
    private String name;
    private String dozent;
    private String raum;
    private FachDatensatz fachDatensatz;
    private Tag tag;
    private Uhrzeit beginn;
    private Uhrzeit ende;

    public Doppelstunde(String name, String dozent, String raum, Tag tag, Uhrzeit beginn, Uhrzeit ende)
    {
        this.name=name;
        this.dozent=dozent;
        this.raum=raum;
        this.tag=tag;
        this.beginn=beginn;
        this.ende=ende;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDozent() {
        return dozent;
    }

    public void setDozent(String dozent) {
        this.dozent = dozent;
    }

    public String getRaum() {
        return raum;
    }

    public void setRaum(String raum) {
        this.raum = raum;
    }

    public FachDatensatz getFachDatensatz() {
        return fachDatensatz;
    }

    public void setFachDatensatz(FachDatensatz fachDatensatz) {
        this.fachDatensatz = fachDatensatz;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Uhrzeit getBeginn() {
        return beginn;
    }

    public void setBeginn(Uhrzeit beginn) {
        this.beginn = beginn;
    }

    public Uhrzeit getEnde() {
        return ende;
    }

    public void setEnde(Uhrzeit ende) {
        this.ende = ende;
    }

}
