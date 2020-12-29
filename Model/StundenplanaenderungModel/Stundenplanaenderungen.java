package Model.StundenplanaenderungModel;

import java.util.ArrayList;

public class Stundenplanaenderungen
{
    private ArrayList<Stundenplanaenderung> aenderungen;

    public Stundenplanaenderungen(ArrayList<Stundenplanaenderung> aenderungen)
    {
        this.aenderungen=aenderungen;
    }

    public ArrayList<Stundenplanaenderung> getAenderungen()
    {
        return aenderungen;
    }
}
