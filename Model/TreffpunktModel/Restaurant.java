package Model.TreffpunktModel;



import java.io.Serializable;



public class Restaurant extends Treffpunkt implements Serializable
{
    private String art;
    private String nationalitaet;
    private boolean lieferdienst;



    public Restaurant(String name, String ort, boolean wetterunabhaengig, String information, String art, String nationalitaet, boolean lieferdienst)
    {
        super(name, ort, wetterunabhaengig, information);
        this.art=art;
        this.nationalitaet=nationalitaet;
        this.lieferdienst=lieferdienst;
    }


    public String getArt()
    {
        return art;
    }

    public String getNationalitaet()
    {
        return nationalitaet;
    }

    public boolean getLieferdienst()
    {
        return lieferdienst;
    }
}
