package Model.MensaplanModel;

import java.io.Serializable;
import java.util.ArrayList;

public final class Mensaplan implements Serializable
{
    private ArrayList<Tagesplan> wochenPlan;

    public Mensaplan(ArrayList<Tagesplan> wochenPlan)
    {
        this.wochenPlan = wochenPlan;
    }

    public ArrayList<Tagesplan> getWochenPlan()
    {
        return wochenPlan;
    }
}
