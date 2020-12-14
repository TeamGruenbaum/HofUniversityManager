package Model.TreffpunktModel;

import Controller.Speicher.SchreiberLeser;

import java.io.Serializable;
import java.util.ArrayList;

public final class Treffpunkte implements Serializable
{
    private ArrayList<Treffpunkt> treffpunkte;

    public Treffpunkte(ArrayList<Treffpunkt> treffpunkte)
    {
        this.treffpunkte=treffpunkte;
    }

    public ArrayList<Treffpunkt> getTreffpunkte() {
        return treffpunkte;
    }
}
