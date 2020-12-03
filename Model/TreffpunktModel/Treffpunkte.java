package Model.TreffpunktModel;

import java.util.ArrayList;

public final class Treffpunkte {
    private static ArrayList<Treffpunkt> treffpunkte;

    private Treffpunkte(){

    }

    public static void set(ArrayList<Treffpunkt> treffpunkte){
        Treffpunkte.treffpunkte = treffpunkte;
    }

    public static ArrayList<Treffpunkt> getTreffpunkte() {
        return treffpunkte;
    }
}
