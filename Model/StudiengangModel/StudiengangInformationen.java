package Model.StudiengangModel;

import java.io.Serializable;

public final class StudiengangInformationen implements Serializable
{
    private String studiengangLeiter;
    private String studiengangTyp;
    private String spoDataURL;
    private Modulhandbuch modulhandbuch;


    private StudiengangInformationen(String studiengangLeiter, String studiengangTyp, String spoDataURL, Modulhandbuch modulhandbuch)
    {
        this.studiengangLeiter = studiengangLeiter;
        this.studiengangTyp = studiengangTyp;
        this.spoDataURL = spoDataURL;
        this.modulhandbuch = modulhandbuch;
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

    public Modulhandbuch getModulhandbuch() {
        return modulhandbuch;
    }
}
