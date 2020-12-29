package Model.OberflaechenModel;

import Model.NutzerdatenModel.Anwendung;

import java.io.Serializable;

public final class MenuepunktInformation implements Serializable
{
    private Anwendung anwendung;
    private String iconDateiname;
    private String fxmlDateiname;

    public MenuepunktInformation(Anwendung anwendung, String iconDateiname, String fxmlDateiname)
    {
        this.anwendung=anwendung;
        this.iconDateiname=iconDateiname;
        this.fxmlDateiname=fxmlDateiname;
    }

    public Anwendung getAnwendung()
    {
        return anwendung;
    }

    public void setAnwendung(Anwendung anwendung)
    {
        this.anwendung = anwendung;
    }

    public String getIconDateiname()
    {
        return iconDateiname;
    }

    public void setIconDateiname(String iconDateiname)
    {
        this.iconDateiname = iconDateiname;
    }

    public String getFxmlDateiname()
    {
        return fxmlDateiname;
    }

    public void setFxmlDateiname(String fxmlDateiname)
    {
        this.fxmlDateiname = fxmlDateiname;
    }
}
