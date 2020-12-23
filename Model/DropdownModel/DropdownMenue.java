package Model.DropdownModel;

import java.util.ArrayList;

public class DropdownMenue
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
