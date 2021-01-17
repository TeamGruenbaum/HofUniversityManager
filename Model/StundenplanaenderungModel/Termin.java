package Model.StundenplanaenderungModel;



import Model.Datum;
import Model.Uhrzeit;



public class Termin
{
    private Datum ausfalldatumDatum;
    private Uhrzeit ausfalluhrzeitUhrzeit;
    private String raum;



    public Termin(Datum ausfalldatumDatum, Uhrzeit ausfalluhrzeitUhrzeit, String raum)
    {
        this.ausfalldatumDatum=ausfalldatumDatum;
        this.ausfalluhrzeitUhrzeit=ausfalluhrzeitUhrzeit;
        this.raum=raum;
    }


    public Datum getAusfalldatum()
    {
        return ausfalldatumDatum;
    }
}
