package Model.NutzerdatenModel;

import Model.OberflaechenModel.Menue;
import Model.OberflaechenModel.MenuepunktInformation;

import java.io.Serializable;
import java.util.ArrayList;

public class Nutzerdaten implements Serializable
{
    private int studiensemester;
    private ArrayList<Doppelstunde> doppelstunden;
    private Login ssoLogin;
    private Thema aktuellesThema;
    private MenuepunktInformation letzterGeoeffneterMenuepunkt;

    public Nutzerdaten(int studiensemester, ArrayList<Doppelstunde> doppelstunden, Login ssoLogin, Thema aktuellesThema, MenuepunktInformation letzterGeoeffneterMenuepunkt)
    {
        this.studiensemester = studiensemester;
        this.doppelstunden = doppelstunden;
        this.ssoLogin = ssoLogin;
        this.aktuellesThema = aktuellesThema;
        this.letzterGeoeffneterMenuepunkt = letzterGeoeffneterMenuepunkt;
    }

    public int getStudiensemester()
    {
        return studiensemester;
    }

    public ArrayList<Doppelstunde> getDoppelstunden()
    {
        return doppelstunden;
    }

    public void setDoppelstunden(ArrayList<Doppelstunde> neuerWert)
    {
        doppelstunden=neuerWert;
    }

    public Login getSsoLogin()
    {
        return ssoLogin;
    }

    public Thema getAktuellesThema()
    {
        return aktuellesThema;
    }

    public void setAktuellesThema(Thema thema)
    {
        aktuellesThema = thema;
    }

    public MenuepunktInformation getLetzterGeoeffneterMenuepunkt()
    {
        return letzterGeoeffneterMenuepunkt;
    }

    public void setLetzterGeoeffneterMenuepunkt(MenuepunktInformation neuerWert)
    {
        letzterGeoeffneterMenuepunkt=neuerWert;
    }
}
