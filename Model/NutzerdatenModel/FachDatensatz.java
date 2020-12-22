package Model.NutzerdatenModel;

import java.util.ArrayList;

public class FachDatensatz
{
    private String name;
    private ArrayList<Aufgabe> aufgaben;
    private ArrayList<Notiz> notizen;
    private ArrayList<Note> noten;

    public FachDatensatz(String name, ArrayList<Aufgabe> aufgaben, ArrayList<Notiz> notiz, ArrayList<Note> noten)
    {
        this.name=name;
        this.aufgaben = aufgaben;
        this.notizen = notizen;
        this.noten = noten;
    }

    public String getName() {
        return name;
    }

    public void setName(String name)
    {
        this.name=name;
    }

    public ArrayList<Aufgabe> getAufgaben() {
        return aufgaben;
    }

    public ArrayList<Notiz> getNotizen() {
        return notizen;
    }

    public ArrayList<Note> getNoten() {
        return noten;
    }
}
