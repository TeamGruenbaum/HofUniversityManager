package Model.NutzerdatenModel;



import Model.DropdownModel.Studiengang;
import Model.DropdownModel.Studiensemester;
import Model.OberflaechenModel.MenuepunktInformation;

import java.io.Serializable;
import java.util.ArrayList;



public class Nutzerdaten implements Serializable
{
    //nullable
    private Studiensemester studiensemester;
    private ArrayList<Doppelstunde> doppelstunden;
    private FachDatensatz fachDatensatz;
    private ArrayList<String> faecher;
    private Login ssoLogin;
    private Thema aktuellesThema;
    private MenuepunktInformation letzterGeoeffneterMenuepunkt;
    //nullable
    private Studiengang studiengang;



    public Nutzerdaten(Studiengang studiengang, Studiensemester studiensemester,
                       ArrayList<Doppelstunde> doppelstunden, FachDatensatz fachDatensatz, ArrayList<String> faecher,Login ssoLogin,
                       Thema aktuellesThema, MenuepunktInformation letzterGeoeffneterMenuepunkt)
    {
        this.studiengang=studiengang;
        this.studiensemester = studiensemester;
        this.doppelstunden = doppelstunden;
        this.fachDatensatz=fachDatensatz;
        this.faecher=faecher;
        this.ssoLogin = ssoLogin;
        this.aktuellesThema = aktuellesThema;
        this.letzterGeoeffneterMenuepunkt = letzterGeoeffneterMenuepunkt;
    }


    public Studiensemester getStudiensemester()
    {
        return studiensemester;
    }

    public void setStudiensemester(Studiensemester neuerWert)
    {
        this.studiensemester=neuerWert;
    }

    public Studiengang getStudiengang()
    {
        return studiengang;
    }

    public void setStudiengang(Studiengang neuerWert)
    {
        this.studiengang=neuerWert;
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

    public void setAktuellesThema(Thema neuerWert)
    {
        aktuellesThema = neuerWert;
    }

    public MenuepunktInformation getLetzterGeoeffneterMenuepunkt()
    {
        return letzterGeoeffneterMenuepunkt;
    }

    public void setLetzterGeoeffneterMenuepunkt(MenuepunktInformation neuerWert)
    {
        letzterGeoeffneterMenuepunkt=neuerWert;
    }

    public FachDatensatz getFachDatensatz()
    {
        return fachDatensatz;
    }

    public ArrayList<String> getFaecher()
    {
        return faecher;
    }

    public void setFaecher(ArrayList<String> neuerWert)
    {
        this.faecher=neuerWert;
    }
}
