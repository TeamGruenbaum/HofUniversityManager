package Model.MensaplanModel;



import java.io.Serializable;
import java.util.ArrayList;



public class Mensaplan implements Serializable
{
    private ArrayList<Tagesplan> tagesplaene;



    public Mensaplan(ArrayList<Tagesplan> wochenplan)
    {
        this.tagesplaene = wochenplan;
    }


    public ArrayList<Tagesplan> getTagesplaene()
    {
        return tagesplaene;
    }
}
