package Model.MensaplanModel;

import java.io.Serializable;
import java.util.ArrayList;

public final class Mensaplan implements Serializable
{
    private ArrayList<Tagesplan> wochenplan;

    public Mensaplan(ArrayList<Tagesplan> wochenplan)
    {
        this.wochenplan = wochenplan;
    }

    public ArrayList<Tagesplan> getWochenplan()
    {
        return wochenplan;
    }
}
