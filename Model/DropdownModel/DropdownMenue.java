package Model.DropdownModel;

import java.io.Serializable;
import java.util.ArrayList;

public class DropdownMenue implements Serializable
{
    private ArrayList<Studiengang> eintraege;

    public DropdownMenue(ArrayList<Studiengang> eintraege)
    {
        this.eintraege=eintraege;
    }

    public ArrayList<Studiengang> getEintraege()
    {
        return eintraege;
    }
}
