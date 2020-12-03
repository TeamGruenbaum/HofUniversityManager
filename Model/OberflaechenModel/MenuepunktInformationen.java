package Model.OberflaechenModel;

import Model.NutzerdatenModel.Anwendung;

public class MenuepunktInformationen
{
    public Anwendung anwendung;
    public String iconDateiname;
    public String fxmlDateiname;

    public MenuepunktInformationen(Anwendung anwendung, String iconDateiname, String fxmlDateiname)
    {
        this.anwendung=anwendung;
        this.iconDateiname=iconDateiname;
        this.fxmlDateiname=fxmlDateiname;
    }
}
