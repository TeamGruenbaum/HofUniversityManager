package Model.StudiengangModel;

import java.io.Serializable;
import java.util.ArrayList;

public final class StudiengangInformationen implements Serializable
{
    private ArrayList<ModulhandbuchFach> modulhandbuchFaecher;


    public StudiengangInformationen(ArrayList<ModulhandbuchFach> modulhandbuchFaecher)
    {
        this.modulhandbuchFaecher = modulhandbuchFaecher;
    }


    public ArrayList<ModulhandbuchFach> getModulhandbuch() {
        return modulhandbuchFaecher;
    }
}
