package Model.OberflaechenModel;

import Model.NutzerdatenModel.Anwendung;

public final class MenuepunktInformation
{
    public Anwendung anwendung;
    public String iconDateiname;
    public String fxmlDateiname;

    public MenuepunktInformation(Anwendung anwendung, String iconDateiname, String fxmlDateiname)
    {
        this.anwendung=anwendung;
        this.iconDateiname=iconDateiname;
        this.fxmlDateiname=fxmlDateiname;
    }
}
