package Model.TreffpunktModel;

import Controller.Speicher.SchreiberLeser;

import java.io.Serializable;
import java.util.ArrayList;

public class Treffpunkte implements Serializable
{
    private ArrayList<Treffpunkt> treffpunkte;

    private Treffpunkte()
    {

    }

    public void set(ArrayList<Treffpunkt> treffpunkte){
        this.treffpunkte = treffpunkte;
    }

    public ArrayList<Treffpunkt> getTreffpunkte() {
        return treffpunkte;
    }
}
