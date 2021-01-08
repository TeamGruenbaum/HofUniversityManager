package Model.MensaplanModel;



import Model.Tag;
import Model.Datum;


import java.io.Serializable;
import java.util.ArrayList;



public class Tagesplan implements Serializable
{
    private ArrayList<Gericht> gerichte;
    private Tag tag;
    private Datum datum;



    public Tagesplan(ArrayList<Gericht> gerichte, Tag tag, Datum datum)
    {
        this.gerichte=gerichte;
        this.tag=tag;
        this.datum=datum;
    }


    public ArrayList<Gericht> getGerichte()
    {
        return gerichte;
    }

    public Tag getTag()
    {
        return tag;
    }

    public Datum getDatum()
    {
        return datum;
    }
}
