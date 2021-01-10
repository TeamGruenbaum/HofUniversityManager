package Model.ModulhandbuchModel;



import java.io.Serializable;
import java.util.ArrayList;



public class Modulhandbuch implements Serializable
{
    private ArrayList<ModulhandbuchFach> modulhandbuchFaecher;



    public Modulhandbuch(ArrayList<ModulhandbuchFach> modulhandbuchFaecher)
    {
        this.modulhandbuchFaecher=modulhandbuchFaecher;
    }


    public ArrayList<ModulhandbuchFach> getModulhandbuchFaecher()
    {
        return modulhandbuchFaecher;
    }
}
