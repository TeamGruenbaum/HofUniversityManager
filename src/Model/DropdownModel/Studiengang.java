package Model.DropdownModel;



import java.io.Serializable;
import java.util.ArrayList;



public class Studiengang implements Serializable
{
    private String name;
    private String kuerzel;
    private ArrayList<Studiensemester> studiensemester;



    public Studiengang(String name, String kuerzel, ArrayList<Studiensemester> studiensemester)
    {
        this.name=name;
        this.kuerzel=kuerzel;
        this.studiensemester=studiensemester;
    }


    public String getName()
    {
        return name;
    }

    public String getKuerzel()
    {
        return kuerzel;
    }

    public ArrayList<Studiensemester> getStudiensemester()
    {
        return studiensemester;
    }
}
