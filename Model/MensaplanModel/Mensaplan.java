package Model.MensaplanModel;

import java.util.ArrayList;

public final class Mensaplan
{
    private static ArrayList<Tagesplan> wochenPlan;

    private Mensaplan()
    {

    }

    public static ArrayList<Tagesplan> getWochenPlan()
    {
        return wochenPlan;
    }
}
