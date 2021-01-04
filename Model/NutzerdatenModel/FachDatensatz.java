package Model.NutzerdatenModel;

import java.io.Serializable;
import java.util.ArrayList;

public class FachDatensatz implements Serializable
{
    private ArrayList<Aufgabe> aufgaben;
    private ArrayList<Notiz> notizen;
    private ArrayList<Note> noten;

    public FachDatensatz(ArrayList<Aufgabe> aufgaben, ArrayList<Notiz> notiz, ArrayList<Note> noten)
    {
        this.aufgaben = aufgaben;
        this.notizen = notiz;
        this.noten = noten;
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
