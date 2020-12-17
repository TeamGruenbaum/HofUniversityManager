package Model.NutzerdatenModel;

import Model.Uhrzeit;

import java.io.Serializable;
import java.util.ArrayList;

public class Fach implements Serializable
{
    private String name;
    private String dozent;
    private Uhrzeit beginn;
    private Uhrzeit ende;
    private String zusatzinfo;
    private String raum;
    private ArrayList<Aufgabe> aufgaben;
    private ArrayList<Notiz> notizen;
    private ArrayList<Note> noten;

    public Fach(String name, String dozent, Uhrzeit beginn, Uhrzeit ende, String zusatzinfo, String raum, ArrayList<Aufgabe> aufgaben, ArrayList<Notiz> notizen, ArrayList<Note> noten)
    {
        this.name = name;
        this.dozent = dozent;
        this.beginn = beginn;
        this.ende = ende;
        this.zusatzinfo = zusatzinfo;
        this.raum = raum;
        this.aufgaben = aufgaben;
        this.notizen = notizen;
        this.noten = noten;
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

    public String getZusatzinfo() {
        return zusatzinfo;
    }

    public void setZusatzinfo(String zusatzinfo) {
        this.zusatzinfo = zusatzinfo;
    }

    public String getRaum() {
        return raum;
    }

    public void setRaum(String raum) {
        this.raum = raum;
    }

    public ArrayList<Aufgabe> getAufgaben()
    {
        return aufgaben;
    }

    public void setAufgaben(ArrayList<Aufgabe> aufgaben)
    {
        this.aufgaben = aufgaben;
    }

    public ArrayList<Notiz> getNotizen()
    {
        return notizen;
    }

    public void setNotizen(ArrayList<Notiz> notizen)
    {
        this.notizen = notizen;
    }

    public ArrayList<Note> getNoten()
    {
        return noten;
    }

    public void setNoten(ArrayList<Note> noten)
    {
        this.noten = noten;
    }
}
