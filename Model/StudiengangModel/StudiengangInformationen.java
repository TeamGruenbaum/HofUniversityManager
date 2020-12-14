package Model.StudiengangModel;

import java.io.Serializable;
import java.util.ArrayList;

public final class StudiengangInformationen implements Serializable
{
    private String studiengangLeiter;
    private String studiengangTyp;
    private String spoDataURL;
    private ArrayList<ModulhandbuchFach> modulhandbuchFaecher;


    public StudiengangInformationen(String studiengangLeiter, String studiengangTyp, String spoDataURL, ArrayList<ModulhandbuchFach> modulhandbuchFaecher)
    {
        this.studiengangLeiter = studiengangLeiter;
        this.studiengangTyp = studiengangTyp;
        this.spoDataURL = spoDataURL;
        this.modulhandbuchFaecher = modulhandbuchFaecher;
    }

    public String getStudiengangLeiter() {
        return studiengangLeiter;
    }

    public String getStudiengangTyp() {
        return studiengangTyp;
    }

    public String getSPO() {
        return spoDataURL;
    }

    public ArrayList<ModulhandbuchFach> getModulhandbuch() {
        return modulhandbuchFaecher;
    }
}
